package Services.Filters

import Core.Models.{Pixel, Image}

/**
 * A filter that adjusts the brightness of the image.
 *
 * @param brightness the brightness adjustment value (-255 to 255).
 */
case class BrightnessFilter(brightness: Int) extends Filter {

  /**
   * Applies the brightness filter to the given image.
   * Delegates the brightness adjustment to the pixel's `adjustBrightness` method.
   *
   * @param image the image to adjust brightness.
   * @return the brightness-adjusted image.
   */
  override def apply(image: Image[? <: Pixel]): Image[? <: Pixel] = {
    val newPixels = image.pixels.map { row =>
      row.map { pixel =>
        pixel.adjustBrightness(brightness)
      }
    }
    new Image(image.width, image.height, newPixels)
  }
}
