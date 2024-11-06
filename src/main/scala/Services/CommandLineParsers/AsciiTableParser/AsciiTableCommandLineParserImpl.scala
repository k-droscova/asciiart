package Services.CommandLineParsers.AsciiTableParser

import Core.Errors.{BaseError, GeneralErrorCodes, LogSeverity, LogContext}
import Services.ImageConvertors.AsciiConvertor.*
class AsciiTableCommandLineParserImpl extends AsciiTableCommandLineParser {
  override def parse(input: String): AsciiConvertor = {
    val args = splitArgs(input)
    parseArguments(args)
  }

  private def splitArgs(input: String): List[String] = {
    // Regex to match quoted strings or unquoted words
    val pattern = """(?:"([^"]*)"|([^"\s]+))""".r

    // Find all matches in the input string
    pattern.findAllIn(input).matchData.map { m =>
      Option(m.group(1)).getOrElse(m.group(2)).trim // Extract the matched group
    }.toList
  }
  private def parseArguments(args: List[String]): AsciiConvertor = {
    var customChars: Option[String] = None
    var tableType: Option[String] = None
    var borders: List[Int] = List()

    for (i <- args.indices) {
      args(i) match {
        case "--custom-table" =>
          if (tableType.isDefined || customChars.isDefined) {
            throw createBaseError("Only one table can be specified")
          }
          if (i + 1 < args.length) {
            customChars = Some(args(i + 1))
          } else {
            throw createBaseError("Custom characters must be specified after --custom-table argument.")
          }

        case arg if arg.startsWith("--table=") =>
          if (tableType.isDefined || customChars.isDefined) {
            throw createBaseError("Only one table can be specified")
          }
          tableType = Some(arg.stripPrefix("--table="))
          if (tableType.get == "bordered") {
            if (i + 1 < args.length) {
              customChars = Some(args(i + 1))
            } else {
              throw createBaseError("Custom characters must be specified after --table=bordered argument.")
            }
            borders = parseBorders(args, i + 1)
          }

        case _ =>
      }
    }

    (customChars, tableType) match {
      case (Some(chars), None) => new CustomLinearAsciiConvertor(chars)
      case (None, Some("default")) => new DefaultLinearAsciiConvertor()
      case (None, Some("bourke")) => new BourkeLinearAsciiConvertor()
      case (Some(chars), Some("bordered")) => new BorderedAsciiConvertor(chars, borders)
      case _ => new DefaultLinearAsciiConvertor()
    }
  }

  private def parseBorders(args: List[String], currentIndex: Int): List[Int] = {
    val borderPattern = """^\[([+-]?\d+(,[+-]?\d+)*)?\]$""".r
    if (currentIndex + 1 >= args.length || !borderPattern.matches(args(currentIndex + 1))) {
      throw createBaseError("Border characters must be specified after --table=bordered argument.")
    }
    val borderValuesArg = args(currentIndex + 1).stripPrefix("[").stripSuffix("]")
    val borderValues = borderValuesArg.split(",").toList.flatMap { value =>
      val trimmedValue = value.trim.stripPrefix("+")
      if (trimmedValue.matches("""^[+-]?\d+$""")) {
        Some(trimmedValue.toInt)
      } else {
        None
      }
    }

    if (borderValues.isEmpty) {
      throw createBaseError("At least one valid border value must be specified in the format: [1,2,3,...].")
    }
    borderValues
  }

  private def createBaseError(message: String): BaseError = {
    BaseError(
      message = message,
      severity = LogSeverity.Error,
      context = LogContext.UI,
      errorCode = GeneralErrorCodes.InvalidArgument
    )
  }
}
