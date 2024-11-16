package UI.CommandLineParsers.AsciiTableParser

import Core.Errors.{BaseError, GeneralErrorCodes, LogContext}
import Core.Models.AsciiTable.Linear.CustomLinearAsciiTable
import UI.CommandLineParsers.AsciiTableParser.SpecializedAsciiParsers.CustomLinearAsciiTableCommandLineParser
import org.mockito.MockedConstruction
import org.mockito.Mockito.*
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.BeforeAndAfterEach

import scala.compiletime.uninitialized

class CustomLinearAsciiTableCommandLineParserTests extends AnyFunSuite with BeforeAndAfterEach {

  private var customTableMock: MockedConstruction[CustomLinearAsciiTable] = uninitialized
  private var parser: CustomLinearAsciiTableCommandLineParser = uninitialized

  override def beforeEach(): Unit = {
    super.beforeEach()
    parser = new CustomLinearAsciiTableCommandLineParser()
  }

  override def afterEach(): Unit = {
    super.afterEach()
    parser = null
  }

  test("Should return None if --custom-table argument is not present") {
    val args = Array("--some-other-arg", "value")
    val result = parser.parse(args)
    assert(result == Right(None))
  }

  test("Should return CustomLinearAsciiTable if --custom-table argument is present with valid characters") {
    customTableMock = mockConstruction(classOf[CustomLinearAsciiTable], (mocked, context) => {
      assert(context.arguments.get(0) == "abc123")
    })

    val args = Array("--custom-table", "abc123")
    val result = parser.parse(args)

    result match {
      case Right(Some(table)) =>
        assert(table == customTableMock.constructed().get(0))
      case _ => fail("Expected Right(Some(CustomLinearAsciiTable)), but got something else.")
    }
    customTableMock.close()
  }

  test("Should return error if --custom-table argument is present without a value") {
    val args = Array("--custom-table")
    val result = parser.parse(args)
    assert(result.isLeft)
    result match {
      case Left(error: BaseError) =>
        assert(error.errorCode == GeneralErrorCodes.InvalidArgument)
        assert(error.message.contains("Custom characters must be specified after --custom-table argument."))
      case _ => fail("Expected Left(BaseError), but got something else.")
    }
  }

  test("Should return error if --custom-table argument is followed by another argument starting with --") {
    val args = Array("--custom-table", "--another-arg")
    val result = parser.parse(args)
    assert(result.isLeft)
    result match {
      case Left(error: BaseError) =>
        assert(error.errorCode == GeneralErrorCodes.InvalidArgument)
        assert(error.message.contains("Custom characters must be specified after --custom-table argument."))
      case _ => fail("Expected Left(BaseError), but got something else.")
    }
  }

  test("Should return error if multiple --custom-table arguments are present") {
    val args = Array("--custom-table", "abc123", "--custom-table", "xyz789")
    val result = parser.parse(args)
    assert(result.isLeft)
    result match {
      case Left(error: BaseError) =>
        assert(error.errorCode == GeneralErrorCodes.InvalidArgument)
        assert(error.message.contains("Only one --custom-table argument is allowed."))
      case _ => fail("Expected Left(BaseError), but got something else.")
    }
  }
}