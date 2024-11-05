package Services.Importers

import Core.Models.Image.RGBImage

trait Importer {
  def importImage(): RGBImage
}
