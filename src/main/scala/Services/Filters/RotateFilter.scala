package Services.Filters

import Core.Models.{Image, Pixel}
import Services.Logging.Errors.BaseError
import Services.Logging.Errors.FilterErrorCodes
import Services.Logging.{LogContext, LogSeverity, LogSource}

/**
 * A filter that rotates the image by a specified number of degrees.
 * Supports rotation in multiples of 90 degrees (e.g., -270, -180, -90, 90, 180, 270).
 *
 * @param degrees the number of degrees to rotate the image.
 */
case class RotateFilter(degrees: Int) extends Filter {

  /**
   * Applies the rotation filter to the given image.
   *
   * @param image the image to rotate.
   * @return the rotated image.
   */
  override def apply(image: Image[? <: Pixel]): Image[? <: Pixel] = {
    val normalizedDegrees = ((degrees % 360) + 360) % 360 // Normalize the degrees to 0, 90, 180 or 270

    normalizedDegrees match {
      case 90  => rotate90(image)
      case 180 => rotate180(image)
      case 270 => rotate270(image)
      case _   => throw BaseError(
        message = s"Invalid value for rotation: rotation must be a multiple of 90 degrees, but got $degrees.",
        context = LogContext.UI,
        errorCode = FilterErrorCodes.InvalidRotationAngle
      )
    }
  }

  private def rotate90(image: Image[? <: Pixel]): Image[? <: Pixel] = {
    val newPixels = (0 until image.width).map { x =>
      (0 until image.height).reverse.map { y =>
        image.getPixel(x, y)
      }.toVector
    }.toVector
    new Image(image.height, image.width, newPixels)
  }

  private def rotate180(image: Image[? <: Pixel]): Image[? <: Pixel] = {
    val newPixels = image.pixels.reverse.map(_.reverse)
    new Image(image.width, image.height, newPixels)
  }

  private def rotate270(image: Image[? <: Pixel]): Image[? <: Pixel] = {
    val newPixels = (0 until image.width).reverse.map { x =>
      (0 until image.height).map { y =>
        image.getPixel(x, y)
      }.toVector
    }.toVector
    new Image(image.height, image.width, newPixels)
  }
}