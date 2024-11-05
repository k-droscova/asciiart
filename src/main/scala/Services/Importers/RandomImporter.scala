package Services.Importers

import Core.Errors.{BaseError, ImageLoadingErrorCodes, LogContext, LogSeverity}
import Core.Models.Image.RGBImage
import Core.Models.Pixel.RGBPixel
import scala.util.Random

/**
 * RandomImporter generates a random RGB image with specified dimensions.
 * It allows for the generation of images with either random dimensions
 * within given minimum and maximum bounds, or exact dimensions if specified.
 *
 * @param minWidth The minimum width of the generated image (default is 1).
 * @param maxWidth The maximum width of the generated image (default is 800).
 * @param minHeight The minimum height of the generated image (default is 1).
 * @param maxHeight The maximum height of the generated image (default is 800).
 * @param exactWidth Optional parameter to specify an exact width for the image.
 *                   If provided, this will override the random width generation.
 * @param exactHeight Optional parameter to specify an exact height for the image.
 *                    If provided, this will override the random height generation.
 *
 * @throws BaseError If any of the parameters are less than 0,
 *                   or if the minimum exceeds maximum value for both height and width
 */
class RandomImporter(
                      minWidth: Int = 1,
                      maxWidth: Int = 800,
                      minHeight: Int = 1,
                      maxHeight: Int = 800,
                      exactWidth: Option[Int] = None,
                      exactHeight: Option[Int] = None
                    ) extends Importer {
  private val random = new Random()
  if (exactWidth.isDefined) {
    val w = exactWidth.get
    if (w < 0) {
      throw BaseError(
        message = s"Exact width must be greater than or equal to 0.",
        severity = LogSeverity.Error,
        context = LogContext.UI,
        errorCode = ImageLoadingErrorCodes.InvalidImageDimensions
      )
    }
  } else {
    if (maxWidth < minWidth || minWidth < 0) {
      throw BaseError(
        message = s"Invalid dimensions: maxWidth must be greater than or equal to minWidth, and minWidth must be >= 0.",
        severity = LogSeverity.Error,
        context = LogContext.UI,
        errorCode = ImageLoadingErrorCodes.InvalidImageDimensions
      )
    }
  }

  if (exactHeight.isDefined) {
    val h = exactHeight.get
    if (h < 0) {
      throw BaseError(
        message = s"Exact height must be greater than or equal to 0.",
        severity = LogSeverity.Error,
        context = LogContext.UI,
        errorCode = ImageLoadingErrorCodes.InvalidImageDimensions
      )
    }
  } else {
    if (maxHeight < minHeight || minHeight < 0) {
      throw BaseError(
        message = s"Invalid dimensions: maxHeight must be greater than or equal to minHeight, and minHeight must be >= 0.",
        severity = LogSeverity.Error,
        context = LogContext.UI,
        errorCode = ImageLoadingErrorCodes.InvalidImageDimensions
      )
    }
  }

  override def importImage(): RGBImage = {
    val width = exactWidth.getOrElse(random.nextInt((maxWidth - minWidth) + 1) + minWidth)
    val height = exactHeight.getOrElse(random.nextInt((maxHeight - minHeight) + 1) + minHeight)
    val pixels = Vector.tabulate(height, width) { (y, x) =>
      createRandomRGBPixel()
    }
    RGBImage(pixels)
  }
  private def createRandomRGBPixel(): RGBPixel = {
    val red = random.nextInt(256)
    val green = random.nextInt(256)
    val blue = random.nextInt(256)
    RGBPixel(red, green, blue)
  }
}
