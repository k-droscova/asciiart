package Services.Filters

import Core.Models.Image.GrayscaleImage
import Core.Models.Pixel.GrayscalePixel

/**
 * InvertFilter inverts the intensities of a GrayscaleImage.
 * Each pixel's intensity is transformed to `255 - intensity`, effectively creating
 * a photographic negative effect.
 */
class InvertFilter extends Filter {
  private val inversionConstant = 255
  override def apply(image: GrayscaleImage): GrayscaleImage = {
    val invertedPixels = image.pixels.map { row =>
      row.map { pixel =>
        val invertedIntensity = inversionConstant - pixel.intensity
        GrayscalePixel(invertedIntensity)
      }
    }
    GrayscaleImage(invertedPixels)
  }
}
