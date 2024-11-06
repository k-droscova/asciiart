package Services.CommandLineParsers.FilterParser

import Core.Errors.{BaseError, GeneralErrorCodes, LogContext, LogSeverity}
import Services.Filters.*

class FilterCommandLineParserImpl extends FilterCommandLineParser {
  override def parse(input: String): List[Filter] = {
    val args = input.split(" ").toList
    parseArguments(args)
  }

  private def parseArguments(args: List[String]): List[Filter] = {
    var filters: List[Filter] = List()
    val numberPattern = "^[+-]?\\d+$".r

    for (i <- args.indices) {
      args(i) match {
        case "--rotate" =>
          if (i + 1 < args.length) {
            val potentialAngle = args(i+1)
            if (numberPattern.matches(potentialAngle)) {
              val angleStr = potentialAngle.stripPrefix("+")
              val angle = angleStr.toIntOption.getOrElse(throw createBaseError("Invalid rotation argument, expected pattern: (+-)Num where Num is integer dividable by 90"))
              filters = RotateFilter(angle) :: filters
            } else {
              throw createBaseError("Invalid rotation argument, expected pattern: (+-)Num where Num is integer dividable by 90")
            }
          } else {
            throw createBaseError("Rotation value must be specified after --rotate.")
          }

        case "--invert" =>
          filters = InvertFilter() :: filters

        case "--brightness" =>
          if (i + 1 < args.length) {
            val potentialBrightness = args(i+1)
            if (numberPattern.matches(potentialBrightness)) {
              val brightnessStr = potentialBrightness.stripPrefix("+")
              val brightnessValue = brightnessStr.toIntOption.getOrElse(throw createBaseError("Invalid brightness argument, expected pattern: (+-)Num where Num is integer"))
              filters = BrightnessFilter(brightnessValue) :: filters
            } else {
              throw createBaseError("Invalid brightness argument, expected pattern: (+-)Num where Num is integer")
            }
          } else {
            throw createBaseError("Brightness value must be specified after --brightness.")
          }

        case _ =>
      }
    }
    filters.reverse
  }

  private def createBaseError(message: String): BaseError = {
    BaseError(
      message = message,
      severity = LogSeverity.Error,
      context = LogContext.UI,
      errorCode = GeneralErrorCodes.InvalidArgument
    )
  }
}
