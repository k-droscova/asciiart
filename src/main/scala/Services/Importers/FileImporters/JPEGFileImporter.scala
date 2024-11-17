package Services.Importers.FileImporters

import Core.Errors.{BaseError, ImageLoadingErrorCodes, LogContext}

/**
 * Imports JPEG image files.
 *
 * @param path The path to the JPEG file.
 * @throws BaseError If the file is not a valid JPEG.
 */
class JPEGFileImporter(path: String) extends FileImporter(path) {
  override protected def validateFormat(formatName: String): Unit = {
    if (!Set("jpeg", "jpg").contains(formatName)) {
      throw BaseError(
        message = "Invalid file format for JPEG importer.",
        context = LogContext.UI,
        errorCode = ImageLoadingErrorCodes.UnsupportedFormat
      )
    }
  }
}
