package Services.Filters

import Core.Models.Image.GrayscaleImage
import Core.Models.Pixel.GrayscalePixel
class BrightnessFilter(brightnessAdjustment: Int) extends Filter {
  override def apply(image: GrayscaleImage): GrayscaleImage = {
    val adjustedPixels = image.pixels.map { row =>
      row.map { pixel =>
        val newIntensity = (pixel.intensity + brightnessAdjustment).clamp(0, 255)
        new GrayscalePixel(newIntensity)
      }
    }
    new GrayscaleImage(adjustedPixels)
  }

  /**
   * Adds a `clamp` method to `Int` values, letting us limit an integer within
   * a given range. This is defined privately in `BrightnessFilter`, through
   * a private implicit class, which acts as an extension on Int within `BrightnessFilter`
   *
   * @param value The integer to clamp.
   */
  private implicit class IntOps(val value: Int) {

    /**
     * Restricts `value` to be within the bounds of `min` and `max`.
     * If `value` is below `min`, it returns `min`; if above `max`, it returns `max`.
     * Useful for keeping pixel intensity between 0 and 255 in brightness adjustments.
     *
     * @param min The lowest allowed value.
     * @param max The highest allowed value.
     * @return The clamped value within `[min, max]`.
     *
     * Example:
     * {{{
     *   val adjustedValue = 300.clamp(0, 255) // Result: 255
     * }}}
     */
    def clamp(min: Int, max: Int): Int = Math.max(min, Math.min(value, max))
  }
}
