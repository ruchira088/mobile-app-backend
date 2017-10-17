package utils

import exceptions.UndefinedEnvValueException

import scala.concurrent.Future

object ConfigUtils
{
  def getEnvValue(name: String): Option[String] = ScalaUtils.toOption(System getenv name)

  def getEnvValueAsFuture(name: String): Future[String] =
    Future.fromTry(ScalaUtils.toTry(getEnvValue(name), UndefinedEnvValueException(name)))

  def getEnvValueOrDefault(name: String, defaultValue: => String): String =
    getEnvValue(name).getOrElse(defaultValue)
}
