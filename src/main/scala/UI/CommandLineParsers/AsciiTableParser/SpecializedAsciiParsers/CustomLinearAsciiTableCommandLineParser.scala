package UI.CommandLineParsers.AsciiTableParser.SpecializedAsciiParsers

import Core.Errors.{BaseError, GeneralErrorCodes, LogContext}
import Core.Models.AsciiTable.Linear.CustomLinearAsciiTable
import Services.ImageConvertors.AsciiConvertor.AsciiConvertor

/**
 * Parses command line arguments to create a custom ASCII table converter.
 *
 * The `CustomLinearAsciiTableCommandLineParser` detects and validates arguments specific
 * to the `--table=custom` option. This option allows users to define a custom ASCII table
 * using a provided set of characters.
 *
 * Argument Format:
 * - `--table=custom "<characters>"`:
 *   - `<characters>`: A string of characters used for the custom ASCII table.
 *
 * Validation:
 * - Only one `--table=custom` argument is allowed.
 * - Custom characters must be specified immediately after the `--table=custom` argument.
 *
 * Example:
 * ```
 * --table=custom ".:-=+*#%@"
 * ```
 *
 * Errors:
 * - Returns a `BaseError` if:
 *   - Multiple `--table=custom` arguments are detected.
 *   - Custom characters are missing or improperly specified.
 */
class CustomLinearAsciiTableCommandLineParser extends SpecializedAsciiTableCommandLineParser {
  override def parse(args: Array[String]): Either[BaseError, Option[AsciiConvertor]] = {
    // Find the indices of `--table=custom`
    val customIndices = args.zipWithIndex.collect { case (arg, index) if arg == "--table=custom" => index }

    customIndices.length match {
      case 0 =>
        // No `--table=custom` argument
        Right(None)
      case 1 =>
        // Exactly one `--table=custom` argument, validate the argument
        val customIndex = customIndices.head
        if (customIndex + 1 >= args.length || args(customIndex + 1).startsWith("--")) {
          Left(BaseError(
            message = "Custom characters must be specified after --table=custom.",
            context = LogContext.UI,
            errorCode = GeneralErrorCodes.InvalidArgument
          ))
        } else {
          // Valid argument follows, create the table
          val chars = args(customIndex + 1).trim
          Right(Some(new AsciiConvertor(new CustomLinearAsciiTable(chars))))
        }
      case _ =>
        // More than one `--table=custom` argument
        Left(BaseError(
          message = "Only one --table=custom argument is allowed.",
          context = LogContext.UI,
          errorCode = GeneralErrorCodes.InvalidArgument
        ))
    }
  }
}
