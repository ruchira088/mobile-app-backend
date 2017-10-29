package dao

import models.db.PushNotification

import scala.concurrent.Future

trait PushNotificationDao
{
  def insert(pushNotification: PushNotification): Future[PushNotification]
}
