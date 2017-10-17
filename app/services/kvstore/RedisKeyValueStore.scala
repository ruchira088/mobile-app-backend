package services.kvstore

import javax.inject.{Inject, Singleton}

import play.api.libs.json.{Json, Reads, Writes}
import redis.RedisClient
import utils.{FutureO, ScalaUtils}

import scala.concurrent.ExecutionContext

@Singleton
class RedisKeyValueStore @Inject()(redisClient: RedisClient)(implicit executionContext: ExecutionContext)
  extends KeyValueStore
{
  def get[A](key: String)(implicit reads: Reads[A]) = for {
    jsonString <- FutureO(redisClient.get[String](key))
    value <- ScalaUtils.toTry(Json.parse(jsonString).asOpt[A])
  } yield value

  def set[A](key: String, value: A)(implicit writes: Writes[A]) =
    redisClient.set[String](key, Json.toJson[A](value).toString())

  def delete(keys: String*) = redisClient.del(keys: _*)
}
