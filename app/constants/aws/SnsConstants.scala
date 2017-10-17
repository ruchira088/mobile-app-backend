package constants.aws

object SnsConstants
{
  private val ATTRIBUTE_PREFIX = "AWS.SNS.SMS"

  val SENDER_ID = s"$ATTRIBUTE_PREFIX.SenderID"

  val SMS_TYPE = s"$ATTRIBUTE_PREFIX.SMSType"

  val MAX_PRICE = s"$ATTRIBUTE_PREFIX.MaxPrice"

  val STRING_DATA_TYPE = "String"

  val NUMBER_DATA_TYPE = "Number"
}
