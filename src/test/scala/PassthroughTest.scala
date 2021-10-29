import chisel3._
import chisel3.iotesters.PeekPokeTester
import org.scalatest._

class PassthroughMemeTest extends FlatSpec with Matchers {
    
    "PassthroughGenerator" should "pass" in {
        chisel3.iotesters.Driver(() => new PassthroughGenerator(69)) {c => 
            new PeekPokeTester(c) {
                poke(c.io.MA_1, 8.S);
		poke(c.io.MA_2, 8.S);
		poke(c.io.MA_3, 8.S);

		poke(c.io.MB_1, 6.S);
		poke(c.io.MB_2, 6.S);
		poke(c.io.MB_3, 6.S);

		step(1);
                expect(c.io.out, 144.S);
		println("\nInputS" + peek(c.io).toString);
                println("\nHello, meme=" + peek(c.io.out).toString + ", out=" + peek(c.io.out).toString);
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
