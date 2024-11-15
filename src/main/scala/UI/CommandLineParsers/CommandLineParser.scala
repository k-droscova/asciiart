package UI.CommandLineParsers

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
  def parse(args: Array[String]): T
}
