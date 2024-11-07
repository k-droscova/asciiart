package UI

import Core.Errors.{BaseError, GeneralErrorCodes, LogContext, LogSeverity}
import UI.ConsoleViewModel
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.BeforeAndAfterEach

import scala.compiletime.uninitialized
import org.mockito.Mockito.*
import org.mockito.MockedConstruction
import org.mockito.ArgumentMatchers.any

import java.io.{ByteArrayOutputStream, PrintStream}
class ConsoleViewTests extends AnyFunSuite with BeforeAndAfterEach {
  private var viewModel: MockedConstruction[ConsoleViewModel] = uninitialized

  override def beforeEach(): Unit = {
    super.beforeEach()
  }

  override def afterEach(): Unit = {
    super.afterEach()
    viewModel = null
  }

  test("Passes correct args to view-model") {
    val args = Array("random", "testing", "args")
    viewModel = mockConstruction(classOf[ConsoleViewModel], (mockedInstance, _) => {
      doNothing().when(mockedInstance).run(any())
    })
    ConsoleView.main(args)

    verify(viewModel.constructed().get(0)).run(args)
    viewModel.close()
  }

  test("Prints error message to console when any error is thrown by view-model") {
    val args = Array("random", "testing", "args")

    val outputStream = new ByteArrayOutputStream()
    Console.withOut(new PrintStream(outputStream)) {
      viewModel = mockConstruction(classOf[ConsoleViewModel], (mockedInstance, _) => {
        doAnswer(_ => throw BaseError(
          "Testing error",
          LogSeverity.Error,
          LogContext.UI,
          GeneralErrorCodes.UnknownError
        )).when(mockedInstance).run(any())
      })
      ConsoleView.main(args)
    }
    assert(outputStream.toString.contains("Error: Testing error"))
    viewModel.close()
  }
}
