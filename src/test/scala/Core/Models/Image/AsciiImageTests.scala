package Core.Models.Image

import Core.Errors.{BaseError, GeneralErrorCodes}
import Core.Models.Pixel.AsciiPixel
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite

import scala.compiletime.uninitialized

class AsciiImageTests extends AnyFunSuite with BeforeAndAfterEach {

  var asciiImage: AsciiImage = uninitialized

  override def beforeEach(): Unit = {
    super.beforeEach()
    // Set up a common 2x2 AsciiImage before each test
    asciiImage = new AsciiImage(Vector(
      Vector(new AsciiPixel('A'), new AsciiPixel('B')),
      Vector(new AsciiPixel('C'), new AsciiPixel('D'))
    ))
  }

  override def afterEach(): Unit = {
    super.afterEach()
    asciiImage = null
  }

  test("getPixel should return the correct pixel at given coordinates") {
    assert(asciiImage.getPixel(0, 0).char == 'A')
    assert(asciiImage.getPixel(1, 0).char == 'B')
    assert(asciiImage.getPixel(0, 1).char == 'C')
    assert(asciiImage.getPixel(1, 1).char == 'D')
  }

  test("getWidth should return the correct width of the image") {
    assert(asciiImage.getWidth == 2)
  }

  test("getHeight should return the correct height of the image") {
    assert(asciiImage.getHeight == 2)
  }

  test("getPixel should throw BaseError for negative x coordinate") {
    val thrown = intercept[BaseError] {
      asciiImage.getPixel(-1, 0)
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("out of bounds"))
  }

  test("getPixel should throw BaseError for negative y coordinate") {
    val thrown = intercept[BaseError] {
      asciiImage.getPixel(0, -1)
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("out of bounds"))
  }

  test("getPixel should throw BaseError for x coordinate beyond width") {
    val thrown = intercept[BaseError] {
      asciiImage.getPixel(2, 0)
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("out of bounds"))
  }

  test("getPixel should throw BaseError for y coordinate beyond height") {
    val thrown = intercept[BaseError] {
      asciiImage.getPixel(0, 2)
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("out of bounds"))
  }
}