package Core.Models.Image

import Core.Errors.{BaseError, GeneralErrorCodes}
import Core.Models.Pixel.RGBPixel
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite
import sourcecode.*

import scala.compiletime.uninitialized

class RGBImageTests extends AnyFunSuite with BeforeAndAfterEach {

  var rgbImage: RGBImage = uninitialized

  override def beforeEach(): Unit = {
    super.beforeEach()
    // Set up a 3x2 RGBImage before each test
    rgbImage = new RGBImage(Vector(
      Vector(new RGBPixel(100, 150, 200), new RGBPixel(120, 160, 220)),
      Vector(new RGBPixel(130, 170, 230), new RGBPixel(140, 180, 240)),
      Vector(new RGBPixel(110, 160, 210), new RGBPixel(125, 175, 225))
    ))
  }

  override def afterEach(): Unit = {
    super.afterEach()
    rgbImage = null
  }

  test("getPixel should return the correct pixel at given coordinates") {
    val pixel00 = rgbImage.getPixel(0, 0)
    assert(pixel00.red == 100)
    assert(pixel00.green == 150)
    assert(pixel00.blue == 200)

    val pixel10 = rgbImage.getPixel(1, 0)
    assert(pixel10.red == 120)
    assert(pixel10.green == 160)
    assert(pixel10.blue == 220)

    val pixel01 = rgbImage.getPixel(0, 1)
    assert(pixel01.red == 130)
    assert(pixel01.green == 170)
    assert(pixel01.blue == 230)

    val pixel11 = rgbImage.getPixel(1, 1)
    assert(pixel11.red == 140)
    assert(pixel11.green == 180)
    assert(pixel11.blue == 240)

    val pixel02 = rgbImage.getPixel(0, 2)
    assert(pixel02.red == 110)
    assert(pixel02.green == 160)
    assert(pixel02.blue == 210)

    val pixel12 = rgbImage.getPixel(1, 2)
    assert(pixel12.red == 125)
    assert(pixel12.green == 175)
    assert(pixel12.blue == 225)
  }

  test("getWidth should return the correct width of the image") {
    assert(rgbImage.getWidth == 2)
  }

  test("getHeight should return the correct height of the image") {
    assert(rgbImage.getHeight == 3)
  }

  test("getPixel should throw BaseError for negative x coordinate") {
    val thrown = intercept[BaseError] {
      rgbImage.getPixel(-1, 0)
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("out of bounds"))
  }

  test("getPixel should throw BaseError for negative y coordinate") {
    val thrown = intercept[BaseError] {
      rgbImage.getPixel(0, -1)
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("out of bounds"))
  }

  test("getPixel should throw BaseError for x coordinate beyond width") {
    val thrown = intercept[BaseError] {
      rgbImage.getPixel(2, 0)
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("out of bounds"))
  }

  test("getPixel should throw BaseError for y coordinate beyond height") {
    val thrown = intercept[BaseError] {
      rgbImage.getPixel(0, 3)
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("out of bounds"))
  }
}