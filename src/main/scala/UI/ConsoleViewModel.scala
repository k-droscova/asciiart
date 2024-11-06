package UI

import Core.Models.Image.{GrayscaleImage, RGBImage, AsciiImage}
import Services.CommandLineParsers.AsciiTableParser.AsciiTableCommandLineParserImpl
import Services.CommandLineParsers.ExporterParser.ExporterCommandLineParserImpl
import Services.CommandLineParsers.FilterParser.FilterCommandLineParserImpl
import Services.CommandLineParsers.ImporterParser.ImporterCommandLineParserImpl
import Services.Importers.Importer
import Services.Exporters.Exporter
import Services.Filters.Filter
import Services.ImageConvertors.AsciiConvertor.AsciiConvertor
import Services.ImageConvertors.GrayscaleConvertor.LinearGrayscaleConvertor
class ConsoleViewModel extends ConsoleViewModeling {
  private val asciiTableParser = new AsciiTableCommandLineParserImpl
  private val importParser = new ImporterCommandLineParserImpl
  private val exportParser = new ExporterCommandLineParserImpl
  private val filterParser = new FilterCommandLineParserImpl
  private val grayscaleConvertor = new LinearGrayscaleConvertor
  override def run(args: Array[String]): Unit = {
    // parse args
    val importer: Importer = importParser.parse(args)
    val ascii: AsciiConvertor = asciiTableParser.parse(args)
    val filters: List[Filter] = filterParser.parse(args)
    val exporter: Exporter = exportParser.parse(args)

    // import image
    val rgbImage: RGBImage = importer.importImage()
    // convert to grayscale
    val grayscaleImage: GrayscaleImage = grayscaleConvertor.convert(rgbImage)
    // apply filters
    val filteredImage: GrayscaleImage = filters.foldLeft(grayscaleImage) { (currentImage, filter) =>
      filter.apply(currentImage)
    }
    // convert to ascii
    val asciiImage: AsciiImage = ascii.convert(filteredImage)
    // export
    exporter.exportImage(asciiImage)
  }
}
