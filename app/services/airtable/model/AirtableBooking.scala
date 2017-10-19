package services.airtable.model

import play.api.libs.json.{Json, OFormat}

case class AirtableBooking(
      rowId: String,
      leadStatus: String,
      suburb: String,
      state: String,
      mobileNumber: String,
      firstName: String,
      email: String,
      cost: BigDecimal,
      supplierPayment: BigDecimal,
      numberOfPeople: Int
)

object AirtableBooking
{
  implicit val oFormat: OFormat[AirtableBooking] = Json.format[AirtableBooking]
}