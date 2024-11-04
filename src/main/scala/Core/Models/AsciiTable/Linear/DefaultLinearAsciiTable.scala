package Core.Models.AsciiTable.Linear

/**
 * DefaultLinearAsciiTable provides a preset ASCII table for converting grayscale
 * values to ASCII characters. It maps grayscale values (0-255) to characters
 * `" .:-=+*#%@"` in a linear way, from darkest (`' '`) to brightest (`'@'`).
 *
 * Example:
 * {{{
 *   val table = new DefaultLinearAsciiTable()
 *   table.getAsciiCharacter(128) // Returns '='
 * }}}
 */
class DefaultLinearAsciiTable extends LinearAsciiTable(" .:-=+*#%@")
