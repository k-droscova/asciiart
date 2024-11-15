package UI

import Core.Models.Image.{AsciiImage, GrayscaleImage, RGBImage}
import Services.Exporters.Exporter
import Services.Filters.Filter
import Services.ImageConvertors.AsciiConvertor.AsciiConvertor
import Services.ImageConvertors.GrayscaleConvertor.LinearGrayscaleConvertor
import Services.Importers.Importer
import UI.CommandLineParsers.AsciiTableParser.AsciiTableCommandLineParserImpl
import UI.CommandLineParsers.ExporterParser.ExporterCommandLineParserImpl
import UI.CommandLineParsers.FilterParser.FilterCommandLineParserImpl
import UI.CommandLineParsers.ImporterParser.ImporterCommandLineParserImpl

class ConsoleViewModel(
                        private val asciiTableParser: AsciiTableCommandLineParserImpl = new AsciiTableCommandLineParserImpl,
                        private val importParser: ImporterCommandLineParserImpl = new ImporterCommandLineParserImpl,
                        private val exportParser: ExporterCommandLineParserImpl = new ExporterCommandLineParserImpl,
                        private val filterParser: FilterCommandLineParserImpl = new FilterCommandLineParserImpl,
                        private val grayscaleConvertor: LinearGrayscaleConvertor = new LinearGrayscaleConvertor
                      )
  extends ConsoleViewModeling {
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
