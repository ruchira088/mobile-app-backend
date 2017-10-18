package services

import javax.inject.{Inject, Singleton}

import dao.StylistDao
import services.kvstore.KeyValueStore
import services.types.{Passcode, PhoneNumber}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AuthenticationService @Inject()(keyValueStore: KeyValueStore, stylistDao: StylistDao)
                                     (implicit executionContext: ExecutionContext)
{
  val PASSCODE_KEY_PREFIX = "passcode"

  def passcodeKey(phoneNumber: PhoneNumber): String =
    s"$PASSCODE_KEY_PREFIX-${phoneNumber.localNumber}"

  def insertPasscode(passcode: Passcode): Future[Boolean] =
    keyValueStore.set[Passcode](passcodeKey(passcode.phoneNumber), passcode)

}
