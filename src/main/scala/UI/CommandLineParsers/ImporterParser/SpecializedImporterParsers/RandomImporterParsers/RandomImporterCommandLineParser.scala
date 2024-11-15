package UI.CommandLineParsers.ImporterParser.SpecializedImporterParsers

import Services.Importers.RandomImporters.RandomImporter
class RandomImporterCommandLineParser extends SpecializedImporterCommandLineParser {
  override def parse(args: Array[String]): Option[Importer] = {
    if (args.contains("--image-random")) Some(new RandomImporter())
    else None
  }
}
