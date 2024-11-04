package Core.Models.AsciiTable.Linear

import Core.Errors.{BaseError, ASCIIConversionErrorCodes, LogContext, LogSeverity}

/**
 * CustomLinearAsciiTable allows for a customizable ASCII conversion table.
 *
 * @param customChars A non-empty string of ASCII characters to be used for mapping grayscale values.
 * @throws BaseError If `customChars` is empty.
 */
class CustomLinearAsciiTable(customChars: String) extends LinearAsciiTable(customChars) {
  if (customChars.isEmpty) {
    throw BaseError(
      message = "Custom ASCII characters cannot be empty.",
      severity = LogSeverity.Error,
      context = LogContext.UI,
      errorCode = ASCIIConversionErrorCodes.InvalidTable
    )
  }
}