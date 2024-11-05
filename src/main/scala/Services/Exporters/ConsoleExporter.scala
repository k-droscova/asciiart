package Services.Exporters

import Core.Models.Image.AsciiImage

/**
 * ConsoleExporter is responsible for exporting ASCII images to the console.
 * It prints the ASCII representation of the image directly to the standard output.
 */
class ConsoleExporter extends Exporter {

  /**
   * Exports the provided ASCII image by printing its content to the console.
   *
   * @param image The ASCII image to be exported.
   */
  override def exportImage(image: AsciiImage): Unit = {
    for (y <- 0 until image.getHeight) {
      val line = (0 until image.getWidth).map(x => image.getPixel(x, y).char).mkString("")
      println(line)
    }
  }
}