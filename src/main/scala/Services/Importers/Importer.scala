package Services.Importers

import Core.Models.Image.RGBImage

/**
 * Trait for importing images into the application.
 * Subclasses define specific logic for loading and processing images from various sources.
 */
trait Importer {
  def importImage(): RGBImage
}
