package controllers.requests.types

import models.db.Stylist
import play.api.mvc.{Request, WrappedRequest}

case class AuthorizedRequest[A](
      authenticatedStylist: Stylist,
      stylist: Stylist,
      request: Request[A]
) extends WrappedRequest[A](request)
