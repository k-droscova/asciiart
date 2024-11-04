package Core.Models.AsciiTable.Linear

import Core.Models.AsciiTable.AsciiTable

abstract class LinearAsciiTable(val characters: String) extends AsciiTable {
  private val range: Int = Math.ceil(256.0 / characters.length).toInt

  def getAsciiCharacter(grayscale: Int): Char = {
    characters(Math.min(grayscale / range, characters.length - 1))
  }
}