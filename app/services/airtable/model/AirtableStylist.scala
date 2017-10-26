package services.airtable.model

import play.api.libs.json.{Json, OFormat}

case class AirtableStylist(
      firstName: String,
      suburb: String,
      state: String,
      mobileNumber: String,
      email: String,
      rowId: String
)

object AirtableStylist
{
  implicit val oFormat: OFormat[AirtableStylist] = Json.format[AirtableStylist]
}
