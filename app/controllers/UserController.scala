package controllers

import javax.inject.{Inject, Singleton}

import controllers.requests.bodies.{AuthenticateUser, RegisterDeviceToken, RegisterUser}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import controllers.actions.{AuthenticatedAction, AuthorizedAction, Read, Write}
import services.airtable.AirtableService
import services.types.AuthToken
import services.{AuthenticationService, RegistrationService}
import utils.JsonUtils.deserialize

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserController @Inject()(
                                registrationService: RegistrationService,
                                authenticationService: AuthenticationService,
                                authenticatedAction: AuthenticatedAction,
                                authorizedAction: AuthorizedAction,
                                airtableService: AirtableService,
                                parser: PlayBodyParsers,
                                controllerComponents: ControllerComponents
    )(implicit executionContext: ExecutionContext)
  extends AbstractController(controllerComponents)
{
  def login(): Action[JsValue] = Action.async(parser.json) {
    implicit request: Request[JsValue] => for
      {
        AuthenticateUser(mobileNumber, passcode) <- Future.fromTry(deserialize[AuthenticateUser])
        authToken: AuthToken <- authenticationService.login(mobileNumber, passcode)
        _ = println(authToken)
      }
      yield Ok(Json.toJson(authToken))
  }

  def register(): Action[JsValue] = Action.async(parser.json) {
    implicit request: Request[JsValue] => for
      {
        RegisterUser(mobileNumber) <- Future.fromTry(deserialize[RegisterUser])
        airtableStylist <- airtableService.fetchStylist(mobileNumber)
        (_, stylist) <- registrationService.register(airtableStylist)
      }
      yield Ok(Json.obj("stylistId" -> stylist.id))
  }

  def bookings(stylistAirtableId: String): Action[AnyContent] =
    authenticatedAction.andThen(authorizedAction(Read, stylistAirtableId)).async {
      for {
        bookings <- airtableService.fetchBookings(stylistAirtableId)
      }
      yield Ok(Json.toJson(bookings))
    }

  def registerForPushNotifications(stylistAirtableId: String): Action[JsValue] =
    authenticatedAction.andThen(authorizedAction(Write, stylistAirtableId)).async(parser.json) {
      implicit request: Request[JsValue] => for
        {
          registerDeviceToken <- Future.fromTry(deserialize[RegisterDeviceToken])
          pushNotification <- registrationService.registerForPushNotifications(stylistAirtableId, registerDeviceToken)
          _ = println(registerDeviceToken.deviceToken)
        }
        yield Ok(Json.obj("pushNotificationId" -> pushNotification.id))
    }
}