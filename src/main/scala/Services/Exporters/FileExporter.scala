package Services.Exporters

import Core.Errors.{BaseError, LogContext, OutputErrorCodes}
import Core.Models.Image.AsciiImage

import java.io.{File, PrintWriter}

/**
 * FileExporter is responsible for exporting ASCII images to a specified file path.
 * It ensures that the file path is valid, the parent directories exist, and that
 * the file does not already exist before saving the image content.
 *
 * @param path The file path to save the ASCII image. This can be either an absolute
 *             or relative path. If the specified path does not end with ".txt",
 *             the extension will be automatically added.
 *
 * @throws BaseError If the file already exists at the specified path, or if an
 *                   error occurs while writing the image content to the file.
 *
 * The exporter will create any necessary parent directories if they do not already exist.
 * If an error occurs during the writing process, any partially written file will be deleted.
 */
class FileExporter(path: String) extends Exporter {
  private val file = new File(path)

  if (file.exists()) {
    throw BaseError(message = s"File already exists at path: $path. Please choose a different file name.", context = LogContext.UI, errorCode = OutputErrorCodes.FileSaveFailed)
  }

  if (!file.getParentFile.exists()) {
    file.getParentFile.mkdirs()
  }

  private val finalPath = if (file.getName.toLowerCase.endsWith(".txt")) {
    path
  } else {
    path + ".txt"
  }

  /**
   * Exports the provided ASCII image to the specified file path.
   *
   * @param image The ASCII image to be exported.
   * @throws BaseError If an error occurs while saving the image to the file,
   *                   including I/O errors.
   */
  override def exportImage(image: AsciiImage): Unit = {
    val writer = new PrintWriter(finalPath)
    try {
      for (y <- 0 until image.getHeight) {
        val line = (0 until image.getWidth).map(x => image.getPixel(x, y).char).mkString("")
        writer.println(line)
      }
    } catch {
      case e: Exception =>
        val file = new File(finalPath)
        if (file.exists())
          file.delete()
        throw BaseError(message = s"Failed to save image to file: ${e.getMessage}.", context = LogContext.System, errorCode = OutputErrorCodes.FileSaveFailed)
    } finally {
      writer.close()
    }
  }
}
