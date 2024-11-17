package UI.CommandLineParsers.AsciiTableParser.SpecializedAsciiParsers

import Services.ImageConvertors.AsciiConvertor.AsciiConvertor
import UI.CommandLineParsers.SpecializedCommandLineParser

/**
 * A specialized parser for ASCII table arguments.
 *
 * This trait extends `SpecializedCommandLineParser` and defines the contract
 * for parsers that handle specific ASCII table configurations.
 */
trait SpecializedAsciiTableCommandLineParser extends SpecializedCommandLineParser[AsciiConvertor]