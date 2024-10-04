package Services.Filters

import Core.Models.{Pixel, Image}
/**
 * Trait representing a filter that can be applied to an image.
 */
trait Filter {
  /**
   * Applies the filter to the given image.
   *
   * @param image the image to which the filter is applied.
   * @return the filtered image.
   */
  def apply(image: Image[? <: Pixel]): Image[? <: Pixel]
}