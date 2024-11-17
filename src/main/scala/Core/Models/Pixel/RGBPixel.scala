package Core.Models.Pixel
import Core.Errors.{BaseError, GeneralErrorCodes, LogContext}

/**
 * Represents a pixel in an RGB image, holding red, green, and blue color components.
 * Validates that each component is within the allowable range of 0 to 255.
 *
 * @param red   The red component of the pixel (0-255).
 * @param green The green component of the pixel (0-255).
 * @param blue  The blue component of the pixel (0-255).
 * @throws BaseError if any of the components are outside the range [0, 255].
 */
class RGBPixel(val red: Int, val green: Int, val blue: Int) extends Pixel {
  if (red < 0 || red > 255) {
    throw BaseError(message = s"Red component $red is out of bounds (0-255)", context = LogContext.System, errorCode = GeneralErrorCodes.InvalidArgument)
  }
  if (green < 0 || green > 255) {
    throw BaseError(message = s"Green component $green is out of bounds (0-255)", context = LogContext.System, errorCode = GeneralErrorCodes.InvalidArgument)
  }
  if (blue < 0 || blue > 255) {
    throw BaseError(message = s"Blue component $blue is out of bounds (0-255)", context = LogContext.System, errorCode = GeneralErrorCodes.InvalidArgument)
  }
}