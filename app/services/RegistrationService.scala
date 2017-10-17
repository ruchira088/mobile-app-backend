package services

import javax.inject.{Inject, Singleton}

import constants.EnvVariables
import dao.StylistDao
import models.Stylist
import services.airtable.model.AirtableStylist
import services.kvstore.KeyValueStore
import services.sms.SmsService
import utils.ConfigUtils.getEnvValueAsFuture
import utils.GeneralUtils
import utils.ScalaUtils.convert

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class RegistrationService @Inject()(smsService: SmsService, keyValueStore: KeyValueStore, stylistDao: StylistDao)
                                   (implicit executionContext: ExecutionContext)
{
  val PASSCODE_KEY_PREFIX = "passcode"

  def passcodeKey(stylist: Stylist): String =
    s"$PASSCODE_KEY_PREFIX-${stylist.mobile}"

  def register(airtableStylist: AirtableStylist): Future[(String, Stylist)] = for {

    passcodeLengthStr <- getEnvValueAsFuture(EnvVariables.PASSCODE_LENGTH)
    passcodeLength <- Future.fromTry(convert[String, Int](_.toInt)(passcodeLengthStr))
    passcode = GeneralUtils.passcode(passcodeLength)

    stylist = Stylist(airtableStylist)
    setPasscode = keyValueStore.set[String](passcodeKey(stylist), passcode)
    saveStylist = stylistDao.insert(stylist)
    sendSMS = smsService.sendMessage(stylist.mobile, passcode)

    _ <- Future.sequence(List(setPasscode, saveStylist, sendSMS))

  } yield (passcode, stylist)

}
