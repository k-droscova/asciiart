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

class ConsoleViewModel(
                        private val asciiTableParser: AsciiTableCommandLineParser = new AsciiTableCommandLineParserImpl,
                        private val importParser: ImporterCommandLineParser = new ImporterCommandLineParserImpl,
                        private val exportParser: ExporterCommandLineParser = new ExporterCommandLineParserImpl,
                        private val filterParser: FilterCommandLineParser = new FilterCommandLineParserImpl
                      )
  extends ConsoleViewModeling {
  override def run(args: Array[String]): Unit = {
    // parse args
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
