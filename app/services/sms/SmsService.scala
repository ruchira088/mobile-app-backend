package services.sms

import scala.concurrent.Future

trait SmsService
{
  type PhoneNumber = String

  def sendMessage(mobileNumber: PhoneNumber, textMessage: String): Future[String]
}
