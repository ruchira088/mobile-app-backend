package models.db

import org.joda.time.DateTime
import play.api.libs.json.{Json, OFormat}
import services.airtable.model.AirtableStylist
import services.types.PhoneNumber
import utils.GeneralUtils.randomUuid
import utils.JsonUtils._

import scala.util.Try

case class Stylist(
    id: String,
    createdAt: DateTime,
    firstName: String,
    email: String,
    mobile: PhoneNumber,
    suburb: String,
    state: String,
    airtableId: String
)

object Stylist
{
  implicit val oFormat: OFormat[Stylist] = Json.format[Stylist]

  def apply(airtableStylist: AirtableStylist): Try[Stylist] = for
    {
      phoneNumber <- PhoneNumber.parse(airtableStylist.mobileNumber)
    }
    yield Stylist(
      randomUuid(),
      DateTime.now(),
      airtableStylist.firstName,
      airtableStylist.email,
      phoneNumber,
      airtableStylist.suburb,
      airtableStylist.state,
      airtableStylist.rowId
    )

}