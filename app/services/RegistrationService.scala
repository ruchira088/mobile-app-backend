package services

import javax.inject.{Inject, Singleton}

import constants.EnvVariables
import services.airtable.model.AirtableStylist
import services.sms.SmsService
import utils.ConfigUtils.getEnvValueAsFuture
import utils.GeneralUtils
import utils.ScalaUtils.convert

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class RegistrationService @Inject()(smsService: SmsService)(implicit executionContext: ExecutionContext)
{
  def register(airtableStylist: AirtableStylist) = for {
    passcodeLengthStr <- getEnvValueAsFuture(EnvVariables.PASSCODE_LENGTH)
    passcodeLength <- Future.fromTry(convert[String, Int](_.toInt)(passcodeLengthStr))
    passcode = GeneralUtils.passcode(passcodeLength)
  } yield ???

}
