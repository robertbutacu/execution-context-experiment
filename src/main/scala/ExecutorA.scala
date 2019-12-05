import java.util.concurrent.Executors

import cats.Functor
import cats.effect.ContextShift
import cats.implicits._
import scala.concurrent.ExecutionContext

class ExecutorA[F[_]: ContextShift: Functor] {
  private val NAME = "EXECUTOR_A"
  private val executionContext: ExecutionContext = ExecutionContext.fromExecutor(Executors.newSingleThreadExecutor())

  def executeOnA[A](f: F[String => A]): F[A] = ContextShift[F].evalOn(executionContext)(f.map(inF => inF(NAME)))
}
