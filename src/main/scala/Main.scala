import cats.effect.{ExitCode, IO, IOApp}

object Main extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    val service = new Service[IO]

    import cats.implicits._

    service.evaluate().as(ExitCode.Error)
  }
}
