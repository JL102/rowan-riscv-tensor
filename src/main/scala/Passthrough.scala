import chisel3._


// Chisel Code, but pass in a parameter to set widths of ports
class CumSum(width: Int) extends Module { 
  val io = IO(new Bundle {
	val MA_1 = Input(UInt(width.W));
	val MA_2 = Input(UInt(width.W));
	val MA_3 = Input(UInt(width.W));

	val MB_1 = Input(UInt(width.W));
	val MB_2 = Input(UInt(width.W));
	val MB_3 = Input(UInt(width.W));

	
	val out = Output(UInt(width.W));
  });
  
 val OutReg = RegInit(0.U(width.W));
  
  OutReg := (io.MA_1 * io.MB_1) + (io.MA_2 * io.MB_2) + (io.MA_3 * io.MB_3);
  io.out := OutReg;
}



object MultiplierOBJ extends App {
  (new chisel3.stage.ChiselStage).emitVerilog(new PassthroughGenerator(64))
}
