package Core.Models.AsciiTable.Linear

import Core.Models.AsciiTable.AsciiTable

/**
 * LinearAsciiTable maps grayscale values (0-255) to ASCII characters linearly. This class is abstract,
 * so different ASCII styles can be set in subclasses by changing the characters used.
 *
 * @param characters A string of ASCII characters for mapping. Each character represents a section
 *                   of the grayscale range.
 *
 * Example:
 * {{{
 *   val table = new DefaultLinearAsciiTable()
 *   table.getAsciiCharacter(128) // Returns the ASCII char for mid-range grayscale
 * }}}
 */
abstract class LinearAsciiTable(val characters: String) extends AsciiTable {
  private val range: Int = Math.ceil(256.0 / characters.length).toInt

  /**
   * Gets the ASCII character for a given grayscale value.
   *
   * @param grayscale Grayscale value (0-255)
   * @return ASCII character that best represents this grayscale
   */
  def getAsciiCharacter(grayscale: Int): Char = {
    characters(Math.min(grayscale / range, characters.length - 1))
  }
}