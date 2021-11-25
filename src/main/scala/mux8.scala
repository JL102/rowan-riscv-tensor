class Mux8 extends Module {
  val io = IO(new Bundle {
    val in0 = Input(UInt(64.W))
    val in1 = Input(UInt(64.W))
    val in2 = Input(UInt(64.W))
    val in3 = Input(UInt(64.W))
    val in4 = Input(Uint(64.W))
    val in5 = Input(Uint(64.W))
    val in6 = Input(Uint(64.W))
    val in7 = Input(Uint(64.W))
    val sel = Input(UInt(3.W))
    val out = Output(UInt(1.W))
  })
  io.out := Mux(io.sel, io.in0, io.in1, io.in2, io.in3, io.in4, io.in5, io.in6, io.in7 )
}

object Mux8 {
  def apply(sel: UInt, sel2: UInt, sel3: UInt, in0: UInt, in1: UInt, in2: UInt, in3: UInt, in4: UInt, in5: UInt, in6: UInt, in7: UInt) = {
    val m = Module(new Mux8)
    m.io.in0 := in0
    m.io.in1 := in1
    m.io.in2 := in2
    m.io.in3 := in3
    m.io.in4 := in4
    m.io.in5 := in5
    m.io.in6 := in6
    m.io.in7 := in7
    m.io.in8 := in8
    m.io.sel := sel
    m.io.out
  }
}
