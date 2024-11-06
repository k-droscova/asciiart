package UI

import Core.Errors.BaseError

object ConsoleView {
  def main(args: Array[String]): Unit = {
    val viewModel = new ConsoleViewModel()

    try {
      viewModel.run(args)
    } catch {
      case e: BaseError =>
        println(s"Error: ${e.message}")
    }
  }
}
