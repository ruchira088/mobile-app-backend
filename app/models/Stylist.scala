package models

import org.joda.time.DateTime
import play.api.libs.json.{Json, OFormat}

import utils.JsonUtils._

case class Stylist(
    id: String,
    createdAt: DateTime,
    firstName: String,
    email: String,
    mobile: String,
    profileName: String
)

object Stylist
{
  implicit val oFormat: OFormat[Stylist] = Json.format[Stylist]
}