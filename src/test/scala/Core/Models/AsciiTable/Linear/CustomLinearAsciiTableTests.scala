package Core.Models.AsciiTable.Linear

import Core.Errors.{ASCIIConversionErrorCodes, BaseError, LogContext}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite

import scala.compiletime.uninitialized

class CustomLinearAsciiTableTests extends AnyFunSuite with BeforeAndAfterEach {
  var customTable: CustomLinearAsciiTable = uninitialized

  override def beforeEach(): Unit = {
    customTable = new CustomLinearAsciiTable("abcdefghijklmnopqrstuvwxyz")
  }

  override def afterEach(): Unit = {
    customTable = null
  }

  test("CustomLinearAsciiTableTests should map grayscale value 0 to 'a'") {
    assert(customTable.getAsciiCharacter(0) == 'a')
  }

  test("CustomLinearAsciiTableTests should map grayscale value 9 to 'a'") {
    assert(customTable.getAsciiCharacter(9) == 'a')
  }

  test("CustomLinearAsciiTableTests should map grayscale value 10 to 'b'") {
    assert(customTable.getAsciiCharacter(10) == 'b')
  }

  test("CustomLinearAsciiTableTests should map grayscale value 19 to 'b'") {
    assert(customTable.getAsciiCharacter(19) == 'b')
  }

  test("CustomLinearAsciiTableTests should map grayscale value 20 to 'c'") {
    assert(customTable.getAsciiCharacter(20) == 'c')
  }

  test("CustomLinearAsciiTableTests should map grayscale value 29 to 'c'") {
    assert(customTable.getAsciiCharacter(29) == 'c')
  }

  test("CustomLinearAsciiTableTests should map grayscale value 30 to 'd'") {
    assert(customTable.getAsciiCharacter(30) == 'd')
  }

  test("CustomLinearAsciiTableTests should map grayscale value 39 to 'd'") {
    assert(customTable.getAsciiCharacter(39) == 'd')
  }

  test("CustomLinearAsciiTableTests should map grayscale value 40 to 'e'") {
    assert(customTable.getAsciiCharacter(40) == 'e')
  }

  test("CustomLinearAsciiTableTests should map grayscale value 49 to 'e'") {
    assert(customTable.getAsciiCharacter(49) == 'e')
  }

  test("CustomLinearAsciiTableTests should map grayscale value 50 to 'f'") {
    assert(customTable.getAsciiCharacter(50) == 'f')
  }

  test("CustomLinearAsciiTableTests should map grayscale value 59 to 'f'") {
    assert(customTable.getAsciiCharacter(59) == 'f')
  }

  test("CustomLinearAsciiTableTests should map grayscale value 60 to 'g'") {
    assert(customTable.getAsciiCharacter(60) == 'g')
  }

  test("CustomLinearAsciiTableTests should map grayscale value 69 to 'g'") {
    assert(customTable.getAsciiCharacter(69) == 'g')
  }

  test("CustomLinearAsciiTableTests should map grayscale value 70 to 'h'") {
    assert(customTable.getAsciiCharacter(70) == 'h')
  }

  test("CustomLinearAsciiTableTests should map grayscale value 79 to 'h'") {
    assert(customTable.getAsciiCharacter(79) == 'h')
  }

  test("CustomLinearAsciiTableTests should map grayscale value 80 to 'i'") {
    assert(customTable.getAsciiCharacter(80) == 'i')
  }

  test("CustomLinearAsciiTableTests should map grayscale value 89 to 'i'") {
    assert(customTable.getAsciiCharacter(89) == 'i')
  }

  test("CustomLinearAsciiTableTests should map grayscale value 90 to 'j'") {
    assert(customTable.getAsciiCharacter(90) == 'j')
  }

  test("CustomLinearAsciiTableTests should map grayscale value 99 to 'j'") {
    assert(customTable.getAsciiCharacter(99) == 'j')
  }

  test("CustomLinearAsciiTableTests should map grayscale value 100 to 'k'") {
    assert(customTable.getAsciiCharacter(100) == 'k')
  }

  test("CustomLinearAsciiTableTests should map grayscale value 109 to 'k'") {
    assert(customTable.getAsciiCharacter(109) == 'k')
  }

  test("CustomLinearAsciiTableTests should map grayscale value 110 to 'l'") {
    assert(customTable.getAsciiCharacter(110) == 'l')
  }

  test("CustomLinearAsciiTableTests should map grayscale value 119 to 'l'") {
    assert(customTable.getAsciiCharacter(119) == 'l')
  }

  test("CustomLinearAsciiTableTests should map grayscale value 120 to 'm'") {
    assert(customTable.getAsciiCharacter(120) == 'm')
  }

  test("CustomLinearAsciiTableTests should map grayscale value 129 to 'm'") {
    assert(customTable.getAsciiCharacter(129) == 'm')
  }

  test("CustomLinearAsciiTableTests should map grayscale value 130 to 'n'") {
    assert(customTable.getAsciiCharacter(130) == 'n')
  }

  test("CustomLinearAsciiTableTests should map grayscale value 139 to 'n'") {
    assert(customTable.getAsciiCharacter(139) == 'n')
  }

  test("CustomLinearAsciiTableTests should map grayscale value 140 to 'o'") {
    assert(customTable.getAsciiCharacter(140) == 'o')
  }

  test("CustomLinearAsciiTableTests should map grayscale value 149 to 'o'") {
    assert(customTable.getAsciiCharacter(149) == 'o')
  }

  test("CustomLinearAsciiTableTests should map grayscale value 150 to 'p'") {
    assert(customTable.getAsciiCharacter(150) == 'p')
  }

  test("CustomLinearAsciiTableTests should map grayscale value 159 to 'p'") {
    assert(customTable.getAsciiCharacter(159) == 'p')
  }

  test("CustomLinearAsciiTableTests should map grayscale value 160 to 'q'") {
    assert(customTable.getAsciiCharacter(160) == 'q')
  }

  test("CustomLinearAsciiTableTests should map grayscale value 169 to 'q'") {
    assert(customTable.getAsciiCharacter(169) == 'q')
  }

  test("CustomLinearAsciiTableTests should map grayscale value 170 to 'r'") {
    assert(customTable.getAsciiCharacter(170) == 'r')
  }

  test("CustomLinearAsciiTableTests should map grayscale value 179 to 'r'") {
    assert(customTable.getAsciiCharacter(179) == 'r')
  }

  test("CustomLinearAsciiTableTests should map grayscale value 180 to 's'") {
    assert(customTable.getAsciiCharacter(180) == 's')
  }

  test("CustomLinearAsciiTableTests should map grayscale value 189 to 's'") {
    assert(customTable.getAsciiCharacter(189) == 's')
  }

  test("CustomLinearAsciiTableTests should map grayscale value 190 to 't'") {
    assert(customTable.getAsciiCharacter(190) == 't')
  }

  test("CustomLinearAsciiTableTests should map grayscale value 199 to 't'") {
    assert(customTable.getAsciiCharacter(199) == 't')
  }

  test("CustomLinearAsciiTableTests should map grayscale value 200 to 'u'") {
    assert(customTable.getAsciiCharacter(200) == 'u')
  }

  test("CustomLinearAsciiTableTests should map grayscale value 209 to 'u'") {
    assert(customTable.getAsciiCharacter(209) == 'u')
  }

  test("CustomLinearAsciiTableTests should map grayscale value 210 to 'v'") {
    assert(customTable.getAsciiCharacter(210) == 'v')
  }

  test("CustomLinearAsciiTableTests should map grayscale value 219 to 'v'") {
    assert(customTable.getAsciiCharacter(219) == 'v')
  }

  test("CustomLinearAsciiTableTests should map grayscale value 220 to 'w'") {
    assert(customTable.getAsciiCharacter(220) == 'w')
  }

  test("CustomLinearAsciiTableTests should map grayscale value 229 to 'w'") {
    assert(customTable.getAsciiCharacter(229) == 'w')
  }

  test("CustomLinearAsciiTableTests should map grayscale value 230 to 'x'") {
    assert(customTable.getAsciiCharacter(230) == 'x')
  }

  test("CustomLinearAsciiTableTests should map grayscale value 239 to 'x'") {
    assert(customTable.getAsciiCharacter(239) == 'x')
  }

  test("CustomLinearAsciiTableTests should map grayscale value 240 to 'y'") {
    assert(customTable.getAsciiCharacter(240) == 'y')
  }

  test("CustomLinearAsciiTableTests should map grayscale value 249 to 'y'") {
    assert(customTable.getAsciiCharacter(249) == 'y')
  }

  test("CustomLinearAsciiTableTests should map grayscale value 250 to 'z'") {
    assert(customTable.getAsciiCharacter(250) == 'z')
  }

  test("CustomLinearAsciiTableTests should map grayscale value 255 to 'z'") {
    assert(customTable.getAsciiCharacter(255) == 'z')
  }

  test("CustomLinearAsciiTable should throw an error if characters are empty") {
    val thrown = intercept[BaseError] {
      new CustomLinearAsciiTable("")
    }
    assert(thrown.errorCode == ASCIIConversionErrorCodes.InvalidTable)
  }
}