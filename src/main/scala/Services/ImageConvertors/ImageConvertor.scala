package Services.ImageConvertors

import Core.Models.Image.Image
import Core.Models.Pixel.Pixel

/**
 * A generic trait for image conversion between two image types.
 * Subclasses define specific conversion logic.
 *
 * @tparam I The input image type, which must extend `Image` with a specific subtype of `Pixel`.
 * @tparam O The output image type, which must extend `Image` with a specific subtype of `Pixel`.
 */
trait ImageConvertor[I <: Image[? <: Pixel], O <: Image[? <: Pixel]] {

  /**
   * Converts the input image of type `I` to an output image of type `O`.
   *
   * @param input The input image to be converted.
   * @return The converted output image.
   */
  def convert(input: I): O
}
