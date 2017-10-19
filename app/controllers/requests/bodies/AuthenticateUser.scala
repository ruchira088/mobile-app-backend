package controllers.requests.bodies

import play.api.libs.json.{Json, OFormat}

case class AuthenticateUser(mobileNumber: String, passcode: String)

object AuthenticateUser
{
  implicit val oFormat: OFormat[AuthenticateUser] = Json.format[AuthenticateUser]
}