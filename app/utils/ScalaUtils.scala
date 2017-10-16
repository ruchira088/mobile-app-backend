package utils

import java.util.concurrent

import scala.concurrent.{ExecutionContext, Future, blocking}

object ScalaUtils
{
  def toOption[A](value: A): Option[A] = value match {
    case null => None
    case _ => Some(value)
  }

  def simpleConversionToScalaFuture[A]
    (javaFuture: concurrent.Future[A])(implicit executionContext: ExecutionContext): Future[A] =
      Future {
        blocking {
          javaFuture.get()
        }
      }

}