package services.sms

import services.types.PhoneNumber

import scala.concurrent.Future

trait SmsService
{
  def sendMessage(phoneNumber: PhoneNumber, textMessage: String): Future[String]
}
