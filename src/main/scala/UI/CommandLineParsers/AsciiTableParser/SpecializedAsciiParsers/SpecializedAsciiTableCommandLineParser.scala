package UI.CommandLineParsers.AsciiTableParser.SpecializedAsciiParsers

import Core.Models.AsciiTable.AsciiTable
import UI.CommandLineParsers.SpecializedCommandLineParser
trait SpecializedAsciiTableCommandLineParser[T <: AsciiTable] extends SpecializedCommandLineParser[T] {}
