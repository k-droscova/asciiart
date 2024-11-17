package UI.CommandLineParsers.ExporterParser.SpecializedExporterParsers

import Core.Errors.{BaseError, GeneralErrorCodes, LogContext}
import Services.Exporters.{Exporter, FileExporter}

/**
 * Specialized parser for the `--output-file` argument.
 */
class FileExporterCommandLineParser extends SpecializedExporterCommandLineParser[FileExporter] {
  override def parse(args: Array[String]): Either[BaseError, Option[FileExporter]] = {
    val fileArgs = args.sliding(2).filter(_.head == "--output-file").toList

    if (fileArgs.size > 1) {
      return Left(BaseError(
        message = "Only one --output-file argument is allowed.",
        context = LogContext.UI,
        errorCode = GeneralErrorCodes.InvalidArgument
      ))
    }

    if (fileArgs.isEmpty) {
      return Right(None)
    }

    val path = fileArgs.head.last
    if (path.startsWith("--")) {
      return Left(BaseError(
        message = "Output filepath was not specified after --output-file.",
        context = LogContext.UI,
        errorCode = GeneralErrorCodes.InvalidArgument
      ))
    }

    try {
      Right(Some(new FileExporter(path.trim)))
    } catch {
      case e: BaseError => Left(e)
      case t: Throwable => Left(BaseError(
        message = s"Unexpected error while initializing FileExporter: ${t.getMessage}",
        context = LogContext.System,
        errorCode = GeneralErrorCodes.UnknownError
      ))
    }
  }
}