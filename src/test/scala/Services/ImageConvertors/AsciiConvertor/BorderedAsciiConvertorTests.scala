package Services.ImageConvertors.AsciiConvertor

import Core.Errors.{ASCIIConversionErrorCodes, BaseError}
import Core.Models.Image.GrayscaleImage
import Core.Models.Pixel.GrayscalePixel
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite

import scala.compiletime.uninitialized

class BorderedAsciiConvertorTests extends AnyFunSuite with BeforeAndAfterEach {
  var borderedConvertor: BorderedAsciiConvertor = uninitialized
  var grayscaleImage: GrayscaleImage = uninitialized

  override def beforeEach(): Unit = {
    super.beforeEach()
    borderedConvertor = new BorderedAsciiConvertor(" .:-=+*#%", List(32, 64, 96, 128, 160, 192, 224, 255))
    grayscaleImage = new GrayscaleImage(Vector(
      Vector(
        new GrayscalePixel(0),    // Expected ' '
        new GrayscalePixel(31),   // Expected ' '
        new GrayscalePixel(32),   // Expected '.'
        new GrayscalePixel(63),   // Expected '.'
        new GrayscalePixel(64),   // Expected ':'
        new GrayscalePixel(95),   // Expected ':'
        new GrayscalePixel(96),   // Expected '-'
        new GrayscalePixel(127),  // Expected '-'
        new GrayscalePixel(128),  // Expected '='
        new GrayscalePixel(159),  // Expected '='
        new GrayscalePixel(160),  // Expected '+'
        new GrayscalePixel(191),  // Expected '+'
        new GrayscalePixel(192),  // Expected '*'
        new GrayscalePixel(223),  // Expected '*'
        new GrayscalePixel(224),  // Expected '#'
        new GrayscalePixel(254),  // Expected '#'
        new GrayscalePixel(255)   // Expected '%'
      )
    ))
  }

  override def afterEach(): Unit = {
    super.afterEach()
    borderedConvertor = null
    grayscaleImage = null
  }

  test("BorderedAsciiConvertor should correctly convert grayscale values to ASCII characters") {
    val asciiImage = borderedConvertor.convert(grayscaleImage)

    val expectedChars = Vector(' ', ' ', '.', '.', ':', ':', '-', '-', '=', '=', '+', '+', '*', '*', '#', '#', '%')
    for (x <- expectedChars.indices) {
      assert(asciiImage.getPixel(x, 0).char == expectedChars(x),
        s"Expected ${expectedChars(x)} but got ${asciiImage.getPixel(x, 0).char} at position ($x, 0)")
    }
  }

  test("BorderedAsciiConvertor should handle empty image") {
    val emptyGrayscaleImage = new GrayscaleImage(Vector.empty)
    val asciiImage = borderedConvertor.convert(emptyGrayscaleImage)

    assert(asciiImage.getWidth == 0)
    assert(asciiImage.getHeight == 0)
  }

  test("BorderedAsciiConvertor should preserve dimensions of the original image") {
    val asciiImage = borderedConvertor.convert(grayscaleImage)
    assert(asciiImage.getWidth == grayscaleImage.getWidth)
    assert(asciiImage.getHeight == grayscaleImage.getHeight)
  }

  test("BorderedAsciiConvertor should throw an error if the border list is empty") {
    val thrown = intercept[BaseError] {
      new BorderedAsciiConvertor(" .:-=", List())
    }
    assert(thrown.errorCode == ASCIIConversionErrorCodes.InvalidTable)
  }

  test("BorderedAsciiConvertor should throw an error if characters string is empty") {
    val thrown = intercept[BaseError] {
      new BorderedAsciiConvertor("", List(32, 64, 96, 128))
    }
    assert(thrown.errorCode == ASCIIConversionErrorCodes.InvalidTable)
  }

  test("BorderedAsciiConvertor should throw an error if the borders list is longer than characters length - 1") {
    val thrown = intercept[BaseError] {
      new BorderedAsciiConvertor(" .:-=", List(32, 64, 96, 128, 160))
    }
    assert(thrown.errorCode == ASCIIConversionErrorCodes.InvalidTable)
  }

  test("BorderedAsciiConvertor should throw an error if any border is out of the 0-255 range") {
    val thrown = intercept[BaseError] {
      new BorderedAsciiConvertor(" .:-", List(32, 300, 96))
    }
    assert(thrown.errorCode == ASCIIConversionErrorCodes.InvalidTable)
    assert(thrown.message.contains("all borders must be within the 0-255 range"))
  }

  test("BorderedAsciiConvertor should map grayscale values correctly regardless of border order") {
    val unorderedConvertor = new BorderedAsciiConvertor(" .:-=+*#%", List(192, 160, 64, 32, 128, 224, 96, 255))
    val asciiImage = borderedConvertor.convert(grayscaleImage)
    val unorderedAsciiImage = unorderedConvertor.convert(grayscaleImage)
    for (y <- 0 until grayscaleImage.getHeight) {
      for (x <- 0 until grayscaleImage.getWidth) {
        assert(asciiImage.getPixel(x, y).char == unorderedAsciiImage.getPixel(x, y).char,
          s"Expected ${asciiImage.getPixel(x, y).char} but got ${unorderedAsciiImage.getPixel(x, y).char} at position ($x, $y)")
      }
    }
  }
}
