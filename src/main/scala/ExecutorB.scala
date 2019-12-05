import java.util.concurrent.Executors

import cats.Functor
import cats.effect.ContextShift
import cats.implicits._
import scala.concurrent.ExecutionContext

class ExecutorB[F[_]: ContextShift: Functor] {
  private val NAME = "EXECUTOR_B"
  private val executionContext: ExecutionContext = ExecutionContext.fromExecutor(Executors.newSingleThreadExecutor())

  def executeOnB[A](f: F[String => A]): F[A] = ContextShift[F].evalOn(executionContext)(f.map(inF => inF(NAME)))
}
