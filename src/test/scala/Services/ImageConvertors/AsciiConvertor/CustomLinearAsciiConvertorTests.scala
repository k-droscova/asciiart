package Services.ImageConvertors.AsciiConvertor

import Core.Errors.{ASCIIConversionErrorCodes, BaseError}
import Core.Models.Image.GrayscaleImage
import Core.Models.Pixel.GrayscalePixel
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.BeforeAndAfterEach

import scala.compiletime.uninitialized
class CustomLinearAsciiConvertorTests extends AnyFunSuite with BeforeAndAfterEach {
  var customConvertor: CustomLinearAsciiConvertor = uninitialized
  var grayscaleImage: GrayscaleImage = uninitialized

  override def beforeEach(): Unit = {
    super.beforeEach()
    customConvertor = new CustomLinearAsciiConvertor("abcdefghijklmnopqrstuvwxyz")
    grayscaleImage = new GrayscaleImage(Vector(
      Vector(
        new GrayscalePixel(0),    // Expected 'a'
        new GrayscalePixel(10),   // Expected 'b'
        new GrayscalePixel(20),   // Expected 'c'
        new GrayscalePixel(30),   // Expected 'd'
        new GrayscalePixel(40),   // Expected 'e'
        new GrayscalePixel(100),  // Expected 'k'
        new GrayscalePixel(150),  // Expected 'p'
        new GrayscalePixel(200),  // Expected 'u'
        new GrayscalePixel(250)   // Expected 'z'
      )
    ))
  }

  override def afterEach(): Unit = {
    super.afterEach()
    customConvertor = null
    grayscaleImage = null
  }

  test("CustomLinearAsciiConvertor should correctly convert grayscale values to ASCII characters") {
    val asciiImage = customConvertor.convert(grayscaleImage)

    val expectedChars = Vector('a', 'b', 'c', 'd', 'e', 'k', 'p', 'u', 'z')
    for (x <- expectedChars.indices) {
      assert(asciiImage.getPixel(x, 0).char == expectedChars(x),
        s"Expected ${expectedChars(x)} but got ${asciiImage.getPixel(x, 0).char} at position ($x, 0)")
    }
  }

  test("CustomLinearAsciiConvertor should throw an error if characters are empty") {
    val thrown = intercept[BaseError] {
      new CustomLinearAsciiConvertor("")
    }
    assert(thrown.errorCode == ASCIIConversionErrorCodes.InvalidTable)
  }

  test("CustomLinearAsciiConvertor should preserve dimensions of the original image") {
    val asciiImage = customConvertor.convert(grayscaleImage)
    assert(asciiImage.getWidth == grayscaleImage.getWidth)
    assert(asciiImage.getHeight == grayscaleImage.getHeight)
  }

  test("CustomLinearAsciiConvertor should handle empty grayscale image") {
    val emptyGrayscaleImage = new GrayscaleImage(Vector.empty)
    val asciiImage = customConvertor.convert(emptyGrayscaleImage)

    assert(asciiImage.getWidth == 0)
    assert(asciiImage.getHeight == 0)
  }
}