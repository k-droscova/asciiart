package Services.ImageConvertors.AsciiConvertor

import Core.Models.AsciiTable.Nonlinear.BorderedAsciiTable
class BorderedAsciiConvertor(characters: String, borders: List[Int]) extends AsciiConvertor(new BorderedAsciiTable(characters, borders))
