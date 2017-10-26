package services.notifications.model

import play.api.libs.json.{JsObject, Json, OFormat}

case class AccountRequest(
      deviceToken: String,
      state: String,
      suburb: String,
      stylistAirtableId: String,
      mobileNumber: String,
      email: String
) {
  accountRequest =>

  def toJsObject: JsObject = Json.toJsObject(accountRequest)
}

object AccountRequest
{
  implicit val oFormat: OFormat[AccountRequest] = Json.format[AccountRequest]
}
