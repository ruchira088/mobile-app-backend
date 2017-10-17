package utils

import java.util.UUID

object GeneralUtils
{
  def randomUuid(): String = UUID.randomUUID().toString

  def passcode(length: Int): String = {
    val digits = randomUuid().filter(_.isDigit)

    if(digits.length >= length)
      digits.substring(0, length)
    else
      digits + passcode(length - digits.length)
  }
}
