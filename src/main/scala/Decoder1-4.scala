import chisel3._
import chisel3.util.{switch, is}

//This decoder is very bad
//Do not attempt to learn anything from this

class Decoder4_1 extends Module {
  val io = IO(new Bundle {
    val sel = Input(UInt(2.W))
    val in = Input(UInt(64.W))
    val out0 = Output(UInt(64.W))
    val out1 = Output(UInt(64.W))
    val out2 = Output(UInt(64.W))
    val out3 = Output(UInt(64.W))
  });


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
      io.out3 := io.in //This should never happen
    }
}
}

//Not sure about this stuff down here, it compiles, but doesn't match the stuff in decoder1-8
object Decoder4 {
  def apply(sel: UInt, in: UInt) = {
    val m = Module(new Decoder4_1)
    m.io.in := in


  }
}
