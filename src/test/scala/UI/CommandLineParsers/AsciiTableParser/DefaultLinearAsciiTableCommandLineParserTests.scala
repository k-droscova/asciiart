package UI.CommandLineParsers.AsciiTableParser

import Core.Errors.{BaseError, GeneralErrorCodes}
import Core.Models.AsciiTable.Linear.DefaultLinearAsciiTable
import UI.CommandLineParsers.AsciiTableParser.SpecializedAsciiParsers.DefaultLinearAsciiTableCommandLineParser
import org.mockito.MockedConstruction
import org.mockito.Mockito.*
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite

import scala.compiletime.uninitialized

class DefaultLinearAsciiTableCommandLineParserTests extends AnyFunSuite with BeforeAndAfterEach {

  private var defaultTableMock: MockedConstruction[DefaultLinearAsciiTable] = uninitialized
  private var parser: DefaultLinearAsciiTableCommandLineParser = uninitialized

  override def beforeEach(): Unit = {
    super.beforeEach()
    parser = new DefaultLinearAsciiTableCommandLineParser()
  }

  override def afterEach(): Unit = {
    super.afterEach()
    parser = null
  }

  test("Should return DefaultLinearAsciiTable if no --table= argument is provided") {
    defaultTableMock = mockConstruction(classOf[DefaultLinearAsciiTable], (_: DefaultLinearAsciiTable, _: MockedConstruction.Context) => {})

    val args = Array("--some-other-arg", "value")
    val result = parser.parse(args)

    result match {
      case Right(Some(convertor)) =>
      case _ => fail("Expected Right(Some(DefaultLinearAsciiTable)), but got something else.")
    }

    defaultTableMock.close()
  }

  test("Should return DefaultLinearAsciiTable if --table=default argument is provided once") {
    defaultTableMock = mockConstruction(classOf[DefaultLinearAsciiTable], (_: DefaultLinearAsciiTable, _: MockedConstruction.Context) => {})

    val args = Array("--table=default")
    val result = parser.parse(args)

    result match {
      case Right(Some(convertor)) =>
      case _ => fail("Expected Right(Some(DefaultLinearAsciiTable)), but got something else.")
    }

    defaultTableMock.close()
  }

  test("Should return None if other --table= argument is provided") {
    val args = Array("--table=bourke")
    val result = parser.parse(args)
    assert(result == Right(None))
  }

  test("Should return error if --table=default is specified multiple times") {
    val args = Array("--table=default", "--table=default")
    val result = parser.parse(args)

    assert(result.isLeft)
    result match {
      case Left(error: BaseError) =>
        assert(error.errorCode == GeneralErrorCodes.InvalidArgument)
        assert(error.message.contains("The --table=default argument is specified multiple times."))
      case _ => fail("Expected Left(BaseError), but got something else.")
    }
  }
}