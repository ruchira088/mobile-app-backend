package controllers.requests.bodies

import play.api.libs.json.{Json, OFormat}

case class RegisterDeviceToken(stylistId: String, deviceToken: String)

object RegisterDeviceToken
{
  implicit val oFormat: OFormat[RegisterDeviceToken] = Json.format[RegisterDeviceToken]
}
