package UI.CommandLineParsers.FilterParser.SpecializedFilterParsers

import Core.Errors.{BaseError, GeneralErrorCodes, LogContext}
import Services.Filters.BrightnessFilter

/**
 * Parses the `--brightness` argument to create a `BrightnessFilter`.
 *
 * The `--brightness` argument must be followed by an integer, representing
 * the brightness adjustment value. Positive values increase brightness,
 * and negative values decrease it.
 *
 * Argument Format:
 * - `--brightness <value>`: Specifies the brightness adjustment value.
 *
 * Validation:
 * - The `<value>` must be a valid integer.
 *
 * Example:
 * ```
 * --brightness 50
 * --brightness -30
 * ```
 *
 * Errors:
 * - Missing or invalid `<value>` value.
 */
class BrightnessFilterCommandLineParser extends SpecializedFilterCommandLineParser[BrightnessFilter] {
  override def parse(args: Array[String]): Either[BaseError, Option[List[BrightnessFilter]]] = {
    // Collect indices of `--brightness`
    val brightnessIndices = args.zipWithIndex.collect {
      case ("--brightness", index) => index
    }

    if (brightnessIndices.isEmpty) {
      return Right(None) // No brightness arguments found
    }

    val filters = brightnessIndices.map { index =>
      // Ensure there is a value after `--brightness`
      if (index + 1 >= args.length || args(index + 1).startsWith("--")) {
        throw BaseError(
          message = "Brightness value must be specified after --brightness.",
          context = LogContext.UI,
          errorCode = GeneralErrorCodes.InvalidArgument
        )
      }

      // Validate and parse the brightness value
      val value = args(index + 1)
      value.stripPrefix("+").toIntOption match {
        case Some(b) => new BrightnessFilter(b)
        case None =>
          throw BaseError(
            message = "Invalid brightness argument, expected pattern: (+-)Num where Num is integer.",
            context = LogContext.UI,
            errorCode = GeneralErrorCodes.InvalidArgument
          )
      }
    }

    Right(Some(filters.toList))
  }
}