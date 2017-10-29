package dao
import javax.inject.{Inject, Singleton}

import models.db.Stylist
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
    collection = collection :+ stylist
    Future.successful(1)
  }

  override def findByMobileNumber(phoneNumber: PhoneNumber): FutureO[Stylist] = find(_.mobile == phoneNumber)

  override def findByAirtableId(airtableId: String): FutureO[Stylist] = find(_.airtableId == airtableId)

  def find(query: Stylist => Boolean): FutureO[Stylist] =
  FutureO {
      Future.successful(collection.find(query))
    }

}
