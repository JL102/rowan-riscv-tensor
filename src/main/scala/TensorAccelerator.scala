// package customtensor

// import chisel3._
// import chisel3.util._
// import chisel3.experimental.IO
// import freechips.rocketchip.tile._
// import freechips.rocketchip.config._
// import freechips.rocketchip.diplomacy._
// import freechips.rocketchip.rocket._
// import freechips.rocketchip.rocket.ALU._

// class TensorAccelerator(opcodes: OpcodeSet, val width: Int)
//     (implicit p: Parameters) extends LazyRoCC(opcodes) {
//   override lazy val module = new TensorAcceleratorModule(this, width)
// }

// class TensorAcceleratorModule(outer: TensorAccelerator, width: Int)
//     extends LazyRoCCModuleImp(outer) 
//     with HasCoreParameters {
//   val reg_operands = Mem(outer.width, UInt(xLen.W))
//   val reg_results = Mem(outer.width, UInt(xLen.W))
  
//   val _initializing :: _ready :: _loadA :: _loadB :: 
//     _multiplying :: _outputting :: _responding :: Nil = Enum(7)
  
//   val cmd = io.cmd
//   val funct       = cmd.bits.inst.funct
  
//   cmd.ready := true.B;
  
//   io.resp.valid := true.B;
//   io.resp.bits.rd := cmd.bits.inst.rd;
//   io.resp.bits.data := 12.U;
  
//   io.busy := false.B;
// }

// // If we have them int and not long, we can retrieve 2 at once
// // it appears that HellaCache only allows us to retrieve data in blocks of 8
// // so adding 8 to the mem address brings us up by 64 bits


// class WithTensorAccelerator(width: Int) extends Config((site, here, up) => {
//   case BuildRoCC => Seq(
//       (p: Parameters) => {
//         implicit val q = p
//         implicit val v = implicitly[ValName]
//         LazyModule(new TensorAccelerator(OpcodeSet.custom1, width)(p))
//     }
//   )
// })


//   // The parts of the command are as follows
//   // inst - the parts of the instruction itself
//   //   opcode
//   //   rd - destination register number
//   //   rs1 - first source register number
//   //   rs2 - second source register number
//   //   funct
//   //   xd - is the destination register being used?
//   //   xs1 - is the first source register being used?
//   //   xs2 - is the second source register being used?f
//   // rs1 - the value of source register 1
//   // rs2 - the value of source register 2