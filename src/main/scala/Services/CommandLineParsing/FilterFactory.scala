package Services.CommandLineParsing

import Services.Filters.{BrightnessFilter, Filter, InvertFilter, RotateFilter}
import Services.Logging.Errors.{BaseError, FilterErrorCodes}
import Services.Logging.{LogContext, LogSeverity, LogSource}
object FilterFactory extends CommandLineParser[List[Filter]] {

  override def parse(args: Map[String, String]): List[Filter] = {
    var filters: List[Filter] = List()

    if (containsRotation(args)) {
      val degrees = validateRotation(args("--rotate"), "--rotate")
      filters = filters :+ RotateFilter(degrees)
    }

    if (containsBrightness(args)) {
      val brightness = validateBrightness(args("--brightness"), "--brightness")
      filters = filters :+ BrightnessFilter(brightness)
    }

    if (containsInvert(args)) {
      filters = filters :+ InvertFilter()
    }

    filters
  }

  private def containsRotation(args: Map[String, String]): Boolean = args.contains("--rotate")

  private def containsBrightness(args: Map[String, String]): Boolean = args.contains("--brightness")

  private def containsInvert(args: Map[String, String]): Boolean = args.contains("--invert")

  private def validateRotation(value: String, argName: String)(implicit file: sourcecode.File, name: sourcecode.Name, line: sourcecode.Line): Int = {
    val degrees = validateInt(value, argName)
    if (degrees % 90 != 0) {
      throw BaseError(
        message = s"Invalid value for $argName: rotation must be a multiple of 90 degrees, but got $degrees.",
        context = LogContext.UI,
        errorCode = FilterErrorCodes.InvalidRotationAngle
      )
    }
    degrees
  }

  private def validateBrightness(value: String, argName: String)(implicit file: sourcecode.File, name: sourcecode.Name, line: sourcecode.Line): Int = {
    val brightness = validateInt(value, argName)
    if (brightness < -255 || brightness > 255) {
      throw BaseError(
        message = s"Invalid value for $argName: brightness must be between -255 and 255, but got $brightness.",
        context = LogContext.UI,
        errorCode = FilterErrorCodes.InvalidScaleFactor
      )
    }
    brightness
  }
}