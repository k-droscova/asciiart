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
    val rotateArgs = args.sliding(2).filter(_.head == "--rotate").toList

    if (rotateArgs.isEmpty) {
      return Right(None) // No rotation arguments found
    }

    val filters = rotateArgs.map {
      case Array("--rotate", value) =>
        value.stripPrefix("+").toIntOption match {
          case Some(angle) =>
            new RotateFilter(angle)
          case _ =>
            throw BaseError(
              message = s"Invalid rotation argument, expected pattern: (+-)Num where Num is integer dividable by 90",
              context = LogContext.UI,
              errorCode = GeneralErrorCodes.InvalidArgument
            )
        }
      case _ =>
        throw BaseError(
          message = "Rotation value must be specified after --rotate.",
          context = LogContext.UI,
          errorCode = GeneralErrorCodes.InvalidArgument
        )
    }

    Right(Some(filters))
  }
}