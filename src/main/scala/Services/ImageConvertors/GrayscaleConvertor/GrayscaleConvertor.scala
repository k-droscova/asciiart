package Services.ImageConvertors.GrayscaleConvertor

import Core.Models.Image.{GrayscaleImage, RGBImage}
import Core.Models.Pixel.{GrayscalePixel, RGBPixel}
import Services.ImageConvertors.ImageConvertor

/**
 * Abstract class for converting an RGB image to a Grayscale image.
 * Provides a base implementation for processing each pixel in the image
 * while allowing subclasses to define the specific pixel conversion formula.
 */
abstract class GrayscaleConvertor extends ImageConvertor[RGBImage, GrayscaleImage] {

  /**
   * Converts the entire RGB image to a Grayscale image by applying
   * the conversion formula to each pixel.
   */
  override def convert(input: RGBImage): GrayscaleImage = {
    val grayscalePixels = input.pixels.map { row =>
      row.map(convertPixel)
    }
    GrayscaleImage(grayscalePixels)
  }

  /**
   * Converts a single RGB pixel to a Grayscale pixel.
   * Subclasses will provide specific implementations for the conversion.
   */
  protected def convertPixel(rgbPixel: RGBPixel): GrayscalePixel
}