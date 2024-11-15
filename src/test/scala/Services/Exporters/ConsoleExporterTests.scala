package Services.Exporters

import Core.Models.Image.AsciiImage
import Core.Models.Pixel.AsciiPixel
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite

import java.io.{ByteArrayOutputStream, PrintStream}
import scala.compiletime.uninitialized
import scala.util.Using

class ConsoleExporterTests extends AnyFunSuite with BeforeAndAfterEach {
  private var exporter: ConsoleExporter = uninitialized
  private var output: ByteArrayOutputStream = uninitialized
  private var originalOut: PrintStream = uninitialized
  override def beforeEach(): Unit = {
    super.beforeEach()
    exporter = new ConsoleExporter()
    output = new ByteArrayOutputStream()
    originalOut = new PrintStream(output)
  }

  override def afterEach(): Unit = {
    super.afterEach()
    exporter = null
    originalOut.flush()
    output.reset()
    originalOut = null
    output = null
  }

  test("ConsoleExporter should print the ASCII image to the console") {
    val image = new AsciiImage(Vector(
      Vector(AsciiPixel('A'), AsciiPixel('B'), AsciiPixel('C')),
      Vector(AsciiPixel('D'), AsciiPixel('E'), AsciiPixel('F'))
    ))

    exporter.exportImage(image)
    Console.withOut(originalOut) {
      exporter.exportImage(image)
    }
    val expectedOutput = "ABC\nDEF\n"
    assert(output.toString() == expectedOutput, "The console output should match the expected ASCII image.")
  }

  test("ConsoleExporter should produce no output for an empty ASCII image") {
    val emptyImage = new AsciiImage(Vector.empty)

    Console.withOut(originalOut) {
      exporter.exportImage(emptyImage)
    }
    assert(output.toString().isEmpty, "The console output should be empty for an empty ASCII image.")
  }
}
