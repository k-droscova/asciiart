package Services.Exporters

import Core.Models.Image.AsciiImage
trait Exporter {
  def exportImage(image: AsciiImage): Unit
}
