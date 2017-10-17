package dao

import javax.inject.{Inject, Singleton}

import models.Stylist
import play.modules.reactivemongo.ReactiveMongoApi
import services.MongoCollection

import scala.concurrent.ExecutionContext

@Singleton
class MongoStylistDao @Inject()(reactiveMongoApi: ReactiveMongoApi)(implicit executionContext: ExecutionContext)
  extends MongoCollection[Stylist](MongoStylistDao.COLLECTION_NAME, reactiveMongoApi)
{


}

object MongoStylistDao
{
  val COLLECTION_NAME = "stylists"
}
