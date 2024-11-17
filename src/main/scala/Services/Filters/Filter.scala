package Services.Filters

import Core.Models.Image.GrayscaleImage

/**
 * Trait representing a filter that can be applied to a GrayscaleImage.
 * Filters are responsible for transforming the pixel intensities or structure
 * of the image based on specific rules (e.g., brightness adjustment, inversion, rotation).
 */
trait Filter {

  /**
   * Applies the filter to the given GrayscaleImage and returns the resulting image.
   *
   * @param image The input GrayscaleImage to be processed.
   * @return A new GrayscaleImage after applying the filter.
   */
  def apply(image: GrayscaleImage): GrayscaleImage
}