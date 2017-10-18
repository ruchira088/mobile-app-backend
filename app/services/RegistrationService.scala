package services

import javax.inject.{Inject, Singleton}

import constants.{ConfigValues, EnvVariables}
import dao.StylistDao
import exceptions.UndefinedEnvValueException
import models.Stylist
import services.airtable.model.AirtableStylist
import services.sms.SmsService
import services.types.Passcode
import utils.ConfigUtils.getEnvValueAsFuture
import utils.ScalaUtils.convert

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class RegistrationService @Inject()(smsService: SmsService, authenticationService: AuthenticationService, stylistDao: StylistDao)
                                   (implicit executionContext: ExecutionContext)
{
  val PASSCODE_KEY_PREFIX = "passcode"

  def passcodeKey(stylist: Stylist): String =
    s"$PASSCODE_KEY_PREFIX-${stylist.mobile}"

  private def getPasscodeLength(): Future[Int] = for {
    passcodeLengthStr <- getEnvValueAsFuture(EnvVariables.PASSCODE_LENGTH)
    length <- Future.fromTry(convert[String, Int](_.toInt)(passcodeLengthStr))
  } yield length

  def register(airtableStylist: AirtableStylist): Future[(Passcode, Stylist)] = for {

    passcodeLength <- getPasscodeLength().recover {
      case UndefinedEnvValueException(_) => ConfigValues.DEFAULT_PASSCODE_LENGTH
    }

    stylist <- Future.fromTry(Stylist(airtableStylist))
    passcode = Passcode(stylist.mobile, passcodeLength)

    savePasscode = authenticationService.insertPasscode(passcode)
    saveStylist = stylistDao.insert(stylist)
    sendSMS = smsService.sendMessage(stylist.mobile, passcode.code)

    _ <- Future.sequence(List(savePasscode, saveStylist, sendSMS))

  } yield (passcode, stylist)

}
