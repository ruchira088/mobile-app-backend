package controllers.actions

import javax.inject.Inject

import controllers.requests.types.{AuthenticatedRequest, AuthorizedRequest}
import dao.StylistDao
import exceptions.{InvalidAirtableStylistIdException, UnauthorizedActionException}
import play.api.mvc._
import services.AuthorizationService
import utils.ScalaUtils

import scala.concurrent.{ExecutionContext, Future}

class AuthorizedAction @Inject()(
            stylistDao: StylistDao,
            parser: BodyParsers.Default,
            authorizationService: AuthorizationService
)(implicit executionContext: ExecutionContext)
{
  def apply(actionType: ActionType, stylistAirtableId: String): ActionBuilder[Request, AnyContent] =
    new ActionBuilderImpl[AnyContent](parser)
    {
      override def invokeBlock[A](wrappedRequest: Request[A], block: Request[A] => Future[Result]): Future[Result] =
        wrappedRequest match {
          case AuthenticatedRequest(authenticatedStylist, _) => for
            {
              isAuthorized <- authorizationService.isAuthorized(stylistAirtableId)(authenticatedStylist, actionType)

              _ <- ScalaUtils.predicate(isAuthorized, UnauthorizedActionException)

              stylist <- {
                if (stylistAirtableId == authenticatedStylist.airtableId)
                  Future.successful(authenticatedStylist)
                else
                  stylistDao.findByAirtableId(stylistAirtableId)
                    .flatten(InvalidAirtableStylistIdException(stylistAirtableId))
              }

              result <- block(AuthorizedRequest(authenticatedStylist, stylist, wrappedRequest))
            }
            yield result
        }
    }
}
