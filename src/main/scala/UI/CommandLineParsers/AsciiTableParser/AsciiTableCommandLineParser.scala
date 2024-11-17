package UI.CommandLineParsers.AsciiTableParser

import Services.ImageConvertors.AsciiConvertor.AsciiConvertor
import UI.CommandLineParsers.CommandLineParser

/**
 * A trait for parsing command line arguments into an ASCII table converter.
 *
 * Classes implementing this trait are responsible for interpreting arguments related to ASCII table
 * selection and returning the corresponding `AsciiConvertor` for image processing.
 */
trait AsciiTableCommandLineParser extends CommandLineParser[AsciiConvertor]
