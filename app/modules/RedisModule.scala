package modules

import akka.actor.ActorSystem
import com.google.inject.AbstractModule
import constants.ConfigValues._
import constants.EnvVariables
import redis.RedisClient
import utils.ConfigUtils.getEnvValue
import utils.ScalaUtils.convert
import utils.SystemUtils

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}

class RedisModule extends AbstractModule
{
  def configure() =
  {
    val redisHost = getEnvValue(EnvVariables.REDIS_HOST) getOrElse DEFAULT_REDIS_HOST

    val redisPort = getEnvValue(EnvVariables.REDIS_PORT)
      .flatMap(port => convert[String, Int](_.toInt)(port).toOption)
      .getOrElse(DEFAULT_REDIS_PORT)

    implicit val akkaSystem: ActorSystem = akka.actor.ActorSystem()

    val redisClient = RedisClient(host = redisHost, port = redisPort)

    val verification = verifyRedisServer(redisClient)

    verification.onComplete {
      case Success(_) =>
        println("Connection to the Redis server has been verified.")
      case Failure(throwable) =>
        SystemUtils.terminate(throwable, "Failed to verify connection with Redis server")
    }

    Await.ready(verification, 30 seconds)

    bind(classOf[RedisClient]).toInstance(redisClient)
  }

  def verifyRedisServer(redisClient: RedisClient): Future[String] = for {
    result <- {
      val ping = redisClient.ping()
      println("Redis ping sent !")
      ping
    }
    _ = println(s"Redis server replied with $result.")
  } yield result
}
