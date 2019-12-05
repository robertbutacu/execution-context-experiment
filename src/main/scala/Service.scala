import cats.{Applicative, Monad}
import cats.effect.{Concurrent, ContextShift, IO}

import scala.util.Random
import cats.implicits._
import cats.effect.syntax.all._

class Service[F[_]: ContextShift: Applicative: Monad: Concurrent] {
  val executorA = new ExecutorA[F]
  val executorB = new ExecutorB[F]

  def evaluate(): F[Unit] = for {
    f1 <- executorA.executeOnA(longOperation()).start
    f2 <- executorB.executeOnB(shortOperation()).start
    r <- Applicative[F].pure(5)
    _ = println(r)
    _ = println("Starting to wait")
    // switch the following comments and code around for different effect

   // _  <- f1.join
    _ <- f2.join
    //_ = println("Waited for f1... This was more blocking, so this is more blocking...")
    _ = println("Waited for f2... This was quicker")
    //_  <- f2.join
    _ <- f1.join
    //_ = println("Waited for f2... This was quicker")
    _ = println("Waited for f1... This was more blocking, so this is more blocking...")
  } yield r

  private def longOperation(): F[String => Unit] = {
    Applicative[F].pure { executionContextName =>
      val operations = (0 to 4).toList
      operations.foreach {
        o =>
          val random = Random.nextInt(5000)
          Thread.sleep(random)
          println(s"Hello from $executionContextName on operation number $o")
      }
    }
  }

  private def shortOperation(): F[String => Unit] = {
    Applicative[F].pure { executionContextName =>
      val operations = (0 to 4).toList
      operations.foreach {
        o =>
          val random = Random.nextInt(1000)
          Thread.sleep(random)
          println(s"Hello from $executionContextName on operation number $o")
      }
    }
  }
}
