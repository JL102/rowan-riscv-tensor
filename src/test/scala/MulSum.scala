import chisel3._
import chisel3.iotesters.PeekPokeTester
import org.scalatest._

class MulSumTestA extends FlatSpec with Matchers {
    
    "MulSum" should "pass" in {
        chisel3.iotesters.Driver(() => new MulSum(64)(3)) {c => 
            new PeekPokeTester(c) {
		println("------------------------");
                poke(c.io.MA(0), 8.S);
		poke(c.io.MA(1), 8.S);
		poke(c.io.MA(2), 8.S);
		
		println("MA is " + peek(c.io.MA).toString);

		poke(c.io.MB(0), 6.S);
		poke(c.io.MB(1), 6.S);
		poke(c.io.MB(2), 6.S);

		println("MB is " + peek(c.io.MB).toString);
		println("Step now");
		step(1);
		println("Stepped");
                expect(c.io.out, 144.S);
		println(peek(c.io.out).toString);
//		println("\nInputS" + peek(c.io).toString);
//                println("\nHello, MA(0)" + peek(c.io.out).toString + ", out=" + peek(c.io.out).toString);
            }
        } should be (true)
    }
}

// class HelloTest extends FlatSpec with Matchers {

//   "Hello" should "pass" in {
//     chisel3.iotesters.Driver(() => new Hello()) { c =>
//       new PeekPokeTester(c) {

//         var ledStatus = -1
//         println("Start the blinking LED")
//         for (i <- 0 until 100) {
//           step(10000)
//           val ledNow = peek(c.io.led).toInt
//           val s = if (ledNow == 0) "o" else "*"
//           if (ledStatus != ledNow) {
//             System.out.println(s)
//             ledStatus = ledNow
//           }
//         }
//         println("\nEnd the blinking LED")
//       }
//     } should be (true)
//   }

// }
