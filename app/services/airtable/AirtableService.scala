package services.airtable

import javax.inject.{Inject, Singleton}

import constants.{ConfigValues, EnvVariables}
import org.joda.time.DateTime
import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import services.airtable.model.{AirtableBooking, AirtableStylist}
import services.types.PhoneNumber
import utils.ConfigUtils
import utils.JsonUtils.deserialize

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AirtableService @Inject()(wsClient: WSClient)(implicit executionContext: ExecutionContext)
{
  def getAirtableServiceUrl(): Future[String] =
    ConfigUtils.getEnvValueAsFuture(EnvVariables.AIRTABLE_SERVICE_URL)

  def fetchStylist(mobileNumber: String): Future[AirtableStylist] = for
    {
      parsedMobileNumber <- Future.fromTry(PhoneNumber.parse(mobileNumber))

      airtableServiceUrl <- getAirtableServiceUrl()

      response <- wsClient.url(s"$airtableServiceUrl/${ConfigValues.AIRTABLE_FIND_STYLIST_PATH}")
        .post(Json.obj("mobileNumber" -> parsedMobileNumber.localNumber))

      airtableStylist <- Future.fromTry(deserialize[AirtableStylist](response.json))
    }
    yield airtableStylist

  def fetchBookings(stylistAirtableId: String): Future[List[AirtableBooking]] = for
    {
      airtableServiceUrl <- getAirtableServiceUrl()

      response <- wsClient.url(s"$airtableServiceUrl/${ConfigValues.getBookingsUrl(stylistAirtableId)}").get()

      bookings <- Future.fromTry(deserialize[List[AirtableBooking]](response.json))

      sortedBookings = bookings.sortBy(booking => DateTime.parse(booking.eventDate).getMillis)
    }
    yield sortedBookings
}
