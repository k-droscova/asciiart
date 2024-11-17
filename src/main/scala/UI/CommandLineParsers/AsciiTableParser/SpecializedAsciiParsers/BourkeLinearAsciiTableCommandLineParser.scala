package UI.CommandLineParsers.AsciiTableParser.SpecializedAsciiParsers

import Core.Errors.{BaseError, GeneralErrorCodes, LogContext}
import Core.Models.AsciiTable.Linear.BourkeLinearAsciiTable
import Services.ImageConvertors.AsciiConvertor.AsciiConvertor

/**
 * Parses command line arguments to create a Bourke ASCII table converter.
 *
 * The `BourkeLinearAsciiTableCommandLineParser` detects and validates arguments specific
 * to the `--table=bourke` option. This option allows users to select the Bourke ASCII table.
 *
 * Argument Format:
 * - `--table=bourke`: Selects the Bourke ASCII table.
 *
 * Validation:
 * - Only one `--table=bourke` argument is allowed.
 *
 * Example:
 * ```
 * --table=bourke
 * ```
 *
 * Errors:
 * - Returns a `BaseError` if:
 *   - Multiple `--table=bourke` arguments are detected.
 */
class BourkeLinearAsciiTableCommandLineParser extends SpecializedAsciiTableCommandLineParser {
  override def parse(args: Array[String]): Either[BaseError, Option[AsciiConvertor]] = {
    val bourkeArgs = args.filter(_ == "--table=bourke")

    bourkeArgs match {
      case Array() => Right(None) // No `--table=bourke` argument provided
      case Array("--table=bourke") => Right(Some(new AsciiConvertor(new BourkeLinearAsciiTable())))
      case _ =>
        Left(BaseError(
          message = "The --table=bourke argument is specified multiple times.",
          context = LogContext.UI,
          errorCode = GeneralErrorCodes.InvalidArgument
        ))
    }
  }
}