package services

import javax.inject.{Inject, Singleton}

import dao.StylistDao
import exceptions._
import services.kvstore.KeyValueStore
import services.types.{AuthToken, Passcode, PhoneNumber}
import utils.{GeneralUtils, ScalaUtils}

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

  def insertAuthToken(authToken: AuthToken): Future[Boolean] =
    keyValueStore.set[AuthToken](authToken.bearerToken, authToken)

  def login(mobileNumber: String, code: String): Future[AuthToken] = for
    {
      phoneNumber <- Future.fromTry(PhoneNumber.parse(mobileNumber))

      passcode <- keyValueStore.get[Passcode](passcodeKey(phoneNumber))
          .flatten(PasscodeNotGeneratedException(phoneNumber))

      _ <- ScalaUtils.predicate(passcode.code == code, IncorrectPasscodeException)

      _ <- keyValueStore.delete(passcodeKey(phoneNumber))

      stylist <- stylistDao.findByMobileNumber(phoneNumber)
        .flatten(StylistNotFoundException("phoneNumber", phoneNumber.localNumber))

      authToken = AuthToken(GeneralUtils.randomUuid(), stylist)

      _ <- insertAuthToken(authToken)
    }
    yield authToken

  def authenticate(token: String): Future[AuthToken] = for {
    authToken <- keyValueStore.get[AuthToken](token).flatten(TokenNotFoundException)
  } yield authToken

}
