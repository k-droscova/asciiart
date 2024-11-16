package UI.CommandLineParsers.AsciiTableParser.SpecializedAsciiParsers

import Core.Errors.{BaseError, GeneralErrorCodes, LogContext}
import Core.Models.AsciiTable.Linear.CustomLinearAsciiTable
import Services.ImageConvertors.AsciiConvertor.AsciiConvertor

class CustomLinearAsciiTableCommandLineParser extends SpecializedAsciiTableCommandLineParser {
  override def parse(args: Array[String]): Either[BaseError, Option[AsciiConvertor]] = {
    // Find the indices of `--table=custom`
    val customIndices = args.zipWithIndex.collect { case (arg, index) if arg == "--table=custom" => index }

    customIndices.length match {
      case 0 =>
        // No `--table=custom` argument
        Right(None)
      case 1 =>
        // Exactly one `--table=custom` argument, validate the next value
        val customIndex = customIndices.head
        if (customIndex + 1 >= args.length || args(customIndex + 1).startsWith("--")) {
          Left(BaseError(
            message = "Custom characters must be specified after --table=custom.",
            context = LogContext.UI,
            errorCode = GeneralErrorCodes.InvalidArgument
          ))
        } else {
          // Valid argument follows, create the table
          val chars = args(customIndex + 1).trim
          Right(Some(new AsciiConvertor(new CustomLinearAsciiTable(chars))))
        }
      case _ =>
        // More than one `--table=custom` argument
        Left(BaseError(
          message = "Only one --table=custom argument is allowed.",
          context = LogContext.UI,
          errorCode = GeneralErrorCodes.InvalidArgument
        ))
    }
  }
}
