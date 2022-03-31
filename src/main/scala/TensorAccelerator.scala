package customtensor

import chisel3._
import chisel3.util._
import chisel3.experimental.IO
import freechips.rocketchip.tile._
import freechips.rocketchip.config._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.rocket._
import freechips.rocketchip.rocket.ALU._

// note: == is a Scala equals, which returns a Scala Boolean, while === is a Chisel equals, which returns a Chisel Boolean!

class TensorAccelerator(opcodes: OpcodeSet, val width: Int)
    (implicit p: Parameters) extends LazyRoCC(opcodes) {
  override lazy val module = new TensorAcceleratorModule(this, width)
}

class TensorAcceleratorModule(outer: TensorAccelerator, width: Int)
    extends LazyRoCCModuleImp(outer) 
    with HasCoreParameters {
  // val reg_operands = Mem(outer.width, UInt(xLen.W))
  // val reg_results = Mem(outer.width, UInt(xLen.W))
  
  // These are the memory addresses of the two input matrices and the output matrix
  val addr_A = UInt(xLen.W);
  val addr_B = UInt(xLen.W);
  val addr_C = UInt(xLen.W);
  // These refer to the row of A / column of B that we are loading.
  val idx_row_A = RegInit(0.U(width.W));
  val idx_col_B = RegInit(0.U(width.W));
  // These refer to the actual data in A and B that we must load.
  val reg_row_A = Mem(outer.width, UInt(xLen.W));
  val reg_col_B = Mem(outer.width, UInt(xLen.W));
  // These refer to the specific cell of reg_row_A and reg_col_B that we are currently loading.
  val cell_idx = RegInit(0.U(width.W));
  // This keeps track of whether we're waiting for a memory operation
  val busy = RegInit(false.B)
  
  val memRespTag  = io.mem.resp.bits.tag(log2Up(outer.width)-1,0)
  
  val s_initializing :: s_ready :: s_loadA :: s_loadB :: s_multiplying :: s_outputting :: s_responding :: Nil = Enum(7);
  val state = RegInit(s_initializing);
  
  // needToLoadA and needToLoadB are not used in the initialization stage; they are used after writing to the output matrix
  // Column is EVEN and row is MAX (e.g. bottom left corner of grid)
  // Column is ODD and row is ZERO (e.g. 2nd column at top)
  val needToLoadB = 
    ( (idx_col_B % 2.U === 0.U) && (idx_row_A === width.U - 1.U) ) || ( (idx_col_B % 2.U === 1.U) && (idx_row_A === 0.U) );
  val needToLoadA = !needToLoadB; // We only load one row or column at a time, so when we aren't loading B, we're loading A
  
  // val loadRequested = false.B;
  val multRequested = RegInit(false.B); // Keep track of if the multiply operation has yet been requested
  
  val cmd = io.cmd;
  val funct  = cmd.bits.inst.funct;
  
  when (state === s_initializing) {
    // In case we want to do other intializy stuff
    state := s_ready;
  }
  // Logic to load A's row
  when (state === s_loadA) {
    io.mem.req.valid := !busy;
    io.mem.req.bits.addr := addr_A + (cell_idx * 8.U);
    
    when (io.mem.resp.valid) {
      reg_row_A(memRespTag)     := io.mem.resp.bits.data(64.U, 32.U).asUInt;
      reg_row_A(memRespTag + 1.U) := io.mem.resp.bits.data(31.U, 0.U).asUInt;
      busy := false.B;
      cell_idx := cell_idx + 1.U;
    }
    
    // When memory is busy, set it to busy
    when (io.mem.req.fire()) {
      busy := true.B;
    }
  }
  // Logic to load B's column
  when (state === s_loadB) {
    
  }
  
  
  when (funct === 1.U) { // req to Perform multiplication
    state := s_multiplying;
  }
  // Request to start loading
  //  Only transition if command is valid and we are currently in the "ready" state
  when (funct === 3.U && cmd.valid && state === s_ready) {
    state := s_loadA;
    // Reset all "pointers" for our rows and columns
    idx_row_A := 0.U;
    idx_col_B := 0.U;
    cell_idx := 0.U;
    
    // Memory addresses given by multiply.c
    addr_A := cmd.bits.rs1;
    addr_B := cmd.bits.rs2;
    
    var i = 0;
    for (i <- 0 to width) {
      reg_row_A(i.U) := 0.U;
      reg_col_B(i.U) := 0.U;
    }
  }
  // Request to multiply & return
  when (funct === 7.U && cmd.valid) {
    multRequested := true.B;
  }
  
  cmd.ready := true.B;
  
  io.resp.valid := cmd.valid || (cmd.valid && needToLoadB); // last bit is intentional garbage, delete when not needed
  io.resp.bits.rd := cmd.bits.inst.rd;
  io.resp.bits.data := 12.U;
  
  io.busy := cmd.valid || (state === s_responding); 
}

// If we have them int and not long, we can retrieve 2 at once
// it appears that HellaCache only allows us to retrieve data in blocks of 8
// so adding 8 to the mem address brings us up by 64 bits


class WithTensorAccelerator(width: Int) extends Config((site, here, up) => {
  case BuildRoCC => Seq(
      (p: Parameters) => {
        implicit val q = p
        implicit val v = implicitly[ValName]
        LazyModule(new TensorAccelerator(OpcodeSet.custom1, width)(p))
    }
  )
})


  // The parts of the command are as follows
  // inst - the parts of the instruction itself
  //   opcode
  //   rd - destination register number
  //   rs1 - first source register number
  //   rs2 - second source register number
  //   funct
  //   xd - is the destination register being used?
  //   xs1 - is the first source register being used?
  //   xs2 - is the second source register being used?f
  // rs1 - the value of source register 1
  // rs2 - the value of source register 2