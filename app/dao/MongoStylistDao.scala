package dao

import javax.inject.{Inject, Singleton}

import models.Stylist
import play.api.libs.json.{Json, OFormat}
import play.modules.reactivemongo.ReactiveMongoApi
import services.MongoCollection
import services.types.PhoneNumber
import utils.FutureO

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class MongoStylistDao @Inject()(val reactiveMongoApi: ReactiveMongoApi)(implicit executionContext: ExecutionContext)
  extends StylistDao with MongoCollection[Stylist]
{
  override def collectionName = "stylists"

  override implicit val oFormat: OFormat[Stylist] = Stylist.oFormat

  override def findByMobileNumber(phoneNumber: PhoneNumber): FutureO[Stylist] =
    FutureO {
      for {
        stylists <- query(Json.obj("mobile" -> Json.toJson(phoneNumber)))
      } yield stylists.headOption
    }

  override def insert(stylist: Stylist): Future[Int] = insertItem(stylist)
}