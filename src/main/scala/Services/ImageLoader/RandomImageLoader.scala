package Services.ImageLoader
import scala.util.Random
import Core.Models.{GrayscalePixel, Image, Pixel}

/**
 * A concrete implementation of ImageLoader that generates random grayscale images.
 * Generates images with adjustable or random dimensions, within optional min/max constraints.
 * If min/max values are not provided, defaults to a range of 50 to 150.
 * Supports optional seeding
 */
class RandomImageLoader(
                         width: Option[Int] = None,
                         height: Option[Int] = None,
                         minWidth: Int = 50,
                         maxWidth: Int = 150,
                         minHeight: Int = 50,
                         maxHeight: Int = 150
                       ) extends ImageLoader {

  private val random = new Random()

  /**
   * Generates a random grayscale image with adjustable or random dimensions, within min/max constraints.
   * @return a randomly generated Image[GrayscalePixel].
   */
  override def loadImage(): Image[GrayscalePixel] = {
    // If width/height are not provided, generate random dimensions within the min/max range
    val imageWidth = width.getOrElse(randomBetween(minWidth, maxWidth))
    val imageHeight = height.getOrElse(randomBetween(minHeight, maxHeight))

    // Generate random grayscale pixels (intensity between 0 and 255)
    val pixels = Vector.fill(imageHeight, imageWidth) {
      val intensity = random.nextInt(256) // Intensity value for grayscale
      GrayscalePixel(intensity)
    }

    new Image[GrayscalePixel](imageWidth, imageHeight, pixels)
  }

  /**
   * Utility method to generate a random integer between a given min and max value (inclusive).
   */
  private def randomBetween(min: Int, max: Int): Int = {
    random.nextInt((max - min) + 1) + min
  }
}