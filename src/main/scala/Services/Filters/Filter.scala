package Services.Filters

import Core.Models.Image.GrayscaleImage

trait Filter {
  def apply(image: GrayscaleImage): GrayscaleImage
}