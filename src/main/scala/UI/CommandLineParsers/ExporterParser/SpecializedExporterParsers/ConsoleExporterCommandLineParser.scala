package UI.CommandLineParsers.ExporterParser.SpecializedExporterParsers

import Core.Errors.{BaseError, GeneralErrorCodes, LogContext}
import Services.Exporters.{ConsoleExporter, Exporter}

/**
 * Specialized parser for the `--output-console` argument.
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
