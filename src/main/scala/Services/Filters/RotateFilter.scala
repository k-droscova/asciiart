package Services.Filters

import Core.Errors.{BaseError, FilterErrorCodes, LogContext}
import Core.Models.Image.GrayscaleImage
import Core.Models.Pixel.GrayscalePixel
class RotateFilter(angle: Int) extends Filter {
  if (angle % 90 != 0) {
    throw BaseError(message = s"Angle $angle is invalid. Angle must be a multiple of 90 degrees.", context = LogContext.UI, errorCode = FilterErrorCodes.InvalidRotationAngle)
  }

  private val normalizedAngle: Int = ((angle % 360) + 360) % 360

  override def apply(image: GrayscaleImage): GrayscaleImage = {
    val rotatedPixels = normalizedAngle match {
      case 0   => image.pixels                    
      case 90  => rotate90(image.pixels)         
      case 180 => rotate180(image.pixels)         
      case 270 => rotate270(image.pixels)
    }
    GrayscaleImage(rotatedPixels)
  }

  private def rotate90(pixels: Vector[Vector[GrayscalePixel]]): Vector[Vector[GrayscalePixel]] = {
    pixels.transpose.map(_.reverse)
  }

  private def rotate180(pixels: Vector[Vector[GrayscalePixel]]): Vector[Vector[GrayscalePixel]] = {
    pixels.map(_.reverse).reverse
  }

  private def rotate270(pixels: Vector[Vector[GrayscalePixel]]): Vector[Vector[GrayscalePixel]] = {
    pixels.transpose.reverse
  }
}
