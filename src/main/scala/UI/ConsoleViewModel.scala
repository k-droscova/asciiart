package UI

import Business.{ImageProcessor, ImageProcessorImpl}
import Services.Exporters.Exporter
import Services.Filters.Filter
import Services.ImageConvertors.AsciiConvertor.AsciiConvertor
import Services.Importers.Importer
import UI.CommandLineParsers.AsciiTableParser.{AsciiTableCommandLineParser, AsciiTableCommandLineParserImpl}
import UI.CommandLineParsers.ExporterParser.{ExporterCommandLineParser, ExporterCommandLineParserImpl}
import UI.CommandLineParsers.FilterParser.{FilterCommandLineParser, FilterCommandLineParserImpl}
import UI.CommandLineParsers.ImporterParser.{ImporterCommandLineParser, ImporterCommandLineParserImpl}

/**
 * The ConsoleViewModel class acts as the ViewModel for the ConsoleView. It is responsible
 * for parsing command-line arguments and then delegates the business logic to ImageProcessor.
 * This class implements the ConsoleViewModeling trait.
 *
 * @param asciiTableParser Parses arguments for selecting the ASCII table used in image conversion.
 *                         Defaults to AsciiTableCommandLineParserImpl.
 * @param importParser     Parses arguments for selecting the image importer. Defaults to ImporterCommandLineParserImpl.
 * @param exportParser     Parses arguments for selecting the image exporter. Defaults to ExporterCommandLineParserImpl.
 * @param filterParser     Parses arguments for applying image filters. Defaults to FilterCommandLineParserImpl.
 */
class ConsoleViewModel(
                        private val asciiTableParser: AsciiTableCommandLineParser = new AsciiTableCommandLineParserImpl,
                        private val importParser: ImporterCommandLineParser = new ImporterCommandLineParserImpl,
                        private val exportParser: ExporterCommandLineParser = new ExporterCommandLineParserImpl,
                        private val filterParser: FilterCommandLineParser = new FilterCommandLineParserImpl
                      )
  extends ConsoleViewModeling {
  override def run(args: Array[String]): Unit = {
    val importer: Importer = importParser.parse(args)
    val ascii: AsciiConvertor = asciiTableParser.parse(args)
    val filters: List[Filter] = filterParser.parse(args)
    val exporter: Exporter = exportParser.parse(args)

    val processor: ImageProcessor = new ImageProcessorImpl(
      importer = importer,
      filters = filters,
      asciiConvertor = ascii,
      exporter = exporter
    )
    processor.processImage()
  }
}
