package Core.Models.Pixel

import org.scalatest.funsuite.AnyFunSuite
import Core.Errors.{BaseError, GeneralErrorCodes}

class GrayscalePixelTests extends AnyFunSuite {

  test("GrayscalePixel should construct successfully with valid intensity values") {
    assert(new GrayscalePixel(0).intensity == 0)    // Minimum valid value
    assert(new GrayscalePixel(128).intensity == 128) // Mid-range valid value
    assert(new GrayscalePixel(255).intensity == 255) // Maximum valid value
  }

  test("GrayscalePixel should throw BaseError for intensity below 0") {
    val thrown = intercept[BaseError] {
      new GrayscalePixel(-1)
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Intensity -1 is out of bounds (0-255)"))
  }

  test("GrayscalePixel should throw BaseError for intensity above 255") {
    val thrown = intercept[BaseError] {
      new GrayscalePixel(256)
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Intensity 256 is out of bounds (0-255)"))
  }
}
