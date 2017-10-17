package exceptions

case class InvalidMobileNumberException(mobileNumber: String) extends Exception