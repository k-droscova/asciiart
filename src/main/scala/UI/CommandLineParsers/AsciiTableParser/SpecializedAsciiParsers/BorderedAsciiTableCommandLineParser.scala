package UI.CommandLineParsers.AsciiTableParser.SpecializedAsciiParsers

import Core.Errors.{BaseError, GeneralErrorCodes, LogContext}
import Core.Models.AsciiTable.Nonlinear.BorderedAsciiTable
import Services.ImageConvertors.AsciiConvertor.AsciiConvertor

/**
 * Parses command line arguments to create a bordered ASCII table converter.
 *
 * The `BorderedAsciiTableCommandLineParser` detects and validates arguments specific
 * to the `--table=bordered` option. This option allows users to specify a bordered
 * ASCII table with custom characters and a list of borders.
 *
 * Argument Format:
 * - `--table=bordered "<characters>" "[<Int>,<Int>,...]"`:
 *   - `<characters>`: A string of characters used for the ASCII table.
 *   - `[<Int>,<Int>,...]`: A list of integer borders in square brackets.
 *
 * Validation:
 * - Only one `--table=bordered` argument is allowed.
 * - Custom characters must be specified immediately after the `--table=bordered` argument.
 * - The number of characters must be one greater than the number of borders.
 * - Borders must follow the characters and be specified in the format `[int,int,...]`.
 *
 * Example:
 * ```
 * --table=bordered ".:-=+" "[10,20,30,220]"
 * ```
 *
 * Errors:
 * - Returns a `BaseError` if:
 *   - Multiple `--table=bordered` arguments are detected.
 *   - Custom characters are missing or improperly specified.
 *   - Borders are missing, improperly formatted, or are not of desired length.
 */
class BorderedAsciiTableCommandLineParser extends SpecializedAsciiTableCommandLineParser {

  /**
   * Parses the command line arguments to detect and process the `--table=bordered` option.
   *
   * @param args The array of command line arguments.
   * @return Either a `BaseError` or an optional `AsciiConvertor`:
   *         - `Right(Some(AsciiConvertor))` if valid arguments are detected.
   *         - `Right(None)` if no relevant arguments are found.
   *         - `Left(BaseError)` if the arguments are invalid.
   */
  override def parse(args: Array[String]): Either[BaseError, Option[AsciiConvertor]] = {
    val borderedArgs = args.zipWithIndex.filter(_._1.startsWith("--table=bordered"))

    borderedArgs match {
      case Array() => Right(None) // No `--table=bordered` argument provided
      case Array((_, index)) =>
        parseBorderedTable(args, index)
      case _ =>
        Left(BaseError(
          message = "Only one --table=bordered argument is allowed.",
          context = LogContext.UI,
          errorCode = GeneralErrorCodes.InvalidArgument
        ))
    }
  }

  /**
   * Validates and processes the arguments following `--table=bordered`.
   *
   * @param args  The command line arguments.
   * @param index The index of the `--table=bordered` argument.
   * @return Either a `BaseError` or an optional `AsciiConvertor`:
   *         - `Right(Some(AsciiConvertor))` if arguments are valid.
   *         - `Left(BaseError)` if the arguments are invalid.
   */
  private def parseBorderedTable(args: Array[String], index: Int): Either[BaseError, Option[AsciiConvertor]] = {
    // Ensure there are characters after `--table=bordered`
    if (index + 1 >= args.length || args(index + 1).startsWith("--")) {
      return Left(BaseError(
        message = "Custom characters must be specified after --table=bordered argument.",
        context = LogContext.UI,
        errorCode = GeneralErrorCodes.InvalidArgument
      ))
    }

    val characters = args(index + 1)

    // Ensure there are borders specified after the characters
    if (index + 2 >= args.length || !args(index + 2).matches("""^\[([+-]?\d+(,[+-]?\d+)*)?\]$""")) {
      return Left(BaseError(
        message = "Borders must be specified after the characters in the format [int,int,...].",
        context = LogContext.UI,
        errorCode = GeneralErrorCodes.InvalidArgument
      ))
    }

    val bordersString = args(index + 2).stripPrefix("[").stripSuffix("]")
    val borders = bordersString.split(",").toList.flatMap { value =>
      value.stripPrefix("+").toIntOption
    }

    Right(Some(new AsciiConvertor(new BorderedAsciiTable(characters, borders))))
  }
}
