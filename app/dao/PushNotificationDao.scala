package dao

import models.PushNotification

import scala.concurrent.Future

trait PushNotificationDao
{
  def insert(pushNotification: PushNotification): Future[PushNotification]
}
