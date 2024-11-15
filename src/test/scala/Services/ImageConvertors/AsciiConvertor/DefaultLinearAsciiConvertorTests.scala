package Services.ImageConvertors.AsciiConvertor

import Core.Models.Image.GrayscaleImage
import Core.Models.Pixel.GrayscalePixel
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite

import scala.compiletime.uninitialized

class DefaultLinearAsciiConvertorTests extends AnyFunSuite with BeforeAndAfterEach {

  var grayscaleImage: GrayscaleImage = uninitialized
  var defaultConvertor: DefaultLinearAsciiConvertor = uninitialized

  override def beforeEach(): Unit = {
    super.beforeEach()
    grayscaleImage = new GrayscaleImage(Vector(
      Vector(
        new GrayscalePixel(0), // Expected ' '
        new GrayscalePixel(26), // Expected '.'
        new GrayscalePixel(78), // Expected '-'
        new GrayscalePixel(104), // Expected '='
        new GrayscalePixel(130), // Expected '+'
        new GrayscalePixel(234) // Expected '@'
      )
    ))
    defaultConvertor = new DefaultLinearAsciiConvertor()
  }

  override def afterEach(): Unit = {
    super.afterEach()
    grayscaleImage = null
    defaultConvertor = null
  }

  test("DefaultLinearAsciiConvertor should preserve image dimensions") {
    val asciiImage = defaultConvertor.convert(grayscaleImage)
    assert(asciiImage.getWidth == grayscaleImage.getWidth)
    assert(asciiImage.getHeight == grayscaleImage.getHeight)
  }

  test("DefaultLinearAsciiConvertor should correctly convert grayscale values to ASCII characters") {
    val asciiImage = defaultConvertor.convert(grayscaleImage)

    val expectedChars = Vector(' ', '.', '-', '=', '+', '@')
    for (x <- expectedChars.indices) {
      assert(asciiImage.getPixel(x, 0).char == expectedChars(x), s"Expected ${expectedChars(x)} but got ${asciiImage.getPixel(x, 0).char} at position ($x, 0)")
    }
  }

  test("DefaultLinearAsciiConvertor should handle an empty grayscale image") {
    val emptyImage = new GrayscaleImage(Vector.empty)
    val asciiImage = defaultConvertor.convert(emptyImage)
    assert(asciiImage.getWidth == 0)
    assert(asciiImage.getHeight == 0)
  }
}
