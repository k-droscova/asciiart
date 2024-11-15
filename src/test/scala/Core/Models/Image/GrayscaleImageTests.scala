package Core.Models.Image

import Core.Errors.{BaseError, GeneralErrorCodes}
import Core.Models.Image.GrayscaleImage
import Core.Models.Pixel.GrayscalePixel
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite
import sourcecode.*

import scala.compiletime.uninitialized

class GrayscaleImageTests extends AnyFunSuite with BeforeAndAfterEach {

  var grayscaleImage: GrayscaleImage = uninitialized

  override def beforeEach(): Unit = {
    super.beforeEach()
    // Set up a common 2x2 GrayscaleImage before each test
    grayscaleImage = new GrayscaleImage(Vector(
      Vector(new GrayscalePixel(100), new GrayscalePixel(150)),
      Vector(new GrayscalePixel(200), new GrayscalePixel(250))
    ))
  }

  override def afterEach(): Unit = {
    super.afterEach()
    grayscaleImage = null
  }

  test("getPixel should return the correct pixel at given coordinates") {
    assert(grayscaleImage.getPixel(0, 0).intensity == 100)
    assert(grayscaleImage.getPixel(1, 0).intensity == 150)
    assert(grayscaleImage.getPixel(0, 1).intensity == 200)
    assert(grayscaleImage.getPixel(1, 1).intensity == 250)
  }

  test("getWidth should return the correct width of the image") {
    assert(grayscaleImage.getWidth == 2)
  }

  test("getHeight should return the correct height of the image") {
    assert(grayscaleImage.getHeight == 2)
  }

  test("getPixel should throw BaseError for negative x coordinate") {
    val thrown = intercept[BaseError] {
      grayscaleImage.getPixel(-1, 0)
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("out of bounds"))
  }

  test("getPixel should throw BaseError for negative y coordinate") {
    val thrown = intercept[BaseError] {
      grayscaleImage.getPixel(0, -1)
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("out of bounds"))
  }

  test("getPixel should throw BaseError for x coordinate beyond width") {
    val thrown = intercept[BaseError] {
      grayscaleImage.getPixel(2, 0)
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("out of bounds"))
  }

  test("getPixel should throw BaseError for y coordinate beyond height") {
    val thrown = intercept[BaseError] {
      grayscaleImage.getPixel(0, 2)
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("out of bounds"))
  }
}
