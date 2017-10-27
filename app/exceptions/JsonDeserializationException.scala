package exceptions

import play.api.libs.json.{JsPath, JsonValidationError}

case class JsonDeserializationException(validationErrors: List[(JsPath, List[JsonValidationError])])
  extends Exception
{
  override def getMessage = validationErrors.map(JsonDeserializationError.tupled).mkString(", ")
}

case class JsonDeserializationError(jsPath: JsPath, validationErrors: List[JsonValidationError])
{
  override def toString = s"(${jsPath.toJsonString}: ${validationErrors.map(_.messages.mkString).mkString})"
}

object JsonDeserializationException
{
  def apply(validationErrors: Seq[(JsPath, Seq[JsonValidationError])]): JsonDeserializationException =
    JsonDeserializationException(validationErrors.toList.map {
      case (jsPath, jsonValidationErrors) => (jsPath, jsonValidationErrors.toList)
    })
}