package Core.Models.Pixel

import org.scalatest.funsuite.AnyFunSuite
import Core.Errors.{BaseError, GeneralErrorCodes}

class RGBPixelTests extends AnyFunSuite {

  test("RGBPixel should construct successfully with valid color component values") {
    val pixel = new RGBPixel(0, 128, 255)
    assert(pixel.red == 0)
    assert(pixel.green == 128)
    assert(pixel.blue == 255)
  }

  test("RGBPixel should throw BaseError for red component below 0") {
    val thrown = intercept[BaseError] {
      new RGBPixel(-1, 128, 128)
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Red component -1 is out of bounds (0-255)"))
  }

  test("RGBPixel should throw BaseError for red component above 255") {
    val thrown = intercept[BaseError] {
      new RGBPixel(256, 128, 128)
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Red component 256 is out of bounds (0-255)"))
  }

  test("RGBPixel should throw BaseError for green component below 0") {
    val thrown = intercept[BaseError] {
      new RGBPixel(128, -1, 128)
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Green component -1 is out of bounds (0-255)"))
  }

  test("RGBPixel should throw BaseError for green component above 255") {
    val thrown = intercept[BaseError] {
      new RGBPixel(128, 256, 128)
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Green component 256 is out of bounds (0-255)"))
  }

  test("RGBPixel should throw BaseError for blue component below 0") {
    val thrown = intercept[BaseError] {
      new RGBPixel(128, 128, -1)
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Blue component -1 is out of bounds (0-255)"))
  }

  test("RGBPixel should throw BaseError for blue component above 255") {
    val thrown = intercept[BaseError] {
      new RGBPixel(128, 128, 256)
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Blue component 256 is out of bounds (0-255)"))
  }
}