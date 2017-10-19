package controllers.actions

import javax.inject.Inject

import controllers.requests.types.AuthenticatedRequest
import exceptions.{AuthorizationHeaderNotFoundException, InvalidTokenException}
import play.api.mvc._
import play.mvc.Http.HeaderNames
import services.AuthenticationService
import utils.ScalaUtils.toTry

import scala.concurrent.{ExecutionContext, Future}
import scala.util.matching.Regex
import scala.util.{Failure, Success, Try}

class AuthenticatedAction @Inject()(
                     parser: BodyParsers.Default,
                     authenticationService: AuthenticationService
)(implicit executionContext: ExecutionContext) extends ActionBuilderImpl[AnyContent](parser)
{
  override def invokeBlock[A](request: Request[A], block: Request[A] => Future[Result]): Future[Result] = for
    {
      bearerToken <- Future.fromTry(AuthenticatedAction.getBearerToken(request))

      authToken <- authenticationService.authenticate(bearerToken)

      result <- block(AuthenticatedRequest(authToken.stylist, request))
    }
    yield result
}

object AuthenticatedAction
{
  type AuthorizationToken = String

  private val AUTHORIZATION_SCHEME = "Bearer"

  private val tokenRegex: Regex = s"$AUTHORIZATION_SCHEME (\\S+)".r

  def extractToken(authorizationHeader: String): Try[AuthorizationToken] = authorizationHeader match {
    case tokenRegex(authorizationToken) => Success(authorizationToken.trim)
    case _ => Failure(InvalidTokenException)
  }

  def getBearerToken[_](request: Request[_]): Try[AuthorizationToken] = for {
      authorizationHeader <- toTry(request.headers.get(HeaderNames.AUTHORIZATION), AuthorizationHeaderNotFoundException)

      bearerToken <- extractToken(authorizationHeader)
    }
    yield bearerToken
}


