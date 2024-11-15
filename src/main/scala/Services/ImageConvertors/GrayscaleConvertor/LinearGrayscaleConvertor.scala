package Services.ImageConvertors.GrayscaleConvertor

import Core.Models.Pixel.{GrayscalePixel, RGBPixel}

class LinearGrayscaleConvertor extends GrayscaleConvertor {

  /**
   * Converts an RGB pixel to Grayscale using a weighted formula:
   * grayscale = (0.3 * Red) + (0.59 * Green) + (0.11 * Blue)
   */
  override protected def convertPixel(rgbPixel: RGBPixel): GrayscalePixel = {
    val grayscaleValue = (0.3 * rgbPixel.red + 0.59 * rgbPixel.green + 0.11 * rgbPixel.blue).toInt
    GrayscalePixel(grayscaleValue)
  }
}