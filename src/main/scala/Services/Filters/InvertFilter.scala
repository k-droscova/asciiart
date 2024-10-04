package Services.Filters

import Core.Models.{Pixel, Image}
/**
 * A filter that inverts the grayscale or color values of the image.
 */
case class InvertFilter() extends Filter {

  /**
   * Applies the invert filter to the given image.
   * Delegates the inversion logic to the pixel's `invertedPixel` method.
   *
   * @param image the image to invert.
   * @return the inverted image.
   */
  override def apply(image: Image[? <: Pixel]): Image[? <: Pixel] = {
    val newPixels = image.pixels.map { row =>
      row.map { pixel =>
        pixel.invertedPixel
      }
    }
    new Image(image.width, image.height, newPixels)
  }
}