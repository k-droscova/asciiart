package AsciiArt

object AppDependencies {

  // Logger service
  lazy val logger: LoggerService = new ConsoleLoggerService()

  // Image loader service (file-based loader for example)
  lazy val imageLoader: ImageLoader = new FileImageLoader("image.jpg")

  // Filter service
  lazy val filterService: FilterService = new BasicFilterService()

  // Output service (saving and printing)
  lazy val outputService: OutputService = new FileOutputService("output.txt")
}