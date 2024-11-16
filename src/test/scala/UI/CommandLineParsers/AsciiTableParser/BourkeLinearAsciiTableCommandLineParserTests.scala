package UI.CommandLineParsers.AsciiTableParser

import Core.Errors.{BaseError, GeneralErrorCodes, LogContext}
import Core.Models.AsciiTable.Linear.BourkeLinearAsciiTable
import UI.CommandLineParsers.AsciiTableParser.SpecializedAsciiParsers.BourkeLinearAsciiTableCommandLineParser
import org.mockito.MockedConstruction
import org.mockito.Mockito.*
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.BeforeAndAfterEach

import scala.compiletime.uninitialized

class BourkeLinearAsciiTableCommandLineParserTests extends AnyFunSuite with BeforeAndAfterEach {

  private var bourkeTableMock: MockedConstruction[BourkeLinearAsciiTable] = uninitialized
  private var parser: BourkeLinearAsciiTableCommandLineParser = uninitialized

  override def beforeEach(): Unit = {
    super.beforeEach()
    parser = new BourkeLinearAsciiTableCommandLineParser()
  }

  override def afterEach(): Unit = {
    super.afterEach()
    parser = null
  }

  test("Should return None if --table=bourke argument is not present") {
    val args = Array("--some-other-arg", "value")
    val result = parser.parse(args)
    assert(result == Right(None))
  }

  test("Should return BourkeLinearAsciiTable if --table=bourke argument is present once") {
    bourkeTableMock = mockConstruction(classOf[BourkeLinearAsciiTable], (_: BourkeLinearAsciiTable, _: MockedConstruction.Context) => {})

    val args = Array("--table=bourke")
    val result = parser.parse(args)

    result match {
      case Right(Some(table)) =>
        assert(table == bourkeTableMock.constructed().get(0))
      case _ => fail("Expected Right(Some(BourkeLinearAsciiTable)), but got something else.")
    }

    bourkeTableMock.close()
  }

  test("Should return error if --table=bourke argument is specified multiple times") {
    val args = Array("--table=bourke", "--table=bourke")
    val result = parser.parse(args)

    assert(result.isLeft)
    result match {
      case Left(error: BaseError) =>
        assert(error.errorCode == GeneralErrorCodes.InvalidArgument)
        assert(error.message.contains("The --table=bourke argument is specified multiple times."))
      case _ => fail("Expected Left(BaseError), but got something else.")
    }
  }
}
