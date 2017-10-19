package controllers.requests.types

import models.Stylist
import play.api.mvc.{Request, WrappedRequest}

case class AuthenticatedRequest[A](stylist: Stylist, request: Request[A]) extends WrappedRequest[A](request)