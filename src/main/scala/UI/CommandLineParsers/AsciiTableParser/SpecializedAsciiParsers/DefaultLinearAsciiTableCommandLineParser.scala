package UI.CommandLineParsers.AsciiTableParser.SpecializedAsciiParsers

import Core.Errors.{BaseError, GeneralErrorCodes, LogContext}
import Core.Models.AsciiTable.Linear.DefaultLinearAsciiTable
import Services.ImageConvertors.AsciiConvertor.AsciiConvertor

class DefaultLinearAsciiTableCommandLineParser extends SpecializedAsciiTableCommandLineParser {
  override def parse(args: Array[String]): Either[BaseError, Option[AsciiConvertor]] = {
    val tableArgs = args.filter(_.startsWith("--table="))

    tableArgs match {
      case Array("--table=default") => Right(Some(new AsciiConvertor(new DefaultLinearAsciiTable)))
      case Array() => Right(Some(new AsciiConvertor(new DefaultLinearAsciiTable))) // No `--table=` provided
      case args if args.count(_ == "--table=default") > 1 =>
        Left(BaseError(
          message = "The --table=default argument is specified multiple times.",
          context = LogContext.UI,
          errorCode = GeneralErrorCodes.InvalidArgument
        ))
      case _ => Right(None) // Let other parsers handle their cases
    }
  }
}
