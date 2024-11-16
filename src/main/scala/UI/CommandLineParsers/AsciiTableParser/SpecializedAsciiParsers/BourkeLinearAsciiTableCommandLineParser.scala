package UI.CommandLineParsers.AsciiTableParser.SpecializedAsciiParsers

import Core.Errors.{BaseError, GeneralErrorCodes, LogContext}
import Core.Models.AsciiTable.Linear.BourkeLinearAsciiTable

class BourkeLinearAsciiTableCommandLineParser extends SpecializedAsciiTableCommandLineParser[BourkeLinearAsciiTable] {
  override def parse(args: Array[String]): Either[BaseError, Option[BourkeLinearAsciiTable]] = {
    val bourkeArgs = args.filter(_ == "--table=bourke")

    bourkeArgs match {
      case Array() => Right(None) // No `--table=bourke` argument provided
      case Array("--table=bourke") => Right(Some(new BourkeLinearAsciiTable()))
      case _ =>
        Left(BaseError(
          message = "The --table=bourke argument is specified multiple times.",
          context = LogContext.UI,
          errorCode = GeneralErrorCodes.InvalidArgument
        ))
    }
  }
}