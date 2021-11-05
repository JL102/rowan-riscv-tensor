import chisel3._


// Chisel Code, but pass in a parameter to set widths of ports
class MulSum(width: Int)(numInputs: Int) extends Module { 
  val io = IO(new Bundle {
	/*
	val MA_1 = Input(UInt(width.W));
	val MA_2 = Input(UInt(width.W));
	val MA_3 = Input(UInt(width.W));

	val MB_1 = Input(UInt(width.W));
	val MB_2 = Input(UInt(width.W));
	val MB_3 = Input(UInt(width.W));
	*/
	val MA = Input(Vec(numInputs, UInt(width.W))); //Creates a bundle of wire, there "numInput" wires, and each wire is "width" bits
	val MB = Input(Vec(numInputs, UInt(width.W)));
	val out = Output(UInt(width.W));
  });
// var a = 0;
 val OutReg = RegInit(0.U(width.W));

   OutReg := (io.MA(0) * io.MB(0)) + (io.MA(1) * io.MB(1)) + (io.MA(2) * io.MB(2)) + (io.MA(3) * io.MB(3));

//  while( a <= numInputs){
//	OutReg := OutReg + (io.MA(a) * io.MB(a));
//	OutReg := OutReg + io.MA(a);//errors out for some reason
//	OutReg := OutReg + a.U;		
//	io.MB(a) := a;
//	a = a + 1;
//  }

  io.out := OutReg;
}



object MultiplierOBJ extends App {
  (new chisel3.stage.ChiselStage).emitVerilog(new MulSum(64)(4))
}
