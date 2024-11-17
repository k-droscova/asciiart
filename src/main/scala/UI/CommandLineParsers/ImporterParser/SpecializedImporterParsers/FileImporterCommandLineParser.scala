package UI.CommandLineParsers.ImporterParser.SpecializedImporterParsers

import Core.Errors.{BaseError, GeneralErrorCodes, ImageLoadingErrorCodes, LogContext}
import Services.Importers.FileImporters.*

import scala.util.boundary.break
import scala.util.boundary

/**
 * Parses command line arguments to determine the file-based image importer.
 *
 * The `FileImporterCommandLineParser` is a specialized parser for the `--image` argument, which
 * specifies the path to an image file. It supports multiple image formats (GIF, JPEG, PNG) and
 * delegates the creation of the appropriate file importer to a list of format-specific factories.
 *
 * Argument Format:
 * - `--image <path>`: Specifies the path to the input image file.
 *
 * Validation:
 * - Only one `--image` argument is allowed.
 * - The `--image` argument must be followed by a valid file path.
 * - The file path must not start with `--`, as this indicates a missing file path.
 * - The image must be in a supported format (GIF, JPEG, PNG).
 *
 * Example:
 * ```
 * --image /path/to/image.jpg
 * ```
 *
 * Errors:
 * - Returns a `BaseError` if:
 *   - Multiple `--image` arguments are detected.
 *   - The `--image` argument is not followed by a valid file path.
 *   - The file is missing, unreadable, or in an unsupported format.
 *
 * @param importers A list of functions that create format-specific `FileImporter` instances. Defaults to:
 *                  - `GIFFileImporter`
 *                  - `JPEGFileImporter`
 *                  - `PNGFileImporter`
 */
class FileImporterCommandLineParser(
                                     val importers: List[String => FileImporter] = List(
                                       path => new GIFFileImporter(path),
                                       path => new JPEGFileImporter(path),
                                       path => new PNGFileImporter(path)
                                     )
                                   ) extends SpecializedImporterCommandLineParser[FileImporter] {

  override def parse(args: Array[String]): Either[BaseError, Option[FileImporter]] = {
    val fileArgs = args.sliding(2).filter(_.head == "--image").toList

    if (fileArgs.size > 1) {
      return Left(BaseError(
        message = "Only one --image argument is allowed.",
        context = LogContext.UI,
        errorCode = GeneralErrorCodes.InvalidArgument
      ))
    }

    if (fileArgs.isEmpty) {
      return Right(None) // No file argument, return None
    }

    val path = fileArgs.head.last
    if (path.startsWith("--")) {
      return Left(BaseError(
        message = "Filepath was not specified after --image.",
        context = LogContext.UI,
        errorCode = GeneralErrorCodes.InvalidArgument
      ))
    }

    constructImportersOneByOne(importers, path.trim).getOrElse(
      Left(BaseError(
        message = "Unable to read the file, ensure the file is in supported format.",
        context = LogContext.UI,
        errorCode = ImageLoadingErrorCodes.UnsupportedFormat
      ))
    )
  }

  private def constructImportersOneByOne(importers: List[String => FileImporter], filePath: String): Option[Either[BaseError, Option[FileImporter]]] = {
    boundary[Option[Either[BaseError, Option[FileImporter]]]]:
      for (importerFactory <- importers) {
        try {
          val importer = importerFactory(filePath) // Attempt to create importer
          break(Some(Right(Some(importer)))) // Success, exit immediately
        } catch {
          case e: BaseError if shouldExitEarly(e) =>
            break(Some(Left(e))) // Exit early for specific errors
          case e: BaseError =>
        }
      }
      None // If no importer succeeds, return None
  }
  private def shouldExitEarly(error: BaseError): Boolean = {
    error.errorCode match {
      case ImageLoadingErrorCodes.FileNotFound | ImageLoadingErrorCodes.FileUnreadable => true
      case _ => false
    }
  }
}