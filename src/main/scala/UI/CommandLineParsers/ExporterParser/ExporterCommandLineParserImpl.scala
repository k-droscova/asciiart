package UI.CommandLineParsers.ExporterParser

import Core.Errors.{BaseError, GeneralErrorCodes, LogContext}
import Services.Exporters.{ConsoleExporter, Exporter, FileExporter}
import UI.CommandLineParsers.ExporterParser.SpecializedExporterParsers.{ConsoleExporterCommandLineParser, FileExporterCommandLineParser, SpecializedExporterCommandLineParser}
import UI.CommandLineParsers.SingleInputCommandLineParser

/**
 * Parses command line arguments related to exporting ASCII art.
 *
 * This class implements the `ExporterCommandLineParser` trait and uses specialized parsers
 * to handle different export options (`--output-file` and `--output-console`).
 */
class ExporterCommandLineParserImpl(
                                     parsers: List[SpecializedExporterCommandLineParser] = List(
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