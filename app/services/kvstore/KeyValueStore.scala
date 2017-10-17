package services.kvstore

import play.api.libs.json.{Reads, Writes}
import utils.FutureO

import scala.concurrent.Future

trait KeyValueStore
{
  def get[A](key: String)(implicit reads: Reads[A]): FutureO[A]

  def set[A](key: String, value: A)(implicit writes: Writes[A]): Future[Boolean]

  def delete(keys: String*): Future[Long]
}
