package Core.Models.Pixel
import Core.Errors.{BaseError, GeneralErrorCodes, LogContext}

/**
 * Represents a pixel in a grayscale image, holding an intensity value between 0 and 255.
 * Validates that the intensity is within the allowable range.
 *
 * @param intensity The grayscale intensity of the pixel, where 0 is black and 255 is white.
 * @throws BaseError if the intensity is outside the range [0, 255].
 */
class GrayscalePixel(val intensity: Int) extends Pixel {
  if (intensity < 0 || intensity > 255) {
    throw BaseError(message = s"Intensity $intensity is out of bounds (0-255)", context = LogContext.System, errorCode = GeneralErrorCodes.InvalidArgument)
  }
}