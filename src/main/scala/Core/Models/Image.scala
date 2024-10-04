package Core.Models

import sourcecode.{File, Line, Name}
import Services.Logging.Errors.{BaseError, GeneralErrorCodes}
import Services.Logging.{LogContext, LogSeverity, LogSource}

/**
 * A class representing an immutable image composed of homogeneous pixels of the same type.
 * @param width the width of the image.
 * @param height the height of the image.
 * @param pixels an immutable 2D Vector of homogeneous pixels of type P.
 * @tparam P the type of the pixel, which must be a subtype of Pixel.
 */
class Image[P <: Pixel](val width: Int, val height: Int, val pixels: Vector[Vector[P]]) {

  /**
   * Checks if the given (x, y) coordinates are within the bounds of the image.
   * @param x the x-coordinate to check.
   * @param y the y-coordinate to check.
   * @throws BaseError if the coordinates are out of bounds.
   */
  private def validateCoordinates(x: Int, y: Int)(implicit file: File, name: Name, line: Line): Unit = {
    if (x < 0 || x >= width) {
      throw BaseError(
        message = s"X coordinate $x is out of bounds",
        context = LogContext.UI,
        errorCode = GeneralErrorCodes.InvalidArgument
      )
    }
    if (y < 0 || y >= height) {
      throw BaseError(
        message = s"Y coordinate $y is out of bounds",
        context = LogContext.UI,
        errorCode = GeneralErrorCodes.InvalidArgument
      )
    }
  }

  /**
   * Retrieves the pixel at the specified (x, y) coordinates.
   * @param x the x-coordinate of the pixel.
   * @param y the y-coordinate of the pixel.
   * @return the Pixel at the specified location.
   * @throws BaseError if the coordinates are out of bounds.
   */
  def getPixel(x: Int, y: Int)(implicit file: sourcecode.File, name: sourcecode.Name, line: sourcecode.Line): P = {
    validateCoordinates(x, y)
    pixels(y)(x)
  }

  /**
   * Returns a new Image with the specified pixel replaced at (x, y).
   * Since the Image is immutable, this method returns a new instance of Image.
   * @param x the x-coordinate where the pixel will be replaced.
   * @param y the y-coordinate where the pixel will be replaced.
   * @param newPixel the new pixel to set at the specified location (must be of type P).
   * @return a new Image with the updated pixel at (x, y).
   * @throws BaseError if the coordinates are out of bounds.
   */
  def withPixel(x: Int, y: Int, newPixel: P)(implicit file: sourcecode.File, name: sourcecode.Name, line: sourcecode.Line): Image[P] = {
    validateCoordinates(x, y)
    val newRow = pixels(y).updated(x, newPixel)
    val newPixels = pixels.updated(y, newRow)
    new Image(width, height, newPixels)
  }

  /**
   * Converts the entire image to grayscale and returns a new Image instance with GrayscalePixels.
   * This is done by converting each pixel of type P to its grayscale value.
   * @return a new Image with GrayscalePixel.
   */
  def toGrayscaleCopy: Image[GrayscalePixel] = {
    val grayscalePixels = pixels.map { row =>
      row.map { pixel =>
        GrayscalePixel(pixel.toGrayscale)
      }
    }
    new Image(width, height, grayscalePixels)
  }
}

/**
 * A specialized Image class that only contains GrayscalePixel.
 * This class overrides the toGrayscaleCopy method to return itself,
 * as the image is already grayscale.
 */
class GrayscaleImage(width: Int, height: Int, pixels: Vector[Vector[GrayscalePixel]])
  extends Image[GrayscalePixel](width, height, pixels) {

  /**
   * Since this image is already grayscale, simply return the same image
   * without creating a new grayscale copy.
   * @return the current instance, as the image is already grayscale.
   */
  override def toGrayscaleCopy: GrayscaleImage = this
}