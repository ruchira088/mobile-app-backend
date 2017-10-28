package utils

import java.util.concurrent

import akka.actor.ActorSystem
import exceptions.EmptyOptionException
import play.api.libs.json.{JsError, JsResult, JsSuccess}

import scala.concurrent.{Future, Promise}
import scala.concurrent.duration._
import scala.util.control.NonFatal
import scala.util.{Failure, Success, Try}

object ScalaUtils
{
  implicit def fromTry[A](tryValue: Try[A]): JsResult[A] = tryValue match {
    case Success(value) => JsSuccess(value)
    case Failure(throwable) => JsError(throwable.getMessage)
  }

  def toOption[A](value: A): Option[A] = value match {
    case null => None
    case _ => Some(value)
  }

  def toTry[A](option: Option[A], exception: => Exception = EmptyOptionException): Try[A] =
    option.fold[Try[A]](Failure(exception))(Success(_))

  def convert[A, B](f: A => B)(value: A): Try[B] =
    try {
      Success(f(value))
    } catch {
      case NonFatal(throwable) => Failure(throwable)
    }

//  def simpleConversionToScalaFuture[A]
//    (javaFuture: concurrent.Future[A])(implicit executionContext: ExecutionContext): Future[A] =
//      Future {
//        blocking {
//          javaFuture.get()
//        }
//      }

  def simpleConversionToScalaFuture[A]
  (javaFuture: concurrent.Future[A])(implicit actorSystem: ActorSystem): Future[A] =
    if (javaFuture.isDone)
      Future.successful(javaFuture.get())
    else {
      val promise = Promise[A]

      actorSystem.scheduler.scheduleOnce(100 milliseconds) {
        promise.completeWith(simpleConversionToScalaFuture(javaFuture))
      }

      promise.future
    }

  def predicate(boolean: Boolean, exception: => Exception, onFail: => Unit = {}): Future[Unit] =
    if (boolean)
      Future.successful(())
    else
      Future.failed(exception)

}