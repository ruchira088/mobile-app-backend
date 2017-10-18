package services.sms

import utils.GeneralUtils

import scala.concurrent.Future

class MockSmsService extends SmsService
{
  override def sendMessage(mobileNumber: PhoneNumber, textMessage: String): Future[String] =
  {
    val messageId = GeneralUtils.randomUuid()
    val consoleMessage =s"""
        | mobileNumber: $mobileNumber
        | textMessage: $textMessage
        | messageId: $messageId
      """.stripMargin

    println(consoleMessage)

    Future.successful(messageId)
  }
}
