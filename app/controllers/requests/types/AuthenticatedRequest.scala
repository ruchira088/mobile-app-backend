package controllers.requests.types

import models.db.Stylist
import play.api.mvc.{Request, WrappedRequest}

case class AuthenticatedRequest[A](authenticatedStylist: Stylist, request: Request[A])
  extends WrappedRequest[A](request)