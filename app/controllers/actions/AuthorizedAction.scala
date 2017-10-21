package controllers.actions

import javax.inject.Inject

import controllers.requests.types.AuthenticatedRequest
import exceptions.UnauthorizedActionException
import play.api.mvc._
import services.AuthorizationService
import utils.ScalaUtils

import scala.concurrent.{ExecutionContext, Future}

class AuthorizedAction @Inject()(
            parser: BodyParsers.Default,
            authorizationService: AuthorizationService
)(implicit executionContext: ExecutionContext)
{
  def apply(actionType: ActionType, stylistAirtableId: String): ActionBuilder[Request, AnyContent] =
    new ActionBuilderImpl[AnyContent](parser)
    {
      override def invokeBlock[A](wrappedRequest: Request[A], block: Request[A] => Future[Result]): Future[Result] =
        wrappedRequest match {
          case AuthenticatedRequest(stylist, _) => for
            {
              isAuthorized <- authorizationService.isAuthorized(stylistAirtableId)(stylist, actionType)

              _ <- ScalaUtils.predicate(isAuthorized, UnauthorizedActionException)

              result <- block(wrappedRequest)
            }
            yield result
        }
    }
}
