package utils

import play.api.http.Status._
import play.api.libs.ws.WSResponse

object ServiceUtils
{
  def httpServiceResponseHandler(response: WSResponse) = response.status match
    {
      case NOT_FOUND => ""

    }
}
