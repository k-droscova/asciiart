package UI.CommandLineParsers.ExporterParser.SpecializedExporterParsers

import Core.Errors.{BaseError, GeneralErrorCodes, LogContext}
import Services.Exporters.{Exporter, FileExporter}

/**
 * Parses command line arguments to create a file-based exporter.
 *
 * The `FileExporterCommandLineParser` detects and validates the `--output-file` argument,
 * which specifies the path to save the output ASCII image to a file.
 *
 * Argument Format:
 * - `--output-file <path>`:
 *   - `<path>`: The path to the file where the ASCII image will be saved. This can be
 *               an absolute or relative path.
 *
 * Validation:
 * - Only one `--output-file` argument is allowed.
 * - The filepath must be specified immediately after the `--output-file` argument.
 *
 * Example:
 * ```
 * --output-file path/to/output.txt
 * ```
 *
 * Errors:
 * - Returns a `BaseError` if:
 *   - Multiple `--output-file` arguments are detected.
 *   - The filepath is missing or improperly specified.
 *   - An unexpected error occurs during initialization of the `FileExporter`.
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