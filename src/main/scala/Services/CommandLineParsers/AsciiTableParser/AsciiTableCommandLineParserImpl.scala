package Services.CommandLineParsers.AsciiTableParser

import Core.Errors.{BaseError, GeneralErrorCodes, LogSeverity, LogContext}
import Services.ImageConvertors.AsciiConvertor.*
class AsciiTableCommandLineParserImpl extends AsciiTableCommandLineParser {
  override def parse(input: String): AsciiConvertor = {
    val args = splitArguments(input)
    parseArguments(args)
  }

  private def parseArguments(args: List[String]): AsciiConvertor = {
    var customChars: Option[String] = None
    val doubleQuotePattern = """\"(.*?)\"""".r
    var tableType: Option[String] = None
    var borders: List[Int] = List()

    for (i <- args.indices) {
      args(i) match {
        case "--custom-table" =>
          if (tableType.isDefined || customChars.isDefined) {
            throw createBaseError("Only one table can be specified")
          }
          if (i + 1 < args.length) {
            val potentialChars = args(i+1)
            if (!doubleQuotePattern.matches(potentialChars))
              throw createBaseError("Custom characters must be specified in quotes after --custom-table argument.")
            customChars = Some(extractQuotedInput(potentialChars))
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
              val potentialChars = args(i + 1)
              if (!doubleQuotePattern.matches(potentialChars))
                throw createBaseError("Characters must be specified in quotes after --table=bordered argument.")
              customChars = Some(extractQuotedInput(potentialChars))
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
      case (None, None) => new DefaultLinearAsciiConvertor()
      case (None, Some("default")) => new DefaultLinearAsciiConvertor()
      case (None, Some("bourke")) => new BourkeLinearAsciiConvertor()
      case (Some(chars), Some("bordered")) =>
        println(s"Initializing Bordered with ${chars} and ${borders}")
        new BorderedAsciiConvertor(chars, borders)
      case _ => throw createBaseError("Invalid table argument.")
    }
  }

  private def parseBorders(args: List[String], currentIndex: Int): List[Int] = {
    // Regex to match the border format [int,int,...]
    val borderPattern = """^\[([+-]?\d+(,[+-]?\d+)*)?\]$""".r
    val potentialBordersInd = currentIndex + 1

    // Check if the next argument exists and matches the pattern
    if (potentialBordersInd >= args.length) {
      throw createBaseError("Border characters must be specified after --table=bordered argument.")
    } else if (!borderPattern.matches(args(potentialBordersInd))) {
      throw createBaseError("Border characters must be be in this pattern: [int,int,int,...]")
    }
    // Extract the argument and remove brackets
    val borderValuesArg = args(potentialBordersInd).stripPrefix("[").stripSuffix("]")
    // Split by commas and convert to List[Int]
    val borderValues = borderValuesArg.split(",").toList.flatMap { value =>
      val trimmedValue = value.trim.stripPrefix("+")
      trimmedValue.toIntOption match {
        case Some(intValue) => Some(intValue)
        case None => None
      }
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
