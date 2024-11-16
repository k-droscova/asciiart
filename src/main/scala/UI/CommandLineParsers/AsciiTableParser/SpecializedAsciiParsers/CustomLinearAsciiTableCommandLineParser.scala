package UI.CommandLineParsers.AsciiTableParser.SpecializedAsciiParsers

import Core.Errors.{BaseError, GeneralErrorCodes, LogContext}
import Core.Models.AsciiTable.Linear.CustomLinearAsciiTable
import Services.ImageConvertors.AsciiConvertor.AsciiConvertor

class CustomLinearAsciiTableCommandLineParser extends SpecializedAsciiTableCommandLineParser {
  override def parse(args: Array[String]): Either[BaseError, Option[AsciiConvertor]] = {
    val customIndices = args.zipWithIndex.collect { case ("--custom-table", index) => index }

    customIndices.length match {
      case 0 =>
        // No `--custom-table` argument
        Right(None)
      case 1 =>
        // Exactly one `--custom-table` argument, validate the next value
        val customIndex = customIndices.head
        if (customIndex + 1 >= args.length || args(customIndex + 1).startsWith("--")) {
          Left(BaseError(
            message = "Custom characters must be specified after --custom-table argument.",
            context = LogContext.UI,
            errorCode = GeneralErrorCodes.InvalidArgument
          ))
        } else {
          // Valid argument follows, create the table
          val chars = args(customIndex + 1).trim
          Right(Some(new AsciiConvertor(new CustomLinearAsciiTable(chars))))
        }
      case _ =>
        // More than one `--custom-table` argument
        Left(BaseError(
          message = "Only one --custom-table argument is allowed.",
          context = LogContext.UI,
          errorCode = GeneralErrorCodes.InvalidArgument
        ))
    }
  }
}
