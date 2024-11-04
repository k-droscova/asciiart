package Services.Filters

import Core.Models.Image.GrayscaleImage
import Core.Models.Pixel.GrayscalePixel
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
