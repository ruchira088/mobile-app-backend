package services

import constants.ConfigValues._
import play.api.libs.json.{JsObject, OFormat}
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.Cursor
import reactivemongo.play.json.collection.JSONCollection
import play.modules.reactivemongo.json._

import scala.concurrent.{ExecutionContext, Future}

class MongoCollection[A](collectionName: String, reactiveMongoApi: ReactiveMongoApi)(implicit oFormat: OFormat[A])
{
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
