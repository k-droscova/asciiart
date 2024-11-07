package Services.ImageConvertors.AsciiConvertor

import Core.Models.Image.GrayscaleImage
import Core.Models.Pixel.GrayscalePixel
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite

import scala.compiletime.uninitialized
class BourkeLinearAsciiConvertorTests extends AnyFunSuite with BeforeAndAfterEach {

  var grayscaleImage: GrayscaleImage = uninitialized
  var bourkeConvertor: BourkeLinearAsciiConvertor = uninitialized

  override def beforeEach(): Unit = {
    super.beforeEach()
    grayscaleImage = new GrayscaleImage(Vector(
      Vector(
        new GrayscalePixel(0), // Expected '$'
        new GrayscalePixel(12), // Expected '%'
        new GrayscalePixel(78), // Expected 'm'
        new GrayscalePixel(104), // Expected 'J'
        new GrayscalePixel(130), // Expected 'v'
        new GrayscalePixel(234) // Expected '!'
      )
    ))
    bourkeConvertor = new BourkeLinearAsciiConvertor()
  }

  override def afterEach(): Unit = {
    super.afterEach()
    grayscaleImage = null
    bourkeConvertor = null
  }

  test("BourkeLinearAsciiConvertor should preserve image dimensions") {
    val asciiImage = bourkeConvertor.convert(grayscaleImage)
    assert(asciiImage.getWidth == grayscaleImage.getWidth)
    assert(asciiImage.getHeight == grayscaleImage.getHeight)
  }

  test("BourkeLinearAsciiConvertor should correctly convert grayscale values to ASCII characters") {
    val asciiImage = bourkeConvertor.convert(grayscaleImage)

    val expectedChars = Vector('$', '%', 'm', 'J', 'v', '!')
    for (x <- expectedChars.indices) {
      assert(asciiImage.getPixel(x, 0).char == expectedChars(x), s"Expected ${expectedChars(x)} but got ${asciiImage.getPixel(x, 0).char} at position ($x, 0)")
    }
  }

  test("BourkeLinearAsciiConvertor should handle an empty grayscale image") {
    val emptyImage = new GrayscaleImage(Vector.empty)
    val asciiImage = bourkeConvertor.convert(emptyImage)
    assert(asciiImage.getWidth == 0)
    assert(asciiImage.getHeight == 0)
  }
}
