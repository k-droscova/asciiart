package Services.Exporters

import Core.Models.Image.AsciiImage

/**
 * Trait defining the contract for exporting ASCII images.
 * Implementations of this trait specify how and where the image should be exported,
 * such as to the console or to a file.
 */
trait Exporter {

  /**
   * Exports the given ASCII image.
   *
   * @param image The ASCII image to be exported.
   */
  def exportImage(image: AsciiImage): Unit
}
