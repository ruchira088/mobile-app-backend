package services.types

import constants.ConfigValues
import org.joda.time.DateTime
import play.api.libs.json.{Json, OFormat}
import utils.GeneralUtils
import utils.JsonUtils._

case class Passcode(phoneNumber: PhoneNumber, code: String, expireAt: DateTime)

object Passcode
{
  implicit val oFormat: OFormat[Passcode] = Json.format[Passcode]

  def apply(phoneNumber: PhoneNumber, passcodeLength: Int): Passcode =
    Passcode(
      phoneNumber,
      GeneralUtils.passcode(passcodeLength),
      DateTime.now().plus(ConfigValues.PASSCODE_LIFE_TIME.toMillis)
    )
}
