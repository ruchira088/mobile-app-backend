package utils

object ConfigUtils
{
  def getEnvValue(name: String): Option[String] = ScalaUtils.toOption(System getenv name)

  def getEnvValueOrDefault(name: String, defaultValue: => String): String =
    getEnvValue(name).getOrElse(defaultValue)
}
