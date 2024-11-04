package Services.ImageConvertors

import Core.Models.Image.Image
import Core.Models.Pixel.Pixel

trait ImageConvertor[I <: Image[? <: Pixel], O <: Image[? <: Pixel]] {
  def convert(input: I): O
}
