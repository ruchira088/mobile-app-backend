package services.sms

import services.types.PhoneNumber
import utils.GeneralUtils

import scala.concurrent.Future

class MockSmsService extends SmsService
{
  override def sendMessage(phoneNumber: PhoneNumber, textMessage: String): Future[String] =
  {
    val messageId = GeneralUtils.randomUuid()
    val consoleMessage =s"""
        | mobileNumber: ${phoneNumber.longFormat}
        | textMessage: $textMessage
        | messageId: $messageId
      """.stripMargin

    println(consoleMessage)

    Future.successful(messageId)
  }
}
