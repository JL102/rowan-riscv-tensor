import chisel3._
import chisel3.util.{switch, is}

//This decoder is very bad
//Do not attempt to learn anything from this

class Decoder8_1 extends Module {
  val io = IO(new Bundle {
    val sel = Input(UInt(3.W))
    val in = Input(UInt(64.W))
    val out0 = Output(UInt(64.W))
    val out1 = Output(UInt(64.W))
    val out2 = Output(UInt(64.W))
    val out3 = Output(UInt(64.W))
    val out4 = Output(UInt(64.W))
    val out5 = Output(UInt(64.W))
    val out6 = Output(UInt(64.W))
    val out7 = Output(UInt(64.W))

  })


//Decoder passes input to designated output
//But what happens for all the other outputs?
//High Z?
  switch ( io.sel ) {

    is(0.U) {
      io.out0 := io.in
    }
    is(1.U) {
      io.out1 := io.in
    }
    is(2.U) {
      io.out2 := io.in
    }
    is(3.U) {
      io.out3 := io.in
    }
    is(4.U) {
      io.out4 := io.in
    }
    is(5.U) {
      io.out5 := io.in
    }
    is(6.U) {
      io.out6 := io.in
    }
    is(7.U) {
      io.out7 := io.in
    }
}
}
/*
object Decoder8 {
  def apply(sel: UInt, in0: UInt, in1: UInt) = {
    val m = Module(new Decoder)
    m.io.in0 := in0
    m.io.in1 := in1
    m.io.sel := sel
    m.io.out
  }

  
}
*/
