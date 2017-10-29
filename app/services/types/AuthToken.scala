package services.types

import models.db.Stylist
import org.joda.time.DateTime
import play.api.libs.json.{Json, OFormat}
import utils.JsonUtils._

case class AuthToken(bearerToken: String, stylist: Stylist, createdAt: DateTime = DateTime.now())

object AuthToken
{
  implicit val oFormat: OFormat[AuthToken] = Json.format[AuthToken]
}