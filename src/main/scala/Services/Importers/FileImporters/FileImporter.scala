package Services.Importers.FileImporters

import Core.Errors.{BaseError, ImageLoadingErrorCodes, LogContext}
import Core.Models.Image.RGBImage
import Core.Models.Pixel.RGBPixel
import Services.Importers.Importer

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.stream.ImageInputStream
import javax.imageio.{ImageIO, ImageReader}
import scala.util.Try

/**
 * Abstract class for importing image files. Handles common validation and loading logic,
 * leaving format-specific validation to subclasses.
 *
 * @param path The path to the file to be imported.
 * @throws BaseError If the file does not exist, is not a valid file, or is unreadable.
 */
abstract class FileImporter(path: String) extends Importer {
  private val file = new File(path)

  if (!file.exists()) {
    throw BaseError(
      message = s"File with filepath $path doesn't exist.",
      context = LogContext.UI,
      errorCode = ImageLoadingErrorCodes.FileNotFound
    )
  }

  if (!file.isFile) {
    throw BaseError(
      message = s"File with filepath $path is not a file.",
      context = LogContext.UI,
      errorCode = ImageLoadingErrorCodes.FileUnreadable
    )
  }

  private val formatName: String = getFormatName(file)
  validateFormat(formatName)

  /**
   * Abstract method to validate the format-specific logic in subclasses.
   * @param formatName The actual format of the file as determined by `getFormatName`.
   */
  protected def validateFormat(formatName: String): Unit

  /**
   * Gets the format name of the image file using `ImageIO`. It is protected, which allows subclasses to override if desired.
   * @param file The image file.
   * @return The format name of the image in lowercase.
   */
  private def getFormatName(file: File): String = {
    val inputStream: ImageInputStream = ImageIO.createImageInputStream(file)
    try {
      val readers = ImageIO.getImageReaders(inputStream)
      if (readers.hasNext) {
        val reader = readers.next()
        reader.getFormatName.toLowerCase
      } else {
        "unknown"
      }
    } finally {
      inputStream.close()
    }
  }

  override def importImage(): RGBImage = {
    val bufferedImage = Try(ImageIO.read(file)) match {
      case scala.util.Success(img) => img
      case scala.util.Failure(exception) =>
        throw BaseError(
          message = s"Failed to load image: ${exception.getMessage}.",
          context = LogContext.System,
          errorCode = ImageLoadingErrorCodes.FileUnreadable
        )
    }
    convertBufferedImageToRGBImage(bufferedImage)
  }

  private def convertBufferedImageToRGBImage(bufferedImage: BufferedImage): RGBImage = {
    val width = bufferedImage.getWidth
    val height = bufferedImage.getHeight
    val pixels = Vector.tabulate(height, width) { (y, x) =>
      createRGBPixel(bufferedImage.getRGB(x, y))
    }
    RGBImage(pixels)
  }

  private def createRGBPixel(rgb: Int): RGBPixel = {
    val red = (rgb >> 16) & 0xff
    val green = (rgb >> 8) & 0xff
    val blue = rgb & 0xff
    RGBPixel(red, green, blue)
  }
}