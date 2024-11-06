package Services.CommandLineParsers

trait CommandLineParser[T] {
  def parse(input: String): T

  protected def splitArguments(input: String): List[String] = {
    val pattern = """(\"[^\"]*\"|\S+)""".r

    pattern.findAllIn(input).matchData.map { m =>
      m.matched
    }.toList
  }

  protected def extractQuotedInput(quotedInput: String) : String = quotedInput.stripPrefix("\"").stripSuffix("\"")
  protected def extractQuotedInputAndTrim(quotedInput: String) : String = extractQuotedInput(quotedInput).trim
}
