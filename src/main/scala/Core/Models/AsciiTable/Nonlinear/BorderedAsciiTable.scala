package Core.Models.AsciiTable.Nonlinear

import Core.Models.AsciiTable.AsciiTable
import Core.Errors.{BaseError, ASCIIConversionErrorCodes, LogContext, LogSeverity}

/**
 * The BorderedAsciiTable class maps grayscale values to ASCII characters based on predefined
 * border thresholds for each character. Each character corresponds to a specific grayscale range.
 *
 * @param characters A string where each character represents an ASCII symbol for a grayscale range.
 * @param borders    A list of grayscale values that serve as upper bounds for each character range.
 *                   This list should have a length of `characters.length - 1`, defining the thresholds.
 * @throws BaseError If the number of borders does not match `characters.length - 1`,
 *                   if `characters` is empty, or if any border is out of the 0-255 range.
 */
class BorderedAsciiTable(characters: String, borders: List[Int]) extends AsciiTable {

  if (characters.isEmpty) {
    throw BaseError(
      message = "Invalid Bordered Ascii Table: characters string cannot be empty.",
      severity = LogSeverity.Error,
      context = LogContext.UI,
      errorCode = ASCIIConversionErrorCodes.InvalidTable
    )
  }

  if (borders.length != characters.length - 1) {
    throw BaseError(
      message = s"Invalid arguments for Bordered Ascii Table: character length ${characters.length} doesn't match expected borders length: ${borders.length + 1}",
      severity = LogSeverity.Error,
      context = LogContext.UI,
      errorCode = ASCIIConversionErrorCodes.InvalidTable
    )
  }

  if (borders.exists(border => border < 0 || border > 255)) {
    throw BaseError(
      message = "Invalid Bordered Ascii Table: all borders must be within the 0-255 range.",
      severity = LogSeverity.Error,
      context = LogContext.UI,
      errorCode = ASCIIConversionErrorCodes.InvalidTable
    )
  }

  /**
   * Maps a grayscale value to a character based on the predefined borders.
   * For grayscale values less than the first border, the first character is used;
   * for values greater than the last border, the last character is used.
   */
  def getAsciiCharacter(grayscale: Int): Char = {
    val index = borders.indexWhere(grayscale < _) match {
      case -1 => characters.length - 1
      case i => i
    }
    characters(index)
  }
}
