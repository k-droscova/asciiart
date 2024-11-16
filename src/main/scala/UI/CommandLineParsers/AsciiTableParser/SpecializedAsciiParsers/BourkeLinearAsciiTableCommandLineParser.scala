package UI.CommandLineParsers.AsciiTableParser.SpecializedAsciiParsers

import Core.Errors.{BaseError, GeneralErrorCodes, LogContext}
import Core.Models.AsciiTable.Linear.BourkeLinearAsciiTable
import Services.ImageConvertors.AsciiConvertor.AsciiConvertor

class BourkeLinearAsciiTableCommandLineParser extends SpecializedAsciiTableCommandLineParser {
  override def parse(args: Array[String]): Either[BaseError, Option[AsciiConvertor]] = {
    val bourkeArgs = args.filter(_ == "--table=bourke")

    bourkeArgs match {
      case Array() => Right(None) // No `--table=bourke` argument provided
      case Array("--table=bourke") => Right(Some(new AsciiConvertor(new BourkeLinearAsciiTable())))
      case _ =>
        Left(BaseError(
          message = "The --table=bourke argument is specified multiple times.",
          context = LogContext.UI,
          errorCode = GeneralErrorCodes.InvalidArgument
        ))
    }
  }
}