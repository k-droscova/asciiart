package UI

trait ConsoleViewModeling {
  /**
   * Defines the contract for running the ConsoleView's ViewModel.
   *
   * @param args Command-line arguments provided by the user.
   */
  def run(args: Array[String]): Unit
}
