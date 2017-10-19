package services.airtable.model

import play.api.libs.json.{Json, OFormat}

case class AirtableBooking(
      `Row ID`: String,
      `Lead Status`: String,
      `Event Date`: String,
      `To be ready by`: String,
      Suburb: String,
      State: String,
      Mobile: List[String],
      `First Name`: List[String],
      `Email (for Zapier)`: String,
      Cost: BigDecimal,
      SupplierPayment: BigDecimal,
      `Number of People`: String,
)

object AirtableBooking
{
  implicit val oFormat: OFormat[AirtableBooking] = Json.format[AirtableBooking]
}