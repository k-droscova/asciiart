package UI.CommandLineParsers.AsciiTableParser

import Core.Errors.{BaseError, GeneralErrorCodes, LogContext}
import Core.Models.AsciiTable.Linear.{BourkeLinearAsciiTable, CustomLinearAsciiTable, DefaultLinearAsciiTable}
import Core.Models.AsciiTable.AsciiTable
import Services.ImageConvertors.AsciiConvertor.AsciiConvertor
import UI.CommandLineParsers.SingleInputCommandLineParser
import UI.CommandLineParsers.AsciiTableParser.SpecializedAsciiParsers.*
class AsciiTableCommandLineParserImpl(
                                       parsers: List[SpecializedAsciiTableCommandLineParser] = List(
                                         new CustomLinearAsciiTableCommandLineParser(),
                                         new DefaultLinearAsciiTableCommandLineParser(),
                                         new BourkeLinearAsciiTableCommandLineParser(),
                                         new BorderedAsciiTableCommandLineParser()
                                       )
                                     ) extends SingleInputCommandLineParser[AsciiConvertor](parsers) with AsciiTableCommandLineParser {

  override protected def noInputError(): BaseError = {
    BaseError(
      message = "You must specify a table type (--table=custom, --table=default, --table=bourke, or --table=bordered).",
      context = LogContext.UI,
      errorCode = GeneralErrorCodes.InvalidArgument
    )
  }

  override protected def multipleInputError(): BaseError = {
    BaseError(
      message = "Only one table type can be specified (--custom-table, --table=default, --table=bourke, or --table=bordered).",
      context = LogContext.UI,
      errorCode = GeneralErrorCodes.InvalidArgument
    )
  }
}
