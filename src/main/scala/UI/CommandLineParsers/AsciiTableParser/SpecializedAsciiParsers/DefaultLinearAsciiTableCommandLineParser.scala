package UI.CommandLineParsers.AsciiTableParser.SpecializedAsciiParsers

import Core.Errors.{BaseError, GeneralErrorCodes, LogContext}
import Core.Models.AsciiTable.Linear.DefaultLinearAsciiTable
import Services.ImageConvertors.AsciiConvertor.AsciiConvertor

/**
 * Parses command line arguments to create a default ASCII table converter.
 *
 * The `DefaultLinearAsciiTableCommandLineParser` detects and validates arguments specific
 * to the `--table=default` option or defaults to the Default ASCII table if no `--table=` argument is provided.
 *
 * Argument Format:
 * - `--table=default`: Selects the Default ASCII table.
 *
 * Validation:
 * - Defaults to the Default ASCII table if no `--table=` argument is provided.
 * - Only one `--table=default` argument is allowed.
 *
 * Example:
 * ```
 * --table=default
 * ```
 * or simply:
 * ```
 * (no --table argument)
 * ```
 *
 * Errors:
 * - Returns a `BaseError` if:
 *   - Multiple `--table=default` arguments are detected.
 */
class DefaultLinearAsciiTableCommandLineParser extends SpecializedAsciiTableCommandLineParser {
  override def parse(args: Array[String]): Either[BaseError, Option[AsciiConvertor]] = {
    val tableArgs = args.filter(_.startsWith("--table="))

    tableArgs match {
      case Array("--table=default") => Right(Some(new AsciiConvertor(new DefaultLinearAsciiTable)))
      case Array() => Right(Some(new AsciiConvertor(new DefaultLinearAsciiTable))) // No `--table=` provided
      case args if args.count(_ == "--table=default") > 1 =>
        Left(BaseError(
          message = "The --table=default argument is specified multiple times.",
          context = LogContext.UI,
          errorCode = GeneralErrorCodes.InvalidArgument
        ))
      case _ => Right(None) // Let other parsers handle their cases
    }
  }
}
