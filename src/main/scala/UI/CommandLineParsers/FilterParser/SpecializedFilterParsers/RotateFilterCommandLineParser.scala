package UI.CommandLineParsers.FilterParser.SpecializedFilterParsers

import Core.Errors.{BaseError, FilterErrorCodes, GeneralErrorCodes, LogContext}
import Services.Filters.RotateFilter

/**
 * Parses the `--rotate` argument to create a `RotateFilter`.
 *
 * The `--rotate` argument must be followed by an integer divisible by 90,
 * representing the rotation angle in degrees. Positive values rotate clockwise,
 * and negative values rotate counterclockwise.
 *
 * Argument Format:
 * - `--rotate <angle>`: Specifies the rotation angle.
 *
 * Validation:
 * - The `<angle>` must be a valid integer divisible by 90.
 *
 * Example:
 * ```
 * --rotate 90
 * --rotate -180
 * ```
 *
 * Errors:
 * - Missing or invalid `<angle>` value.
 */
class RotateFilterCommandLineParser extends SpecializedFilterCommandLineParser[RotateFilter] {
  override def parse(args: Array[String]): Either[BaseError, Option[List[RotateFilter]]] = {
    // Collect indices of `--rotate`
    val rotateIndices = args.zipWithIndex.collect {
      case ("--rotate", index) => index
    }

    if (rotateIndices.isEmpty) {
      return Right(None) // No rotation arguments found
    }

    val filters = rotateIndices.map { index =>
      // Ensure there is a value after `--rotate`
      if (index + 1 >= args.length || args(index + 1).startsWith("--")) {
        throw BaseError(
          message = "Rotation value must be specified after --rotate.",
          context = LogContext.UI,
          errorCode = GeneralErrorCodes.InvalidArgument
        )
      }

      // Validate and parse the rotation value
      val value = args(index + 1)
      value.stripPrefix("+").toIntOption match {
        case Some(angle) if angle % 90 == 0 => new RotateFilter(angle)
        case Some(angle) =>
          throw BaseError(
            message = s"Angle $angle is invalid. Angle must be a multiple of 90 degrees.",
            context = LogContext.UI,
            errorCode = FilterErrorCodes.InvalidRotationAngle
          )
        case None =>
          throw BaseError(
            message = s"Invalid rotation argument, expected pattern: (+-)Num where Num is integer dividable by 90.",
            context = LogContext.UI,
            errorCode = GeneralErrorCodes.InvalidArgument
          )
      }
    }

    Right(Some(filters.toList))
  }
}