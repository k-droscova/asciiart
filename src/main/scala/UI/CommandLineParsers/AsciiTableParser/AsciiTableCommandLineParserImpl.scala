package UI.CommandLineParsers.AsciiTableParser

import Core.Errors.{BaseError, GeneralErrorCodes, LogContext}
import Services.ImageConvertors.AsciiConvertor.*

/**
 * AsciiTableCommandLineParserImpl is responsible for parsing command line input related to
 * ASCII table conversions and returning the appropriate AsciiConvertor based on user specifications.
 */
class AsciiTableCommandLineParserImpl extends AsciiTableCommandLineParser {

  /**
   * Parses the input string containing command line arguments and returns the corresponding AsciiConvertor.
   *
   * Supported arguments:
   * - `--custom-table "custom characters"`: Specifies a custom ASCII character set for the conversion.
   * - `--table=default`: Uses the default ASCII table for conversion.
   * - `--table=bourke`: Uses the Bourke ASCII table for conversion.
   * - `--table=bordered "custom characters" [int,int,...]`: Specifies a bordered ASCII table with a custom character set and border values.
   *
   * @param input A string representing the command line arguments.
   * @return An instance of AsciiConvertor based on the parsed arguments.
   * @throws BaseError if:
   *   - More than one table type is specified (e.g., both `--custom-table` and `--table=default`).
   *   - Invalid table type is provided (e.g., `--table=invalid`).
   *   - Missing characters after `--custom-table` argument.
   *   - Missing characters after `--table=bordered` argument.
   *   - Invalid format for border values (should be in the format: [int,int,...]).
   */
  override def parse(args: Array[String]): AsciiConvertor = {
    parseArguments(args)
  }

  /**
   * Processes the list of parsed arguments to determine the appropriate AsciiConvertor to create.
   *
   * @param args A list of strings representing the parsed command line arguments.
   * @return An instance of AsciiConvertor.
   * @throws BaseError if more than one table type is specified or if necessary arguments are missing.
   */
  private def parseArguments(args: Array[String]): AsciiConvertor = {
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
            customChars = Some(args(i+1))
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
              customChars = Some(args(i+1))
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
        new BorderedAsciiConvertor(chars, borders)
      case _ => throw createBaseError("Invalid table argument.")
    }
  }

  /**
   * Parses border values from the command line arguments for the BorderedAsciiConvertor
   *
   * @param args         A list of strings representing the parsed command line arguments.
   * @param currentIndex The index where the border values are expected.
   * @return A list of integers representing the border values.
   * @throws BaseError if the border argument is malformed or not specified.
   */
  private def parseBorders(args: Array[String], currentIndex: Int): List[Int] = {
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
      val trimmedValue = value.stripPrefix("+")
      trimmedValue.toIntOption match {
        case Some(intValue) => Some(intValue)
        case None => None
      }
    }
    borderValues
  }

  private def createBaseError(message: String): BaseError = {
    BaseError(message = message, context = LogContext.UI, errorCode = GeneralErrorCodes.InvalidArgument)
  }
}
