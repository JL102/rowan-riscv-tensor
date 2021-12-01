package customtensor

import chisel3._
import chisel3.util._
import chisel3.experimental.IO
import freechips.rocketchip.tile._
import freechips.rocketchip.config._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.rocket._
import freechips.rocketchip.rocket.ALU._

class CustomAccelerator(opcodes: OpcodeSet, val width: Int)
    (implicit p: Parameters) extends LazyRoCC(opcodes) {
  override lazy val module = new CustomAcceleratorModule(this, width)
}

class CustomAcceleratorModule(outer: CustomAccelerator, width: Int)
    extends LazyRoCCModuleImp(outer) 
    with HasCoreParameters {
  val reg_operands = Mem(outer.width, UInt(xLen.W))
  val reg_results = Mem(outer.width, UInt(xLen.W))
  
  val busy = RegInit(VecInit(Seq.fill(outer.width){false.B})) // way of keeping track of what's busy and what's not
  val imul = Module(new PipelinedMultiplier(xLen, 2)) // numStages can change at some point I think
  // todo: keep track of when multiplier is busy
  val imul_busy = RegInit(VecInit(Seq.fill(1){false.B}))
  
  val cmd = Queue(io.cmd)	
  val funct       = cmd.bits.inst.funct
  val matrixIdx   = cmd.bits.rs1(log2Up(outer.width)-1,0)
  val memAddress  = cmd.bits.rs2
  val doMultiply  = funct === 1.U // Perform multiplication
  val doLoad      = funct === 3.U // Load matrices from memory
  val doRetrieve  = funct === 4.U // Retrieve result
  
  val memRespTag  = io.mem.resp.bits.tag(log2Up(outer.width)-1,0)
  
  // Multiply the stored values
  val a = reg_operands(0.U)
  val b = reg_operands(1.U)
  val wdata_multiply = reg_results(0.U)
  
  // When memory is busy, set it to busy
  when (io.mem.req.fire()) {
    busy(matrixIdx) := true.B
  }
  
  // Ran into issues when using imul.io.req.fire() 
  //    (I think fire() returned true when I didn't expect it to)
  //  Since I'm controlling the multiplier, I know when it's busy or not
  //  so I don't need to wait for it to "fire" to know it's gonna be busy
  when (imul.io.req.valid && !imul.io.resp.valid) {
    imul_busy(0.U) := true.B
  }
  
  // When memory load has been returned
  when (io.mem.resp.valid) {
    reg_operands(memRespTag) := io.mem.resp.bits.data.asUInt // mem has to be uint
    busy(memRespTag) := false.B
  }
  
  // When multiplier is finished
  when (imul.io.resp.valid) {
    reg_results(0.U) := imul.io.resp.bits.data
    imul_busy(0.U) := false.B
  }
  
  
  // datapath?
  val anyLoadsBusy = busy.reduce(_||_)
  val anyImulBusy = imul_busy.reduce(_||_)
  val doResp = cmd.bits.inst.xd
  val stallReg = busy(matrixIdx)
  val stallLoad = doLoad && !io.mem.req.ready
  val stallResp = doResp && !io.resp.ready 
  val stallMul = doMultiply && !imul.io.resp.valid || anyImulBusy
  
  // Prepare the multiplier
  //   Only activate the multiplier when the command is valid
  imul.io.req.valid := cmd.valid && doMultiply && !anyLoadsBusy
  imul.io.req.bits.fn := FN_MUL // riscv instruction: MUL is fine
  imul.io.req.bits.dw := true.B // 64b doubleword
  imul.io.req.bits.in1 := a 
  imul.io.req.bits.in2 := b 
  imul.io.req.bits.tag := DontCare 
  
  cmd.ready := !stallReg && !stallLoad && !stallResp && !stallMul
    // command resolved if no stalls AND not waiting for a load
  
  // this is where the results of the multiply is sent
  io.resp.valid := cmd.valid && doResp && !stallReg && !stallMul
    // valid response if valid command, need a response, and no stalls
  io.resp.bits.rd := cmd.bits.inst.rd
    // Must respond with the appropriate tag or undefined behavior
  io.resp.bits.data := wdata_multiply
  
  io.busy := cmd.valid || anyLoadsBusy || anyImulBusy
    // Be busy when have pending memory requests or committed possibility of pending requests
  io.interrupt := false.B
  
  // MEMORY REQUEST INTERFACE
  io.mem.req.valid := cmd.valid && doLoad && !stallReg && !stallResp
  io.mem.req.bits.addr := memAddress
  io.mem.req.bits.tag := matrixIdx
  io.mem.req.bits.cmd := M_XRD // perform a load
  io.mem.req.bits.size := log2Ceil(8).U 
  io.mem.req.bits.signed := true.B //might change
  io.mem.req.bits.data := 0.U // Not storing anything
  io.mem.req.bits.phys := false.B // not sure what this is
  io.mem.req.bits.dprv := cmd.bits.status.dprv // not sure what this is
  
}

class WithCustomAccelerator(width: Int) extends Config((site, here, up) => {
  case BuildRoCC => Seq(
      (p: Parameters) => {
        implicit val q = p
        implicit val v = implicitly[ValName]
        LazyModule(new CustomAccelerator(OpcodeSet.custom1, width)(p))
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