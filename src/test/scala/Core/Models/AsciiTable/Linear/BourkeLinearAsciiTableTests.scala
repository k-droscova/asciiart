package Core.Models.AsciiTable.Linear

import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite

import scala.compiletime.uninitialized
class BourkeLinearAsciiTableTests extends AnyFunSuite with BeforeAndAfterEach {
  var bourkeTable: BourkeLinearAsciiTable = uninitialized

  override def beforeEach(): Unit = {
    super.beforeEach()
    bourkeTable = new BourkeLinearAsciiTable()
  }

  override def afterEach(): Unit = {
    super.afterEach()
    bourkeTable = null
  }

  test("BourkeLinearAsciiTable should map grayscale value 0 to '$'") {
    assert(bourkeTable.getAsciiCharacter(0) == '$')
  }

  test("BourkeLinearAsciiTable should map grayscale value 12 to '$'") {
    assert(bourkeTable.getAsciiCharacter(12) == '%')
  }

  test("BourkeLinearAsciiTable should map grayscale value 13 to '@'") {
    assert(bourkeTable.getAsciiCharacter(13) == '%')
  }

  test("BourkeLinearAsciiTable should map grayscale value 25 to '@'") {
    assert(bourkeTable.getAsciiCharacter(25) == 'W')
  }

  test("BourkeLinearAsciiTable should map grayscale value 26 to 'B'") {
    assert(bourkeTable.getAsciiCharacter(26) == 'W')
  }

  test("BourkeLinearAsciiTable should map grayscale value 38 to 'B'") {
    assert(bourkeTable.getAsciiCharacter(38) == '*')
  }

  test("BourkeLinearAsciiTable should map grayscale value 39 to '%'") {
    assert(bourkeTable.getAsciiCharacter(39) == '*')
  }

  test("BourkeLinearAsciiTable should map grayscale value 51 to '%'") {
    assert(bourkeTable.getAsciiCharacter(51) == 'h')
  }

  test("BourkeLinearAsciiTable should map grayscale value 52 to '8'") {
    assert(bourkeTable.getAsciiCharacter(52) == 'k')
  }

  test("BourkeLinearAsciiTable should map grayscale value 64 to '8'") {
    assert(bourkeTable.getAsciiCharacter(64) == 'p')
  }

  test("BourkeLinearAsciiTable should map grayscale value 65 to '&'") {
    assert(bourkeTable.getAsciiCharacter(65) == 'p')
  }

  test("BourkeLinearAsciiTable should map grayscale value 77 to '&'") {
    assert(bourkeTable.getAsciiCharacter(77) == 'm')
  }

  test("BourkeLinearAsciiTable should map grayscale value 78 to 'W'") {
    assert(bourkeTable.getAsciiCharacter(78) == 'm')
  }

  test("BourkeLinearAsciiTable should map grayscale value 90 to 'W'") {
    assert(bourkeTable.getAsciiCharacter(90) == '0')
  }

  test("BourkeLinearAsciiTable should map grayscale value 91 to 'M'") {
    assert(bourkeTable.getAsciiCharacter(91) == '0')
  }

  test("BourkeLinearAsciiTable should map grayscale value 103 to 'M'") {
    assert(bourkeTable.getAsciiCharacter(103) == 'C')
  }

  test("BourkeLinearAsciiTable should map grayscale value 104 to '#'") {
    assert(bourkeTable.getAsciiCharacter(104) == 'J')
  }

  test("BourkeLinearAsciiTable should map grayscale value 116 to '#'") {
    assert(bourkeTable.getAsciiCharacter(116) == 'X')
  }

  test("BourkeLinearAsciiTable should map grayscale value 117 to '*'") {
    assert(bourkeTable.getAsciiCharacter(117) == 'X')
  }

  test("BourkeLinearAsciiTable should map grayscale value 129 to '*'") {
    assert(bourkeTable.getAsciiCharacter(129) == 'v')
  }

  test("BourkeLinearAsciiTable should map grayscale value 130 to 'o'") {
    assert(bourkeTable.getAsciiCharacter(130) == 'v')
  }

  test("BourkeLinearAsciiTable should map grayscale value 142 to 'o'") {
    assert(bourkeTable.getAsciiCharacter(142) == 'x')
  }

  test("BourkeLinearAsciiTable should map grayscale value 143 to 'a'") {
    assert(bourkeTable.getAsciiCharacter(143) == 'x')
  }

  test("BourkeLinearAsciiTable should map grayscale value 155 to 'a'") {
    assert(bourkeTable.getAsciiCharacter(155) == 'f')
  }

  test("BourkeLinearAsciiTable should map grayscale value 156 to 'h'") {
    assert(bourkeTable.getAsciiCharacter(156) == 't')
  }

  test("BourkeLinearAsciiTable should map grayscale value 168 to 'h'") {
    assert(bourkeTable.getAsciiCharacter(168) == '|')
  }

  test("BourkeLinearAsciiTable should map grayscale value 169 to 'k'") {
    assert(bourkeTable.getAsciiCharacter(169) == '|')
  }

  test("BourkeLinearAsciiTable should map grayscale value 181 to 'k'") {
    assert(bourkeTable.getAsciiCharacter(181) == '1')
  }

  test("BourkeLinearAsciiTable should map grayscale value 182 to 'b'") {
    assert(bourkeTable.getAsciiCharacter(182) == '1')
  }

  test("BourkeLinearAsciiTable should map grayscale value 194 to 'b'") {
    assert(bourkeTable.getAsciiCharacter(194) == '[')
  }

  test("BourkeLinearAsciiTable should map grayscale value 195 to 'd'") {
    assert(bourkeTable.getAsciiCharacter(195) == '[')
  }

  test("BourkeLinearAsciiTable should map grayscale value 207 to 'd'") {
    assert(bourkeTable.getAsciiCharacter(207) == '-')
  }

  test("BourkeLinearAsciiTable should map grayscale value 208 to 'p'") {
    assert(bourkeTable.getAsciiCharacter(208) == '_')
  }

  test("BourkeLinearAsciiTable should map grayscale value 220 to 'p'") {
    assert(bourkeTable.getAsciiCharacter(220) == '<')
  }

  test("BourkeLinearAsciiTable should map grayscale value 221 to 'q'") {
    assert(bourkeTable.getAsciiCharacter(221) == '<')
  }

  test("BourkeLinearAsciiTable should map grayscale value 233 to 'q'") {
    assert(bourkeTable.getAsciiCharacter(233) == '!')
  }

  test("BourkeLinearAsciiTable should map grayscale value 234 to 'w'") {
    assert(bourkeTable.getAsciiCharacter(234) == '!')
  }

  test("BourkeLinearAsciiTable should map grayscale value 246 to 'w'") {
    assert(bourkeTable.getAsciiCharacter(246) == ';')
  }

  test("BourkeLinearAsciiTable should map grayscale value 247 to 'm'") {
    assert(bourkeTable.getAsciiCharacter(247) == ';')
  }

  test("BourkeLinearAsciiTable should map grayscale value 255 to 'm'") {
    assert(bourkeTable.getAsciiCharacter(255) == ',')
  }
}
