package Services.CommandLineParsers

trait CommandLineParser[T] {
  def parse(input: String): T
}
