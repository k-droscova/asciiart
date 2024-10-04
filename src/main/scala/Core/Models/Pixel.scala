package Core.Models

/**
 * Trait representing a Pixel in an image.
 * Provides methods to convert the pixel to grayscale, retrieve its value,
 * invert its value, adjust brightness, and clamp pixel values.
 */
trait Pixel {
  def toGrayscale: Int
  def getValue: Any
  def invertedPixel: Pixel
  def adjustBrightness(amount: Int): Pixel

  protected def clamp(value: Int, min: Int = 0, max: Int = 255): Int = {
    math.max(min, math.min(max, value))
  }
}

/**
 * Case class representing an RGB pixel.
 */
case class RGBPixel(red: Int, green: Int, blue: Int) extends Pixel {

  override def toGrayscale: Int = (0.3 * red + 0.59 * green + 0.11 * blue).toInt

  override def getValue: (Int, Int, Int) = (red, green, blue)

  override def invertedPixel: Pixel = RGBPixel(255 - red, 255 - green, 255 - blue)

  override def adjustBrightness(amount: Int): Pixel = {
    RGBPixel(clamp(red + amount), clamp(green + amount), clamp(blue + amount))
  }
}

/**
 * Case class representing a Grayscale pixel.
 */
case class GrayscalePixel(intensity: Int) extends Pixel {

  override def toGrayscale: Int = intensity

  override def getValue: Int = intensity

  override def invertedPixel: Pixel = GrayscalePixel(255 - intensity)

  override def adjustBrightness(amount: Int): Pixel = {
    GrayscalePixel(clamp(intensity + amount))
  }
}

/**
 * Case class representing an RGBA pixel with an alpha (transparency) component.
 */
case class AlphaPixel(red: Int, green: Int, blue: Int, alpha: Int) extends Pixel {

  // Reuse RGBPixel for grayscale calculation
  private val rgbPixel = RGBPixel(red, green, blue)

  override def toGrayscale: Int = rgbPixel.toGrayscale

  override def getValue: (Int, Int, Int, Int) = (red, green, blue, alpha)

  override def invertedPixel: Pixel = AlphaPixel(255 - red, 255 - green, 255 - blue, alpha)

  override def adjustBrightness(amount: Int): Pixel = {
    AlphaPixel(clamp(red + amount), clamp(green + amount), clamp(blue + amount), alpha)
  }
}