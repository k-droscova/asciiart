package Core.Models.Pixel
import Core.Errors.{BaseError, GeneralErrorCodes, LogSeverity, LogContext}

class GrayscalePixel(val intensity: Int) extends Pixel {
  if (intensity < 0 || intensity > 255) {
    throw BaseError(
      message = s"Intensity $intensity is out of bounds (0-255)",
      severity = LogSeverity.Error,
      context = LogContext.System,
      errorCode = GeneralErrorCodes.InvalidArgument
    )
  }
}