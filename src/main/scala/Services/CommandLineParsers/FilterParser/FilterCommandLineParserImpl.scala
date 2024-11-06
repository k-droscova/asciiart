package Services.CommandLineParsers.FilterParser

import Core.Errors.{BaseError, GeneralErrorCodes, LogContext, LogSeverity}
import Services.Filters.*

/**
 * The `FilterCommandLineParserImpl` class is responsible for parsing command line arguments related to
 * image filtering options. It interprets the provided arguments to create a list of filters that can be
 * applied to ASCII images. This class implements the `FilterCommandLineParser` trait.
 *
 * It supports the following command line arguments:
 * - `--rotate degrees`: Specifies the rotation angle for the ASCII image. The "degrees" can be a positive
 *   or negative integer and should be divisible by 90.
 * - `--invert`: Indicates that the invert filter should be applied to the ASCII image.
 * - `--brightness value`: Specifies the brightness adjustment value for the ASCII image. The "value" can
 *   be a positive or negative integer.
 *
 * The class ensures that:
 * - The rotation angle must be specified immediately following the `--rotate` argument.
 * - The rotation angle must be a valid integer that can be parsed.
 * - The brightness adjustment value must be specified immediately following the `--brightness` argument.
 * - The brightness adjustment value must be a valid integer that can be parsed.
 * - The `--invert` argument does not require any additional parameters and can be specified on its own.
 *
 * If the command line input is invalid or if conflicting options are provided, a `BaseError` is thrown,
 * which includes a message detailing the nature of the error.
 */
class FilterCommandLineParserImpl extends FilterCommandLineParser {
  /**
   * Parses the input string containing command line arguments related to image filtering
   * and returns a list of corresponding `Filter` instances.
   *
   * @param input A string representing the command line arguments. The input can include
   *              various filter options such as `--rotate`, `--invert`, and `--brightness`.
   *              Example input: "--rotate +90 --invert --brightness -5"
   * @return A list of `Filter` objects based on the parsed arguments. The list will contain instances
   *         of `RotateFilter`, `InvertFilter`, and `BrightnessFilter`, reflecting the specified options.
   * @throws BaseError if the input is invalid, if required arguments are missing, or if conflicting
   *                   options are provided. Specific cases include:
   *                   - Missing angle for `--rotate`
   *                   - Missing brightness value for `--brightness`
   *                   - Invalid formats for angle or brightness values
   *
   * The method processes the input string by first splitting it into arguments, and then it
   *                   delegates the parsing to the `parseArguments` method, which handles the logic for
   *                   validating and creating the filter instances.
   */
  override def parse(args: Array[String]): List[Filter] = {
    parseArguments(args)
  }

  private def parseArguments(args: Array[String]): List[Filter] = {
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
