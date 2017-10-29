package models.db

import org.joda.time.DateTime
import play.api.libs.json.{Json, OFormat}
import utils.JsonUtils._

case class PushNotification(
    id: String,
    createdAt: DateTime,
    stylistId: String,
    stylistAirtableId: String,
    deviceToken: String
)

object PushNotification
{
  implicit val oFormat: OFormat[PushNotification] = Json.format[PushNotification]
}