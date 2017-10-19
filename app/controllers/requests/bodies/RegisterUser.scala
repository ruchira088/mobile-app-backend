package controllers.requests.bodies

import play.api.libs.json.{Json, OFormat}

case class RegisterUser(mobileNumber: String)

object RegisterUser
{
  implicit val oFormat: OFormat[RegisterUser] = Json.format[RegisterUser]
}
