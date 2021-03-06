package services

import javax.inject.{Inject, Singleton}

import constants.{ConfigValues, EnvVariables}
import controllers.requests.bodies.RegisterDeviceToken
import dao.{PushNotificationDao, StylistDao}
import exceptions.UndefinedEnvValueException
import models.db.{PushNotification, Stylist}
import org.joda.time.DateTime
import services.airtable.model.AirtableStylist
import services.notifications.PushNotificationService
import services.sms.SmsService
import services.types.Passcode
import utils.ConfigUtils.getEnvValueAsFuture
import utils.GeneralUtils
import utils.ScalaUtils.convert

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class RegistrationService @Inject()(
         smsService: SmsService,
         authenticationService: AuthenticationService,
         stylistDao: StylistDao,
         pushNotificationService: PushNotificationService,
         pushNotificationDao: PushNotificationDao)(implicit executionContext: ExecutionContext)
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
      case _ : UndefinedEnvValueException => ConfigValues.DEFAULT_PASSCODE_LENGTH
    }

    stylist <- Future.fromTry(Stylist(airtableStylist))

    passcode = Passcode(stylist.mobile, passcodeLength)
    savePasscode = authenticationService.insertPasscode(passcode)

    saveStylist = stylistDao.insert(stylist)

    sendSMS = smsService.sendMessage(stylist.mobile, passcode.code)

    _ <- Future.sequence(List(savePasscode, saveStylist, sendSMS))

  } yield (passcode, stylist)

  def registerForPushNotifications(stylist: Stylist, registerDeviceToken: RegisterDeviceToken) =
    Future.sequence(
      List(
        pushNotificationDao.insert(
          PushNotification(
            GeneralUtils.randomUuid(),
            DateTime.now(),
            registerDeviceToken.stylistId,
            stylist.airtableId,
            registerDeviceToken.deviceToken
          )
        ),
        pushNotificationService.register(stylist, registerDeviceToken)
      )
    )
}
