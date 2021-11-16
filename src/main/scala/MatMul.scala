import chisel3._


// Chisel Code, but pass in a parameter to set widths of ports
class MatMul(width: Int) extends Module { 
   val io = IO(new Bundle {
      val InputMatrixA = Input(Vec(width,Vec(width,UInt(width.W))));
      val InputMatrixB = Input(Vec(width,Vec(width,UInt(width.W)))); 

      val out = Output(Vec(2,Vec(2,UInt(width.W))));
	  });

	





	io.out := io.InputMatrixA;

  



}



object FullMatrix extends App {
  (new chisel3.stage.ChiselStage).emitVerilog(new MatMul(2))
}
