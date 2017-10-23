package models

import play.api.libs.json.{Json, OFormat}

case class PushNotification(
    id: String,
    stylistId: String,
    stylistAirtableId: String,
    deviceToken: String
)

object PushNotification
{
  implicit val oFormat: OFormat[PushNotification] = Json.format[PushNotification]
}