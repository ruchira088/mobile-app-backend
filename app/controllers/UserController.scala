package controllers

import javax.inject.{Inject, Singleton}

import controllers.request.body.{AuthenticateUser, RegisterUser}
import controllers.response.AuthenticateResult
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import services.airtable.AirtableService
import utils.JsonUtils.deserialize

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserController @Inject()(
        airtableService: AirtableService,
        parser: PlayBodyParsers,
        controllerComponents: ControllerComponents
    )(implicit executionContext: ExecutionContext)
  extends AbstractController(controllerComponents)
{
  def authenticate(): Action[JsValue] = Action.async(parser.json) {
    implicit request: Request[JsValue] => for {
      authenticateUser <- Future.fromTry(deserialize[AuthenticateUser])
      _ = println(authenticateUser)
    } yield Ok(AuthenticateResult(success = true).toJson)
  }

  def register(): Action[JsValue] = Action.async(parser.json) {
    implicit request: Request[JsValue] => for {
      RegisterUser(mobileNumber) <- Future.fromTry(deserialize[RegisterUser])
      airtableStylist <- airtableService.fetchStylist(mobileNumber)
//      passcodeLength <- ConfigUtils.getEnvValueAsFuture(E)
    } yield Ok(Json.toJson(airtableStylist))
  }
}