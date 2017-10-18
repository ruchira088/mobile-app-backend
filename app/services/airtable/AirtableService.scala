package services.airtable

import javax.inject.{Inject, Singleton}

import constants.{ConfigValues, EnvVariables}
import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import services.airtable.model.AirtableStylist
import services.types.PhoneNumber
import utils.ConfigUtils
import utils.JsonUtils.deserialize

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AirtableService @Inject()(wsClient: WSClient)(implicit executionContext: ExecutionContext)
{
  def fetchStylist(mobileNumber: String): Future[AirtableStylist] = for
    {
      parsedMobileNumber <- Future.fromTry(PhoneNumber.parse(mobileNumber))

      airtableServiceUrl <- ConfigUtils.getEnvValueAsFuture(EnvVariables.AIRTABLE_SERVICE_URL)

      response <- wsClient.url(s"$airtableServiceUrl/${ConfigValues.AIRTABLE_FIND_STYLIST_PATH}")
        .post(Json.obj("mobileNumber" -> parsedMobileNumber.localNumber))

      airtableStylist <- Future.fromTry(deserialize[AirtableStylist](response.json))
    }
    yield airtableStylist

}
