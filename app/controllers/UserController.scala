package controllers

import javax.inject.{Inject, Singleton}

import controllers.request.body.AuthenticateUser
import controllers.response.AuthenticateResult
import play.api.libs.json.JsValue
import play.api.mvc._
import utils.ControllerUtils.deserialize

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserController @Inject()(parser: PlayBodyParsers, controllerComponents: ControllerComponents)
                              (implicit executionContext: ExecutionContext)
  extends AbstractController(controllerComponents)
{
  def authenticate(): Action[JsValue] = Action.async(parser.json) {
    implicit request: Request[JsValue] => for {
      authenticateUser <- Future.fromTry(deserialize[AuthenticateUser])
      _ = println(authenticateUser)
    } yield Ok(AuthenticateResult(success = true).toJson)
  }
}