package Core.Models.AsciiTable.Linear

import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite

import scala.compiletime.uninitialized

class DefaultLinearAsciiTableTests extends AnyFunSuite with BeforeAndAfterEach {
  var defaultTable: DefaultLinearAsciiTable = uninitialized
  override def beforeEach(): Unit = {
    super.beforeEach()
    defaultTable = new DefaultLinearAsciiTable()
  }

  override def afterEach(): Unit = {
    super.afterEach()
    defaultTable = null
  }

  test("DefaultLinearAsciiTable should map grayscale value 0 to ' '") {
    assert(defaultTable.getAsciiCharacter(0) == ' ')
  }

  test("DefaultLinearAsciiTable should map grayscale value 13 to ' '") {
    assert(defaultTable.getAsciiCharacter(13) == ' ')
  }

  test("DefaultLinearAsciiTable should map grayscale value 25 to ' '") {
    assert(defaultTable.getAsciiCharacter(25) == ' ')
  }

  test("DefaultLinearAsciiTable should map grayscale value 26 to '.'") {
    assert(defaultTable.getAsciiCharacter(26) == '.')
  }

  test("DefaultLinearAsciiTable should map grayscale value 39 to '.'") {
    assert(defaultTable.getAsciiCharacter(39) == '.')
  }

  test("DefaultLinearAsciiTable should map grayscale value 51 to '.'") {
    assert(defaultTable.getAsciiCharacter(51) == '.')
  }

  test("DefaultLinearAsciiTable should map grayscale value 52 to ':'") {
    assert(defaultTable.getAsciiCharacter(52) == ':')
  }

  test("DefaultLinearAsciiTable should map grayscale value 65 to ':'") {
    assert(defaultTable.getAsciiCharacter(65) == ':')
  }

  test("DefaultLinearAsciiTable should map grayscale value 77 to ':'") {
    assert(defaultTable.getAsciiCharacter(77) == ':')
  }

  test("DefaultLinearAsciiTable should map grayscale value 78 to '-'") {
    assert(defaultTable.getAsciiCharacter(78) == '-')
  }

  test("DefaultLinearAsciiTable should map grayscale value 91 to '-'") {
    assert(defaultTable.getAsciiCharacter(91) == '-')
  }

  test("DefaultLinearAsciiTable should map grayscale value 103 to '-'") {
    assert(defaultTable.getAsciiCharacter(103) == '-')
  }

  test("DefaultLinearAsciiTable should map grayscale value 104 to '='") {
    assert(defaultTable.getAsciiCharacter(104) == '=')
  }

  test("DefaultLinearAsciiTable should map grayscale value 117 to '='") {
    assert(defaultTable.getAsciiCharacter(117) == '=')
  }

  test("DefaultLinearAsciiTable should map grayscale value 129 to '='") {
    assert(defaultTable.getAsciiCharacter(129) == '=')
  }

  test("DefaultLinearAsciiTable should map grayscale value 130 to '+'") {
    assert(defaultTable.getAsciiCharacter(130) == '+')
  }

  test("DefaultLinearAsciiTable should map grayscale value 143 to '+'") {
    assert(defaultTable.getAsciiCharacter(143) == '+')
  }

  test("DefaultLinearAsciiTable should map grayscale value 155 to '+'") {
    assert(defaultTable.getAsciiCharacter(155) == '+')
  }

  test("DefaultLinearAsciiTable should map grayscale value 156 to '*'") {
    assert(defaultTable.getAsciiCharacter(156) == '*')
  }

  test("DefaultLinearAsciiTable should map grayscale value 169 to '*'") {
    assert(defaultTable.getAsciiCharacter(169) == '*')
  }

  test("DefaultLinearAsciiTable should map grayscale value 181 to '*'") {
    assert(defaultTable.getAsciiCharacter(181) == '*')
  }

  test("DefaultLinearAsciiTable should map grayscale value 182 to '#'") {
    assert(defaultTable.getAsciiCharacter(182) == '#')
  }

  test("DefaultLinearAsciiTable should map grayscale value 195 to '#'") {
    assert(defaultTable.getAsciiCharacter(195) == '#')
  }

  test("DefaultLinearAsciiTable should map grayscale value 207 to '#'") {
    assert(defaultTable.getAsciiCharacter(207) == '#')
  }

  test("DefaultLinearAsciiTable should map grayscale value 208 to '%'") {
    assert(defaultTable.getAsciiCharacter(208) == '%')
  }

  test("DefaultLinearAsciiTable should map grayscale value 221 to '%'") {
    assert(defaultTable.getAsciiCharacter(221) == '%')
  }

  test("DefaultLinearAsciiTable should map grayscale value 233 to '%'") {
    assert(defaultTable.getAsciiCharacter(233) == '%')
  }

  test("DefaultLinearAsciiTable should map grayscale value 234 to '@'") {
    assert(defaultTable.getAsciiCharacter(234) == '@')
  }

  test("DefaultLinearAsciiTable should map grayscale value 247 to '@'") {
    assert(defaultTable.getAsciiCharacter(247) == '@')
  }

  test("DefaultLinearAsciiTable should map grayscale value 255 to '@'") {
    assert(defaultTable.getAsciiCharacter(255) == '@')
  }
}
