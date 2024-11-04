package Core.Models.AsciiTable

trait AsciiTable {
  /**
   * Maps a grayscale intensity (0-255) to an ASCII character.
   *
   * @param grayscale The grayscale intensity (0-255).
   * @return The ASCII character corresponding to the grayscale intensity.
   */
  def getAsciiCharacter(grayscale: Int): Char
}
