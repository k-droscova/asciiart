package Core.Models.Pixel
import Core.Errors.{BaseError, GeneralErrorCodes, LogSeverity, LogContext}

class RGBPixel(val red: Int, val green: Int, val blue: Int) extends Pixel {
  if (red < 0 || red > 255) {
    throw BaseError(
      message = s"Red component $red is out of bounds (0-255)",
      severity = LogSeverity.Error,
      context = LogContext.System,
      errorCode = GeneralErrorCodes.InvalidArgument
    )
  }
  if (green < 0 || green > 255) {
    throw BaseError(
      message = s"Green component $green is out of bounds (0-255)",
      severity = LogSeverity.Error,
      context = LogContext.System,
      errorCode = GeneralErrorCodes.InvalidArgument
    )
  }
  if (blue < 0 || blue > 255) {
    throw BaseError(
      message = s"Blue component $blue is out of bounds (0-255)",
      severity = LogSeverity.Error,
      context = LogContext.System,
      errorCode = GeneralErrorCodes.InvalidArgument
    )
  }
}