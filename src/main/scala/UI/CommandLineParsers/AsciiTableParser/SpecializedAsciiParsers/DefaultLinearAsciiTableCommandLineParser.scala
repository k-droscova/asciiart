package UI.CommandLineParsers.AsciiTableParser.SpecializedAsciiParsers

import Core.Errors.{BaseError, GeneralErrorCodes, LogContext}
import Core.Models.AsciiTable.Linear.DefaultLinearAsciiTable

class DefaultLinearAsciiTableCommandLineParser extends SpecializedAsciiTableCommandLineParser[DefaultLinearAsciiTable] {
  override def parse(args: Array[String]): Either[BaseError, Option[DefaultLinearAsciiTable]] = {
    val tableArgs = args.filter(_.startsWith("--table="))

    tableArgs match {
      case Array("--table=default") => Right(Some(new DefaultLinearAsciiTable()))
      case Array() => Right(Some(new DefaultLinearAsciiTable())) // No `--table=` provided
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
