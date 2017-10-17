package controllers.response

import play.api.libs.json.{JsValue, Json, OFormat}

case class AuthenticateResult(success: Boolean)
{
  self =>

  def toJson: JsValue = Json.toJson(self)
}

object AuthenticateResult
{
  implicit val oFormat: OFormat[AuthenticateResult] = Json.format[AuthenticateResult]
}