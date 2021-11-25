package customtensor

import chisel3._
import chisel3.util._
import chisel3.experimental.IO
import freechips.rocketchip.tile._
import freechips.rocketchip.config._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.rocket.{TLBConfig, HellaCacheReq}

class CustomAccelerator(opcodes: OpcodeSet, width: Int)
    (implicit p: Parameters) extends LazyRoCC(opcodes) {
  override lazy val module = new CustomAcceleratorModule(this, width)
}

class CustomAcceleratorModule(outer: CustomAccelerator, width: Int)
    extends LazyRoCCModuleImp(outer) 
    with HasCoreParameters {
  
  val cmd = Queue(io.cmd)	
  val funct = cmd.bits.inst.funct
  
  
  val a = cmd.bits.rs1
  val b = cmd.bits.rs2
  val wdata = a * b
  
  val doResp = cmd.bits.inst.xd
  val stallResp = doResp && !io.resp.ready
  
  cmd.ready := !stallResp
  
  io.resp.valid := cmd.valid && doResp
    // valid response if valid command, need a response, and no stalls
  io.resp.bits.rd := cmd.bits.inst.rd
    // Must respond with the appropriate tag or undefined behavior
  io.resp.bits.data := wdata
  // val addr = cmd.bits.rs2(log2Up(outer.n)-1, 0)
  
  io.busy := cmd.valid
  io.interrupt := false.B
  // io.out := io.in + 1.U;
  
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
}

class WithCustomAccelerator(width: Int) extends Config((site, here, up) => {
  case BuildRoCC => Seq(
      (p: Parameters) => {
        implicit val q = p
        implicit val v = implicitly[ValName]
        LazyModule(new CustomAccelerator(OpcodeSet.custom1, width)(p))
    }
  )
  // case BuildRoCC => Seq((p: Parameters) => LazyModule(

  // 	new CustomAccelerator(OpcodeSet.custom0 | OpcodeSet.custom1)(p))
  // )
  // case BuildRoCC => up(BuildRoCC) ++ Seq(
  // 	(p: Parameters) => {
  // 		val sha3 = LazyModule.apply(new CustomAccelerator(OpcodeSet.custom2)(p))
  // 		sha3
  // 	}
  // )
})

// class AccumulatorExample(opcodes: OpcodeSet, val n: Int = 4)(implicit p: Parameters) extends LazyRoCC(opcodes) {
//   override lazy val module = new AccumulatorExampleModuleImp(this)
// }

// class AccumulatorExampleModuleImp(outer: AccumulatorExample)(implicit p: Parameters) extends LazyRoCCModuleImp(outer)
//     with HasCoreParameters {
//   val regfile = Mem(outer.n, UInt(xLen.W))
//   val busy = RegInit(VecInit(Seq.fill(outer.n){false.B}))

//   val cmd = Queue(io.cmd)
//   val funct = cmd.bits.inst.funct
//   val addr = cmd.bits.rs2(log2Up(outer.n)-1,0)
//   val doWrite = funct === 0.U
//   val doRead = funct === 1.U
//   val doLoad = funct === 2.U
//   val doAccum = funct === 3.U
//   val memRespTag = io.mem.resp.bits.tag(log2Up(outer.n)-1,0)

//   // datapath
//   val addend = cmd.bits.rs1
//   val accum = regfile(addr)
//   val wdata = Mux(doWrite, addend, accum + addend)

//   when (cmd.fire() && (doWrite || doAccum)) {
//     regfile(addr) := wdata
//   }

//   when (io.mem.resp.valid) {
//     regfile(memRespTag) := io.mem.resp.bits.data
//     busy(memRespTag) := false.B
//   }

//   // control
//   when (io.mem.req.fire()) {
//     busy(addr) := true.B
//   }

//   val doResp = cmd.bits.inst.xd
//   val stallReg = busy(addr)
//   val stallLoad = doLoad && !io.mem.req.ready
//   val stallResp = doResp && !io.resp.ready

//   cmd.ready := !stallReg && !stallLoad && !stallResp
//     // command resolved if no stalls AND not issuing a load that will need a request

//   // PROC RESPONSE INTERFACE
//   io.resp.valid := cmd.valid && doResp && !stallReg && !stallLoad
//     // valid response if valid command, need a response, and no stalls
//   io.resp.bits.rd := cmd.bits.inst.rd
//     // Must respond with the appropriate tag or undefined behavior
//   io.resp.bits.data := accum
//     // Semantics is to always send out prior accumulator register value

//   io.busy := cmd.valid || busy.reduce(_||_)
//     // Be busy when have pending memory requests or committed possibility of pending requests
//   io.interrupt := false.B
//     // Set this true to trigger an interrupt on the processor (please refer to supervisor documentation)

//   // MEMORY REQUEST INTERFACE
//   io.mem.req.valid := cmd.valid && doLoad && !stallReg && !stallResp
//   io.mem.req.bits.addr := addend
//   io.mem.req.bits.tag := addr
//   io.mem.req.bits.cmd := M_XRD // perform a load (M_XWR for stores)
//   io.mem.req.bits.size := log2Ceil(8).U
//   io.mem.req.bits.signed := false.B
//   io.mem.req.bits.data := 0.U // we're not performing any stores...
//   io.mem.req.bits.phys := false.B
//   io.mem.req.bits.dprv := cmd.bits.status.dprv
// }

// class OpcodeSet(val opcodes: Seq[UInt]) {
//   def |(set: OpcodeSet) =
//     new OpcodeSet(this.opcodes ++ set.opcodes)

//   def matches(oc: UInt) = opcodes.map(_ === oc).reduce(_ || _)
// }

// object OpcodeSet {
//   def custom0 = new OpcodeSet(Seq("b0001011".U))
//   def custom1 = new OpcodeSet(Seq("b0101011".U))
//   def custom2 = new OpcodeSet(Seq("b1011011".U))
//   def custom3 = new OpcodeSet(Seq("b1111011".U))
//   def all = custom0 | custom1 | custom2 | custom3
// }