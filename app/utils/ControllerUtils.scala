package utils

import exceptions.FormValidationException
import play.api.libs.json.{JsValue, Reads}
import play.api.mvc.Request

import scala.util.{Failure, Success, Try}

object ControllerUtils
{
  def deserialize[A](implicit request: Request[JsValue], reads: Reads[A]): Try[A] =
    request.body.validate.fold(
      formErrors => Failure(FormValidationException(formErrors)),
      Success(_)
    )
}
