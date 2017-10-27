package utils

import constants.ErrorMessages._
import exceptions._
import play.api.libs.json.{JsObject, Json}
import play.api.mvc.Result
import play.api.mvc.Results._

object ErrorUtils
{
  private def errorJson(errorMessage: String): JsObject = Json.obj("error" -> errorMessage)

  def responseErrorHandler: PartialFunction[Throwable, Result] =
  {
    case exception: JsonDeserializationException =>
      UnprocessableEntity(errorJson(MISSING_INVALID_FIELDS.format(exception.getMessage)))
    case _ : PasscodeNotGeneratedException => Unauthorized(errorJson(PASSCODE_NOT_FOUND))
    case IncorrectPasscodeException => Unauthorized(errorJson(INCORRECT_PASSCODE))
    case StylistNotFoundException(selectorField, value) =>
      NotFound(errorJson(STYLIST_NOT_FOUND.format(selectorField, value.toString)))
    case UnableToSetValueInKeyStoreException => ServiceUnavailable(errorJson(KEY_VALUE_STORE_ERROR))
    case _ : UnableToInsertItemException => ServiceUnavailable(errorJson(DATABASE_ERROR))
    case AuthorizationHeaderNotFoundException => Unauthorized(errorJson(MISSING_AUTHORIZATION_HEADER))
    case InvalidBearerTokenException => Unauthorized(errorJson(INVALID_TOKEN))
    case UnauthorizedActionException => Unauthorized(errorJson(UNAUTHORIZED_ACTION))
    case InvalidMobileNumberException(invalidMobileNumber) =>
      UnprocessableEntity(errorJson(INVALID_MOBILE_NUMBER.format(invalidMobileNumber)))
    case TokenNotFoundException => Unauthorized(errorJson(UNAUTHENTICATED_USER))
    case AirtableServiceException(error) => BadGateway(error)
    case exception => InternalServerError(errorJson(exception.getMessage))
  }
}