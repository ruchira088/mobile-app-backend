package constants

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.duration._

object ConfigValues
{
  val DEFAULT_SMS_SENDER_ID = "Flayr"

  val DEFAULT_SMS_TYPE = "Transactional"

  val DEFAULT_SMS_MAX_PRICE = "0.50"

  val AIRTABLE_FIND_STYLIST_PATH = "find"

  val DEFAULT_PASSCODE_LENGTH = 6

  val MONGO_MAX_QUERY_RESULTS_SIZE: Int = -1

  val DEFAULT_REDIS_HOST = "localhost"

  val DEFAULT_REDIS_PORT = 6379

  val DEFAULT_COUNTRY_CODE = "+61"

  val DEFAULT_LOCAL_MOBILE_NUMBER_LENGTH = 9

  val PASSCODE_LIFE_TIME: FiniteDuration = 5 minutes
}
