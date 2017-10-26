package services.notifications

import javax.inject.{Inject, Singleton}

import constants.EnvVariables
import controllers.requests.bodies.RegisterDeviceToken
import models.Stylist
import play.api.libs.ws.WSClient
import services.notifications.model.AccountRequest
import utils.ConfigUtils

import scala.concurrent.ExecutionContext

@Singleton
class PushNotificationService @Inject()(wsClient: WSClient)(implicit executionContext: ExecutionContext)
{
  def register(stylist: Stylist, registerDeviceToken: RegisterDeviceToken) = for
    {
      pushNotificationServiceUrl <-ConfigUtils.getEnvValueAsFuture(EnvVariables.PUSH_NOTIFICATION_SERVICE_URL)

      requestBody = AccountRequest(
        registerDeviceToken.deviceToken,
        stylist.state,
        stylist.suburb,
        stylist.airtableId,
        stylist.mobile.longFormat,
        stylist.email
      )

      response <- wsClient.url(s"$pushNotificationServiceUrl/account")
        .post(requestBody.toJsObject)
    }
    yield (): Unit
}
