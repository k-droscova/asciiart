package Services.Importers.FileImporters

import Core.Errors.{BaseError, ImageLoadingErrorCodes, LogContext}

/**
 * Imports PNG image files.
 *
 * @param path The path to the PNG file.
 * @throws BaseError If the file is not a valid PNG.
 */
class PNGFileImporter(path: String) extends FileImporter(path) {
  override protected def validateFormat(formatName: String): Unit = {
    if (formatName != "png") {
      throw BaseError(
        message = "Invalid file format for PNG importer.",
        context = LogContext.UI,
        errorCode = ImageLoadingErrorCodes.UnsupportedFormat
      )
    }
  }
}
