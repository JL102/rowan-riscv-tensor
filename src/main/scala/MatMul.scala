import chisel3._


// Chisel Code, but pass in a parameter to set widths of ports
class MatMul(size: Int) extends Module { 
   val io = IO(new Bundle {
      val InputMatrixA = Input(Vec(size,Vec(size,UInt(64.W))));
      val InputMatrixB = Input(Vec(size,Vec(size,UInt(64.W)))); 

      val out = Output(Vec(size,Vec(size,UInt(64.W))));
	  });

	for( var col <- size ){//we need to go accross each column in the output
		for( var row <- size ){//we need to go down row by row in the output

		}
		
	}





	io.out := io.InputMatrixA;

  



}



object FullMatrix extends App {
  (new chisel3.stage.ChiselStage).emitVerilog(new MatMul(2))
}
