package dao
import javax.inject.{Inject, Singleton}

import models.Stylist
import org.joda.time.DateTime
import services.types.PhoneNumber
import utils.FutureO

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class InMemoryStylistDao @Inject()(implicit executionContext: ExecutionContext) extends StylistDao
{
  var collection: List[Stylist] = List.empty

  override def insert(stylist: Stylist) =
  {
    val startTime = DateTime.now()
    collection = collection :+ stylist

    Future.successful((DateTime.now().getMillis - startTime.getMillis).toInt)
  }

  override def findByMobileNumber(phoneNumber: PhoneNumber) =
    FutureO {
      Future.successful(collection.find(_.mobile == phoneNumber))
    }
}
