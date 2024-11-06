package Services.CommandLineParsers

/**
 * A trait for parsing command line arguments into a specified type.
 *
 * This trait defines a generic interface for parsing command line input. Implementing classes
 * should provide specific logic to handle different types of command line arguments and return
 * the appropriate type specified by the type parameter `T`.
 *
 * @tparam T The type that will be returned by the parser.
 */
trait CommandLineParser[T] {

  /**
   * Parses the input string containing command line arguments and returns the corresponding value of type T.
   *
   * @param input A string representing the command line arguments. The input can include various options
   *              that need to be parsed based on the implementation.
   * @return An instance of type T based on the parsed arguments.
   */
  def parse(input: String): T

  /**
   * Splits the input string into individual arguments, preserving quoted strings.
   *
   * @param input The command line input string to be split.
   * @return A list of strings representing the individual arguments, including quoted arguments as single entries.
   */
  protected def splitArguments(input: String): List[String] = {
    val pattern = """(\"[^\"]*\"|\S+)""".r

    pattern.findAllIn(input).matchData.map { m =>
      m.matched
    }.toList
  }

  /**
   * Extracts the inner content of a quoted string by removing the surrounding quotes.
   *
   * @param quotedInput The input string that is expected to be enclosed in quotes.
   * @return The content of the quoted string without the surrounding quotes.
   */
  protected def extractQuotedInput(quotedInput: String) : String = quotedInput.stripPrefix("\"").stripSuffix("\"")

  /**
   * Extracts the inner content of a quoted string, trimming any leading or trailing whitespace.
   *
   * @param quotedInput The input string that is expected to be enclosed in quotes.
   * @return The content of the quoted string without the surrounding quotes and trimmed of whitespace.
   */
  protected def extractQuotedInputAndTrim(quotedInput: String) : String = extractQuotedInput(quotedInput).trim
}
