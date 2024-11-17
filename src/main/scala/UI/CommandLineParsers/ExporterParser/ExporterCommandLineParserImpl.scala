package UI.CommandLineParsers.ExporterParser

import Core.Errors.{BaseError, GeneralErrorCodes, LogContext}
import Services.Exporters.{ConsoleExporter, Exporter, FileExporter}
import UI.CommandLineParsers.ExporterParser.SpecializedExporterParsers.{ConsoleExporterCommandLineParser, FileExporterCommandLineParser, SpecializedExporterCommandLineParser}
import UI.CommandLineParsers.SingleInputCommandLineParser

/**
 * Implementation of `ExporterCommandLineParser` that supports multiple export methods.
 *
 * The `ExporterCommandLineParserImpl` delegates the parsing logic to a list of specialized parsers,
 * each responsible for detecting and validating a specific export method. It ensures that exactly one
 * export method is selected and provides meaningful errors for invalid or conflicting inputs.
 *
 * Supported Export Methods:
 * - `--output-file <path>`: Exports the ASCII art to the specified file path.
 * - `--output-console`: Prints the ASCII art directly to the console.
 *
 * @param parsers A list of specialized parsers for the supported export methods. Defaults to the following parsers:
 *                - `FileExporterCommandLineParser`: Handles the `--output-file` argument.
 *                - `ConsoleExporterCommandLineParser`: Handles the `--output-console` argument.
 */
class ExporterCommandLineParserImpl(
                                     parsers: List[SpecializedExporterCommandLineParser[? <: Exporter]] = List(
                                       new FileExporterCommandLineParser(),
                                       new ConsoleExporterCommandLineParser()
                                     )
                                   ) extends SingleInputCommandLineParser[Exporter](parsers) with ExporterCommandLineParser {

  /**
   * Error to throw when no output method is specified.
   */
  override protected def noInputError(): BaseError = {
    BaseError(
      message = "You must specify either --output-file or --output-console.",
      context = LogContext.UI,
      errorCode = GeneralErrorCodes.InvalidArgument
    )
  }

  /**
   * Error to throw when multiple output methods are detected.
   */
  override protected def multipleInputError(): BaseError = {
    BaseError(
      message = "Cannot specify both --output-file and --output-console.",
      context = LogContext.UI,
      errorCode = GeneralErrorCodes.InvalidArgument
    )
  }
}