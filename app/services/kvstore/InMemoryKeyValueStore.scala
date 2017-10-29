package services.kvstore
import javax.inject.{Inject, Singleton}

import org.joda.time.DateTime
import play.api.libs.json.{Reads, Writes}
import utils.FutureO

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class InMemoryKeyValueStore @Inject()(implicit executionContext: ExecutionContext) extends KeyValueStore
{
  var inMemoryStore: Map[String, Any] = Map.empty

  override def get[A](key: String)(implicit reads: Reads[A]) = FutureO {
    Future.successful(inMemoryStore.get(key).flatMap {
      case value: A => Some(value)
      case _ => None
    })
  }

  override def set[A](key: String, value: A)(implicit writes: Writes[A]) = {
    inMemoryStore = inMemoryStore + (key -> value)
    Future.successful(true)
  }

  override def delete(keys: String*) = {
    val startTime = DateTime.now()
    inMemoryStore = inMemoryStore -- keys
    Future.successful(DateTime.now().getMillis - startTime.getMillis)
  }
}
