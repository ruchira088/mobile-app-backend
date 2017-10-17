package dao

import javax.inject.{Inject, Singleton}

import models.Stylist
import play.api.libs.json.{Json, OFormat}
import play.modules.reactivemongo.ReactiveMongoApi
import services.MongoCollection
import utils.JsonUtils._

import scala.concurrent.ExecutionContext

@Singleton
class MongoStylistDao @Inject()(val reactiveMongoApi: ReactiveMongoApi)(implicit executionContext: ExecutionContext)
  extends MongoCollection[Stylist] with StylistDao
{
  override def collectionName = "stylists"

  override implicit val oFormat: OFormat[Stylist] = Json.format[Stylist]
}