package dao

import javax.inject.{Inject, Singleton}

import models.PushNotification
import play.api.libs.json.OFormat
import play.modules.reactivemongo.ReactiveMongoApi
import services.MongoCollection

import scala.concurrent.ExecutionContext

@Singleton
class MongoPushNotificationDao @Inject()(val reactiveMongoApi: ReactiveMongoApi)
                                        (implicit executionContext: ExecutionContext)
  extends PushNotificationDao with MongoCollection[PushNotification]
{
  override implicit val oFormat: OFormat[PushNotification] = PushNotification.oFormat

  override def collectionName: String = "pushNotifications"

  override def insert(pushNotification: PushNotification) =
    insertItem(pushNotification).map(_ => pushNotification)
}
