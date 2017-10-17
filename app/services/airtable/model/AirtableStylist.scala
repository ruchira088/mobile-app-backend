package services.airtable.model

import play.api.libs.json.{Json, OFormat}

case class AirtableStylist(
  FirstName: String,
  Mobile: String,
  Email: String,
  `Profile Handle`: String
)

object AirtableStylist
{
  implicit val oFormat: OFormat[AirtableStylist] = Json.format[AirtableStylist]
}