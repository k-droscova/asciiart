package Services.ImageConvertors.AsciiConvertor

import Core.Models.AsciiTable.Linear.CustomLinearAsciiTable

class CustomLinearAsciiConvertor(customChars: String) extends AsciiConvertor(new CustomLinearAsciiTable(customChars))
