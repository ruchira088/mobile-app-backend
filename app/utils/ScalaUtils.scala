package utils

import java.util.concurrent

import exceptions.EmptyOptionException

import scala.concurrent.{ExecutionContext, Future, blocking}
import scala.util.{Failure, Success, Try}

object ScalaUtils
{
  def toOption[A](value: A): Option[A] = value match {
    case null => None
    case _ => Some(value)
  }

  def toTry[A](option: Option[A], exception: => Exception = EmptyOptionException): Try[A] =
    option.fold[Try[A]](Failure(exception))(Success(_))

  // TODO Implement the more efficient Java Future to Scala Future conversion
  def simpleConversionToScalaFuture[A]
    (javaFuture: concurrent.Future[A])(implicit executionContext: ExecutionContext): Future[A] =
      Future {
        blocking {
          javaFuture.get()
        }
      }

}