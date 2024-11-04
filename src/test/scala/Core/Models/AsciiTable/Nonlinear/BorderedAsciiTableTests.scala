package Core.Models.AsciiTable.Nonlinear

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.BeforeAndAfterEach
import scala.compiletime.uninitialized
import Core.Errors.{BaseError, ASCIIConversionErrorCodes, LogContext, LogSeverity}

class BorderedAsciiTableTests extends AnyFunSuite with BeforeAndAfterEach {

  var asciiTable: BorderedAsciiTable = uninitialized

  override def beforeEach(): Unit = {
    super.beforeEach()
    asciiTable = new BorderedAsciiTable(" .:-=+*#%", List(32, 64, 96, 128, 160, 192, 224, 255))
  }

  override def afterEach(): Unit = {
    super.afterEach()
    asciiTable = null
  }

  test("BorderedAsciiTable should map grayscale 0 to ' '") {
    assert(asciiTable.getAsciiCharacter(0) == ' ')
  }

  test("BorderedAsciiTable should map grayscale 31 to ' '") {
    assert(asciiTable.getAsciiCharacter(31) == ' ')
  }

  test("BorderedAsciiTable should map grayscale 32 to '.'") {
    assert(asciiTable.getAsciiCharacter(32) == '.')
  }

  test("BorderedAsciiTable should map grayscale 63 to '.'") {
    assert(asciiTable.getAsciiCharacter(63) == '.')
  }

  test("BorderedAsciiTable should map grayscale 64 to ':'") {
    assert(asciiTable.getAsciiCharacter(64) == ':')
  }

  test("BorderedAsciiTable should map grayscale 95 to ':'") {
    assert(asciiTable.getAsciiCharacter(95) == ':')
  }

  test("BorderedAsciiTable should map grayscale 96 to '-'") {
    assert(asciiTable.getAsciiCharacter(96) == '-')
  }

  test("BorderedAsciiTable should map grayscale 127 to '-'") {
    assert(asciiTable.getAsciiCharacter(127) == '-')
  }

  test("BorderedAsciiTable should map grayscale 128 to '='") {
    assert(asciiTable.getAsciiCharacter(128) == '=')
  }

  test("BorderedAsciiTable should map grayscale 159 to '='") {
    assert(asciiTable.getAsciiCharacter(159) == '=')
  }

  test("BorderedAsciiTable should map grayscale 160 to '+'") {
    assert(asciiTable.getAsciiCharacter(160) == '+')
  }

  test("BorderedAsciiTable should map grayscale 191 to '+'") {
    assert(asciiTable.getAsciiCharacter(191) == '+')
  }

  test("BorderedAsciiTable should map grayscale 192 to '*'") {
    assert(asciiTable.getAsciiCharacter(192) == '*')
  }

  test("BorderedAsciiTable should map grayscale 223 to '*'") {
    assert(asciiTable.getAsciiCharacter(223) == '*')
  }

  test("BorderedAsciiTable should map grayscale 224 to '#'") {
    assert(asciiTable.getAsciiCharacter(224) == '#')
  }

  test("BorderedAsciiTable should map grayscale 254 to '#'") {
    assert(asciiTable.getAsciiCharacter(254) == '#')
  }

  test("BorderedAsciiTable should map grayscale 255 to '%'") {
    assert(asciiTable.getAsciiCharacter(255) == '%')
  }

  test("BorderedAsciiTable should throw an error if characters string is empty") {
    val thrown = intercept[BaseError] {
      new BorderedAsciiTable("", List())
    }
    assert(thrown.errorCode == ASCIIConversionErrorCodes.InvalidTable)
  }

  test("BorderedAsciiTable should be valid with one character and no borders") {
    asciiTable = new BorderedAsciiTable(" ", List())
    assert(asciiTable.getAsciiCharacter(0) == ' ')
    assert(asciiTable.getAsciiCharacter(255) == ' ')
  }

  test("BorderedAsciiTable should throw an error if two characters are provided but no border") {
    val thrown = intercept[BaseError] {
      new BorderedAsciiTable(" .", List())
    }
    assert(thrown.errorCode == ASCIIConversionErrorCodes.InvalidTable)
  }

  test("BorderedAsciiTable should throw an error if borders list length is less than characters length - 1") {
    val thrown = intercept[BaseError] {
      new BorderedAsciiTable(" .:", List(32))
    }
    assert(thrown.errorCode == ASCIIConversionErrorCodes.InvalidTable)
  }

  test("BorderedAsciiTable should be valid if borders list length is exactly characters length - 1") {
    asciiTable = new BorderedAsciiTable(" .:", List(32, 64))
    assert(asciiTable.getAsciiCharacter(0) == ' ')
    assert(asciiTable.getAsciiCharacter(32) == '.')
    assert(asciiTable.getAsciiCharacter(64) == ':')
  }

  test("BorderedAsciiTable should throw an error if borders list is longer than characters length - 1") {
    val thrown = intercept[BaseError] {
      new BorderedAsciiTable(" .:-=", List(32, 64, 96, 128, 160))
    }
    assert(thrown.errorCode == ASCIIConversionErrorCodes.InvalidTable)
  }

  test("BorderedAsciiTable should be valid if borders length matches characters length - 1 for five characters") {
    asciiTable = new BorderedAsciiTable(" .:-=", List(32, 64, 96, 128))
    assert(asciiTable.getAsciiCharacter(0) == ' ')
    assert(asciiTable.getAsciiCharacter(32) == '.')
    assert(asciiTable.getAsciiCharacter(64) == ':')
    assert(asciiTable.getAsciiCharacter(96) == '-')
    assert(asciiTable.getAsciiCharacter(128) == '=')
  }

  test("BorderedAsciiTable should throw an error if any border is out of the 0-255 range") {
    val thrown = intercept[BaseError] {
      new BorderedAsciiTable(" .:", List(32, 300)) // Invalid: 300 is out of range
    }
    assert(thrown.errorCode == ASCIIConversionErrorCodes.InvalidTable)
    assert(thrown.message.contains("all borders must be within the 0-255 range"))
  }
}
