package UI.CommandLineParsers.ExporterParser.SpecializedExporterParsers

import Core.Errors.{BaseError, GeneralErrorCodes, LogContext}
import Services.Exporters.{ConsoleExporter, Exporter}

/**
 * Parses command line arguments to create a console-based exporter.
 *
 * The `ConsoleExporterCommandLineParser` detects and validates the `--output-console` argument,
 * which specifies that the output should be displayed in the console.
 *
 * Argument Format:
 * - `--output-console`: Specifies that the ASCII image should be exported to the console.
 *
 * Validation:
 * - Only one `--output-console` argument is allowed.
 *
 * Example:
 * ```
 * --output-console
 * ```
 *
 * Errors:
 * - Returns a `BaseError` if:
 *   - Multiple `--output-console` arguments are detected.
 */
class ConsoleExporterCommandLineParser extends SpecializedExporterCommandLineParser[ConsoleExporter] {
  override def parse(args: Array[String]): Either[BaseError, Option[ConsoleExporter]] = {
    val consoleArgCount = args.count(_ == "--output-console")

    consoleArgCount match {
      case 0 => Right(None) // Argument not found
      case 1 => Right(Some(new ConsoleExporter()))
      case _ => Left(BaseError(
        message = "Only one --output-console argument is allowed.",
        context = LogContext.UI,
        errorCode = GeneralErrorCodes.InvalidArgument
      ))
    }
  }
}
