import chisel3._
import chisel3.util.{switch, is}

//This decoder is very bad
//Do not attempt to learn anything from this

class Decoder4_1 extends Module {
  val io = IO(new Bundle {
    val sel = Input(Bool(2.W))
    val in = Input(UInt(64.W))
    val out0 = Output(UInt(64.W))
    val out1 = Output(UInt(64.W))
    val out2 = Output(UInt(64.W))
    val out3 = Output(UInt(64.W))
  });


//Decoder passes input to designated output
//But what happens for all the other outputs?
//High Z?
  switch ( sel ) {

    is(0) {
      io.out0 := io.in
    }
    is(1) {
      io.out1 := io.in
    }
    is(2) {
      io.out2 := io.in
    }
    is(3) {
      io.out3 := io.in
    }
    is(4) {
      io.out3 := io.in //This should never happen
    }
}
}

object Decoder4 {
  def apply(sel: UInt, in0: UInt, in1: UInt) = {
    val m = Module(new Mux2)
    m.io.in0 := in0
    m.io.in := in
    m.io.sel := sel
    m.io.out
  }
}
