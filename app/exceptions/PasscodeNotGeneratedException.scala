package exceptions

import services.types.PhoneNumber

case class PasscodeNotGeneratedException(phoneNumber: PhoneNumber) extends Exception
