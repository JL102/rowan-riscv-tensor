import chisel3._


// Chisel Code, but pass in a parameter to set widths of ports
class PassthroughGenerator(width: Int) extends Module { 
  val io = IO(new Bundle {
    val in = Input(UInt(width.W));
    val out = Output(UInt(width.W));
    val meme = Output(UInt(width.W));
  });
  
  val memeReg = RegInit(420.U(width.W));
  
  io.out := io.in + 1.U;
  memeReg := io.in + 420.U;
  io.meme := memeReg;
}



object ICanNameThisLiterallyAnything extends App {
  (new chisel3.stage.ChiselStage).emitVerilog(new PassthroughGenerator(69))
}