import chisel3._
import chisel3.iotesters.PeekPokeTester
import org.scalatest._

class MatMulTestA extends FlatSpec with Matchers {

    "MatMul" should "pass" in {
        chisel3.iotesters.Driver(() => new MatMul(64)) {c =>
            new PeekPokeTester(c) {
                println("------------------------");
                poke(c.io.InputMatrixA(0)(0), 8.S);
                println("Input Matrix A is " + peek(c.io.InputMatrixA).toString);

                //poke(c.io.MB(0), 6.S);
                //poke(c.io.MB(1), 6.S);
                
		//println("MB is " + peek(c.io.MB).toString);
                //println("Step now");

                step(1);
                println("Stepped");
                println(peek(c.io.out).toString);
//              println("\nInputS" + peek(c.io).toString);
//                println("\nHello, MA(0)" + peek(c.io.out).toString + ", out=" + peek(c.io.out).toString);
		
		expect(c.io.InputMatrixA(0)(0), 8.S);

            }
        } should be (true)
    }
}
