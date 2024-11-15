package Services.Importers

import Core.Errors.{BaseError, ImageLoadingErrorCodes, LogContext}
import Core.Models.Image.RGBImage
import Core.Models.Pixel.RGBPixel

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.{ImageIO, ImageReader}
import javax.imageio.stream.ImageInputStream
import scala.util.Try

/**
 * FileImporter is responsible for importing RGB images from specified file paths.
 * It checks for the existence and validity of the file and ensures that the image format
 * is supported before loading the image. The supported image types are jpg, jpeg, png, gif
 *
 * @param path The file path to the image to be imported. This can be either
 *             absolute or relative.
 * @throws BaseError If the file does not exist, is not a file, is of an unsupported format.
 */
class FileImporter(path: String) extends Importer {
  private val supportedFormats = Set("jpg", "jpeg", "png", "gif")
  private val file = new File(path)

  if (!file.exists()) {
    throw BaseError(message = s"File with filepath $path doesn't exist.", context = LogContext.UI, errorCode = ImageLoadingErrorCodes.FileNotFound)
  }

  if (!file.isFile) {
    throw BaseError(message = s"File with filepath $path is not a file.", context = LogContext.UI, errorCode = ImageLoadingErrorCodes.FileUnreadable)
  }

  private val formatName = getFormatName(file)

  if (!supportedFormats.contains(formatName)) {
    throw BaseError(message = s"Unsupported image format: $formatName. Supported formats are: ${supportedFormats.mkString(", ")}.", context = LogContext.UI, errorCode = ImageLoadingErrorCodes.UnsupportedFormat)
  }

  /**
   * Imports the image as an RGBImage.
   *
   * @return The imported RGBImage.
   * @throws BaseError If the image cannot be loaded for any reason.
   */
  override def importImage(): RGBImage = {
    val bufferedImage = Try(ImageIO.read(file)) match {
      case scala.util.Success(img) => img
      case scala.util.Failure(exception) => throw
        BaseError(message = s"Failed to load image: ${exception.getMessage}.", context = LogContext.System, errorCode = ImageLoadingErrorCodes.FileUnreadable)
    }
    convertBufferedImageToRGBImage(bufferedImage)
  }

  /**
   * Converts a BufferedImage to an RGBImage.
   *
   * @param bufferedImage The BufferedImage to convert.
   * @return The converted RGBImage.
   */
  private def convertBufferedImageToRGBImage(bufferedImage: BufferedImage): RGBImage = {
    val width = bufferedImage.getWidth
    val height = bufferedImage.getHeight
    val pixels = Vector.tabulate(height, width) { (y, x) =>
      createRGBPixel(bufferedImage.getRGB(x, y))
    }
    RGBImage(pixels)
  }

  /**
   * Creates an RGBPixel from an integer representation of the pixel.
   *
   * @param rgb The integer value of the pixel.
   * @return The RGBPixel created from the RGB components.
   */
  private def createRGBPixel(rgb: Int): RGBPixel = {
    val red = (rgb >> 16) & 0xff
    val green = (rgb >> 8) & 0xff
    val blue = rgb & 0xff
    RGBPixel(red, green, blue)
  }

  /**
   * Gets the format name of the image file.
   *
   * @param file The image file.
   * @return The format name of the image in lowercase
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
}
