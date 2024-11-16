package UI.CommandLineParsers.AsciiTableParser.SpecializedAsciiParsers

import Core.Errors.{BaseError, GeneralErrorCodes, LogContext}
import Core.Models.AsciiTable.Nonlinear.BorderedAsciiTable
import Services.ImageConvertors.AsciiConvertor.AsciiConvertor

class BorderedAsciiTableCommandLineParser extends SpecializedAsciiTableCommandLineParser {

  override def parse(args: Array[String]): Either[BaseError, Option[AsciiConvertor]] = {
    val borderedArgs = args.zipWithIndex.filter(_._1.startsWith("--table=bordered"))

    borderedArgs match {
      case Array() => Right(None) // No `--table=bordered` argument provided
      case Array((_, index)) =>
        parseBorderedTable(args, index)
      case _ =>
        Left(BaseError(
          message = "Only one --table=bordered argument is allowed.",
          context = LogContext.UI,
          errorCode = GeneralErrorCodes.InvalidArgument
        ))
    }
  }

  private def parseBorderedTable(args: Array[String], index: Int): Either[BaseError, Option[AsciiConvertor]] = {
    // Ensure there are characters after `--table=bordered`
    if (index + 1 >= args.length || args(index + 1).startsWith("--")) {
      return Left(BaseError(
        message = "Custom characters must be specified after --table=bordered argument.",
        context = LogContext.UI,
        errorCode = GeneralErrorCodes.InvalidArgument
      ))
    }

    val characters = args(index + 1)

    // Ensure there are borders specified after the characters
    if (index + 2 >= args.length || !args(index + 2).matches("""^\[([+-]?\d+(,[+-]?\d+)*)?\]$""")) {
      return Left(BaseError(
        message = "Borders must be specified after the characters in the format [int,int,...].",
        context = LogContext.UI,
        errorCode = GeneralErrorCodes.InvalidArgument
      ))
    }

    val bordersString = args(index + 2).stripPrefix("[").stripSuffix("]")
    val borders = bordersString.split(",").toList.flatMap { value =>
      value.stripPrefix("+").toIntOption
    }

    Right(Some(new AsciiConvertor(new BorderedAsciiTable(characters, borders))))
  }
}
