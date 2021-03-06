import chisel3._
import chisel3.iotesters.PeekPokeTester
import org.scalatest._

class MatMulTestA extends FlatSpec with Matchers {

    "MatMul" should "pass" in {
        chisel3.iotesters.Driver(() => new MatMul(2)) {c =>
            new PeekPokeTester(c) {
                println("------------------------");
		
		//placing items in Matrix A
                poke(c.io.InputMatrixA(0)(0), 8.S);
		poke(c.io.InputMatrixA(0)(1), 8.S);
		poke(c.io.InputMatrixA(1)(0), 8.S);
		poke(c.io.InputMatrixA(1)(1), 8.S);

		//placing items in Matrix B
		poke(c.io.InputMatrixB(0)(0), 8.S);
                poke(c.io.InputMatrixB(0)(1), 8.S);
                poke(c.io.InputMatrixB(1)(0), 8.S);
                poke(c.io.InputMatrixB(1)(1), 8.S);

                println("Input Matrix A is " + peek(c.io.InputMatrixA).toString);

                step(1);
                println("MatMul out is" + peek(c.io.out).toString);
		
		expect(c.io.InputMatrixA(0)(0), 8.S);

            }
        } should be (true)
    }
}
