package Services.CommandLineParser

trait CommandLineParser {
  /**
   * Parses the relevant arguments and returns an appropriate object or action.
   *
   * @param args the map of parsed arguments.
   * @return an object related to the specific command (e.g., ImageLoader, Filter, Output, etc.).
   */
  def parse(args: Map[String, String]): Option[Any]
}