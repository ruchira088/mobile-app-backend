package services

import javax.inject.{Inject, Singleton}

import controllers.actions.ActionType
import models.Stylist

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AuthorizationService @Inject()(implicit executionContext: ExecutionContext)
{
  def isAuthorized(stylistAirtableId: String)(stylist: Stylist, actionType: ActionType): Future[Boolean] =
    Future.successful(stylistAirtableId == stylist.airtableId)
}
