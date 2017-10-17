package dao

import javax.inject.{Inject, Singleton}

import models.Stylist
import play.modules.reactivemongo.ReactiveMongoApi
import services.MongoCollection

import scala.concurrent.ExecutionContext

@Singleton
class MongoStylistDao @Inject()(val reactiveMongoApi: ReactiveMongoApi)(implicit executionContext: ExecutionContext)
  extends MongoCollection[Stylist]
{
  override def collectionName = "stylists"
}