package services.implementations

import javax.inject.{Inject, Singleton}

import constants.{ConfigValues, EnvVariables}
import play.api.libs.json.{JsValue, Json}
import play.api.libs.ws.WSClient
import utils.ConfigUtils

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AirtableService @Inject()(wsClient: WSClient)(implicit executionContext: ExecutionContext)
{
  def fetchStylist(mobileNumber: String): Future[JsValue] = for {
    airtableServiceUrl <- ConfigUtils.getEnvValueAsFuture(EnvVariables.AIRTABLE_SERVICE_URL)
    response <- wsClient.url(s"$airtableServiceUrl/${ConfigValues.AIRTABLE_FIND_STYLIST_PATH}")
      .post(Json.obj("mobileNumber" -> mobileNumber))
  } yield response.json

}
