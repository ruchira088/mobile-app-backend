package services.types

import constants.ConfigValues._
import exceptions.InvalidMobileNumberException

import scala.util.matching.Regex
import scala.util.{Failure, Success, Try}

case class PhoneNumber(localNumber: String, countryCode: String = DEFAULT_COUNTRY_CODE)
{
  phoneNumber =>

  def longFormat: String = s"$countryCode$localNumber"
}

object PhoneNumber
{
  private val SHORT_FORMAT: Regex = s"(\\d{$DEFAULT_LOCAL_MOBILE_NUMBER_LENGTH})".r

  private val MEDIUM_FORMAT: Regex = s"0(\\d{$DEFAULT_LOCAL_MOBILE_NUMBER_LENGTH})".r

  private val LONG_FORMAT: Regex = s"\\$DEFAULT_COUNTRY_CODE(\\d{$DEFAULT_LOCAL_MOBILE_NUMBER_LENGTH})".r

  def parse(phoneNumberStr: String): Try[PhoneNumber] = phoneNumberStr.filter(!_.isWhitespace) match {
    case SHORT_FORMAT(localNumber) => Success(PhoneNumber(localNumber))
    case MEDIUM_FORMAT(localNumber) => Success(PhoneNumber(localNumber))
    case LONG_FORMAT(localNumber) => Success(PhoneNumber(localNumber))
    case noMatch => Failure(InvalidMobileNumberException(noMatch))
  }
}