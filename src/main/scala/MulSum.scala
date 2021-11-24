import chisel3._


class MulSum(width: Int)(numInputs: Int) extends Module { 
  val io = IO(new Bundle {
	val MA = Input(Vec(numInputs, UInt(width.W))); //Creates a bundle of wire, there "numInput" wires, and each wire is "width" bits
	val MB = Input(Vec(numInputs, UInt(width.W)));
	val out = Output(UInt(width.W));
  });




   io.out := (io.MA(0) * io.MB(0)) + (io.MA(1) * io.MB(1)) + (io.MA(2) * io.MB(2)) + (io.MA(3) * io.MB(3));

}



object MultiplierOBJ extends App {
  (new chisel3.stage.ChiselStage).emitVerilog(new MulSum(64)(4))
}
