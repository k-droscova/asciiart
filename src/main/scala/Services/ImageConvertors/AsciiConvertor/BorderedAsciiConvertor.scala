package Services.ImageConvertors.AsciiConvertor

import Core.Models.AsciiTable.Nonlinear.BorderedAsciiTable
class BorderedAsciiConvertor(val characters: String, val borders: List[Int]) extends AsciiConvertor(new BorderedAsciiTable(characters, borders))
