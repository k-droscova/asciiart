package Services.Importers.FileImporters

import Core.Errors.{BaseError, ImageLoadingErrorCodes, LogContext}

/**
 * Imports GIF image files.
 *
 * @param path The path to the GIF file.
 * @throws BaseError If the file is not a valid GIF.
 */
class GIFFileImporter(path: String) extends FileImporter(path) {
  override protected def validateFormat(formatName: String): Unit = {
    if (formatName != "gif") {
      throw BaseError(
        message = "Invalid file format for GIF importer.",
        context = LogContext.UI,
        errorCode = ImageLoadingErrorCodes.UnsupportedFormat
      )
    }
  }
}