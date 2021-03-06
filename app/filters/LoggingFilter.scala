package filters

import javax.inject.Inject

import akka.stream.Materializer
import play.api.Logger
import play.api.mvc.{Filter, RequestHeader, Result}

import scala.concurrent.{ExecutionContext, Future}

class LoggingFilter @Inject()(implicit executionContext: ExecutionContext, val mat: Materializer) extends Filter
{
  val logger = Logger(this.getClass)

  override def apply(nextFilter: RequestHeader => Future[Result])(requestHeader: RequestHeader): Future[Result] =
  {
    val startTime = System.currentTimeMillis()

    logger.info(s"requestId: ${requestHeader.id}, ${requestHeader.method} ${requestHeader.uri} from ${requestHeader.remoteAddress}")

    for {
      result <- nextFilter(requestHeader)
      _ = logger.info(s"requestId: ${requestHeader.id}, requestDuration: ${System.currentTimeMillis() - startTime}ms")
    }
    yield result
  }
}
