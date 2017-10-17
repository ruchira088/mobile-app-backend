package services

import constants.ConfigValues._
import play.api.libs.json._
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json._
import reactivemongo.api.Cursor
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.{ExecutionContext, Future}

trait MongoCollection[A]
{
  implicit val oFormat: OFormat[A]

  def reactiveMongoApi: ReactiveMongoApi

  def collectionName: String

  def getCollection(implicit executionContext: ExecutionContext): Future[JSONCollection] =
    reactiveMongoApi.database.map(_.collection[JSONCollection](collectionName))

  def insert(item: A)(implicit executionContext: ExecutionContext): Future[Int] = for {
      collection <- getCollection
      writeResult <- collection.insert(item)
    } yield writeResult.n

  def query(jsObject: JsObject)(implicit executionContext: ExecutionContext): Future[List[A]] = for {
      collection <- getCollection
      items <- collection.find(jsObject).cursor[A]()
        .collect[List](
          MONGO_MAX_QUERY_RESULTS_SIZE,
          Cursor.FailOnError[List[A]]()
        )
    } yield items

}
