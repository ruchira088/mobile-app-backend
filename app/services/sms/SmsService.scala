package services.sms

import exceptions.InvalidMobileNumberException

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

trait SmsService
{
  type PhoneNumber = String

  val MOBILE_NUMBER_PREFIX = "+61"

  def sendMessage(mobileNumber: PhoneNumber, textMessage: String): Future[String]

  def sanitize(mobileNumber: PhoneNumber): Try[String] = mobileNumber match
    {
      case number if number.startsWith(MOBILE_NUMBER_PREFIX) && number.length == 12 => Success(number)
      case number if number.startsWith("4") && number.length == 9 => sanitize(s"$MOBILE_NUMBER_PREFIX$number")
      case number if number.startsWith("04") && number.length == 10 => sanitize(number.tail)
      case _ => Failure(InvalidMobileNumberException(mobileNumber))
    }
}
