package Services.CommandLineParsers.AsciiTableParser

import Core.Errors.{BaseError, GeneralErrorCodes}
import Services.ImageConvertors.AsciiConvertor.{BourkeLinearAsciiConvertor, BorderedAsciiConvertor, DefaultLinearAsciiConvertor, CustomLinearAsciiConvertor}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.BeforeAndAfterEach

import scala.compiletime.uninitialized
import org.mockito.Mockito._
import org.mockito.MockedConstruction
class AsciiTableCommandLineParserTests extends AnyFunSuite with BeforeAndAfterEach {
  private var parser: AsciiTableCommandLineParserImpl = uninitialized
  private var defaultTableMock: MockedConstruction[DefaultLinearAsciiConvertor] = uninitialized
  private var customTableMock: MockedConstruction[CustomLinearAsciiConvertor] = uninitialized
  private var borderedTableMock: MockedConstruction[BorderedAsciiConvertor] = uninitialized
  private var bourkeTableMock: MockedConstruction[BourkeLinearAsciiConvertor] = uninitialized

  override def beforeEach(): Unit = {
    super.beforeEach()
    parser = new AsciiTableCommandLineParserImpl()
  }

  override def afterEach(): Unit = {
    super.afterEach()
    parser = null
  }

  test("No input returns default table") {
    defaultTableMock = mockConstruction(classOf[DefaultLinearAsciiConvertor], (_, _) => {})
    val converter = parser.parse(Array("random", "input", "for", "testing"))
    assert(converter.isInstanceOf[DefaultLinearAsciiConvertor])
    defaultTableMock.close()
  }

  test("Valid input with default table") {
    defaultTableMock = mockConstruction(classOf[DefaultLinearAsciiConvertor], (_, _) => {})
    val converter = parser.parse(Array("--table=default"))
    assert(converter.isInstanceOf[DefaultLinearAsciiConvertor])
    defaultTableMock.close()
  }

  test("Empty input returns default table") {
    defaultTableMock = mockConstruction(classOf[DefaultLinearAsciiConvertor], (_, _) => {})
    val converter = parser.parse(Array.empty)
    assert(converter.isInstanceOf[DefaultLinearAsciiConvertor])
    defaultTableMock.close()
  }

  test("Input with only whitespace returns default table") {
    defaultTableMock = mockConstruction(classOf[DefaultLinearAsciiConvertor], (_, _) => {})
    val converter = parser.parse(Array("          "))
    assert(converter.isInstanceOf[DefaultLinearAsciiConvertor])
    defaultTableMock.close()
  }

  test("Valid input with bourke table") {
    bourkeTableMock = mockConstruction(classOf[BourkeLinearAsciiConvertor], (_, _) => {})
    val converter = parser.parse(Array("--table=bourke"))
    assert(converter.isInstanceOf[BourkeLinearAsciiConvertor])
    bourkeTableMock.close()
  }

  test("Valid input with custom table") {
    customTableMock = mockConstruction(classOf[CustomLinearAsciiConvertor], (mocked, context) => {
      assert("customchars" == context.arguments.get(0).asInstanceOf[String])
    })
    val converter = parser.parse(Array("--custom-table", "customchars"))
    assert(converter.isInstanceOf[CustomLinearAsciiConvertor])
    customTableMock.close()
  }

  test("Valid input with bordered table and borders") {
    val converter = parser.parse(Array("--table=bordered", "chars", "[1,2,3,4]"))
    assert(converter.isInstanceOf[BorderedAsciiConvertor])
    val convertor: BorderedAsciiConvertor = converter.asInstanceOf[BorderedAsciiConvertor]
    assert(convertor.characters == "chars")
    assert(convertor.borders == List(1,2,3,4))
  }

  test("Whitespace handling in custom table argument") {
    customTableMock = mockConstruction(classOf[CustomLinearAsciiConvertor], (mocked, context) => {
      assert(" chars " == context.arguments.get(0).asInstanceOf[String])
    })
    val converter = parser.parse(Array("--custom-table", " chars "))
    assert(converter.isInstanceOf[CustomLinearAsciiConvertor])
    customTableMock.close()
  }

  test("Invalid table type") {
    val thrown = intercept[BaseError] {
      parser.parse(Array("--table=invalid"))
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Invalid table argument."))
  }

  test("Multiple table arguments") {
    val thrown = intercept[BaseError] {
      parser.parse(Array("--table=default", "--custom-table", "chars"))
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Only one table can be specified"))
  }

  test("Missing characters for custom table") {
    val thrown = intercept[BaseError] {
      parser.parse(Array("--custom-table"))
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Custom characters must be specified after --custom-table argument."))
  }

  test("Empty characters for custom table") {
    val thrown = intercept[BaseError] {
      parser.parse(Array("--custom-table", ""))
    }
    assert(thrown.message.contains("Custom ASCII characters cannot be empty."))
  }

  test("Missing character argument for bordered table") {
    val thrown = intercept[BaseError] {
      parser.parse(Array("--table=bordered"))
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Custom characters must be specified after --table=bordered argument."))
  }

  test("Missing border argument for bordered table") {
    val thrown = intercept[BaseError] {
      parser.parse(Array("--table=bordered", "jchebchjeb"))
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Border characters must be specified after --table=bordered argument."))
  }

  test("Invalid string border argument for bordered table") {
    val thrown = intercept[BaseError] {
      parser.parse(Array("--table=bordered", "jchebchjeb", "[abc,1,1,1,1]"))
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Border characters must be be in this pattern: [int,int,int,...]"))
  }

  test("Invalid non int border argument for bordered table") {
    val thrown = intercept[BaseError] {
      parser.parse(Array("--table=bordered", "jchebchjeb", "[123.23,11,11,111]"))
    }
    assert(thrown.errorCode == GeneralErrorCodes.InvalidArgument)
    assert(thrown.message.contains("Border characters must be be in this pattern: [int,int,int,...]"))
  }

  test("Invalid border argument combination for bordered table - mismatch in char length and border length") {
    val thrown = intercept[BaseError] {
      parser.parse(Array("--table=bordered", "abcd", "[]"))
    }
    assert(thrown.message.contains("Invalid arguments for Bordered Ascii Table:"))
  }

  test("Invalid border argument combination for bordered table - empty string") {
    val thrown = intercept[BaseError] {
      parser.parse(Array("--table=bordered", "", "[]"))
    }
    assert(thrown.message.contains("Invalid Bordered Ascii Table: characters string cannot be empty."))
  }
}
