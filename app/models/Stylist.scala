package models

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
    airtableId: String
)

object Stylist
{
  implicit val oFormat: OFormat[Stylist] = Json.format[Stylist]

  def apply(airtableStylist: AirtableStylist): Try[Stylist] = for
    {
      phoneNumber <- PhoneNumber.parse(airtableStylist.Mobile)
      AirtableStylist(firstName, _, email, rowId) = airtableStylist

      stylist = Stylist(randomUuid(), DateTime.now(), firstName, email, phoneNumber, rowId)
    }
    yield stylist

}