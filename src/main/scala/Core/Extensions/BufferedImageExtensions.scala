package Core.Extensions

import java.awt.image.BufferedImage
import Core.Models.*
import Services.Logging.*
import Services.Logging.Errors.{BaseError, GeneralErrorCodes}

/**
 * Extension methods for BufferedImage to handle conversion to Image.
 */
object BufferedImageExtensions {

  /**
   * Extension method to convert a BufferedImage to a custom Image type.
   * Handles RGB, Grayscale, and ARGB BufferedImage types.
   */
  extension (bufferedImage: BufferedImage) {
    def toImage: Image[? <: Pixel] = {
      val width = bufferedImage.getWidth
      val height = bufferedImage.getHeight

      bufferedImage.getType match {
        case BufferedImage.TYPE_INT_RGB =>
          val pixels = (0 until height).map { y =>
            (0 until width).map { x =>
              val color = bufferedImage.getRGB(x, y)
              val red = (color >> 16) & 0xFF
              val green = (color >> 8) & 0xFF
              val blue = color & 0xFF
              RGBPixel(red, green, blue)
            }.toVector
          }.toVector
          new Image[RGBPixel](width, height, pixels)

        case BufferedImage.TYPE_BYTE_GRAY =>
          val pixels = (0 until height).map { y =>
            (0 until width).map { x =>
              val color = bufferedImage.getRGB(x, y)
              val intensity = color & 0xFF
              GrayscalePixel(intensity)
            }.toVector
          }.toVector
          new Image[GrayscalePixel](width, height, pixels)

        case BufferedImage.TYPE_INT_ARGB =>
          val pixels = (0 until height).map { y =>
            (0 until width).map { x =>
              val color = bufferedImage.getRGB(x, y)
              val alpha = (color >> 24) & 0xFF
              val red = (color >> 16) & 0xFF
              val green = (color >> 8) & 0xFF
              val blue = color & 0xFF
              AlphaPixel(red, green, blue, alpha)
            }.toVector
          }.toVector
          new Image[AlphaPixel](width, height, pixels)

        case _ =>
          throw BaseError(
            message = "Unsupported image type: ${bufferedImage.getType}",
            context = LogContext.UI,
            errorCode = GeneralErrorCodes.InvalidArgument
          )
      }
    }
  }
}
