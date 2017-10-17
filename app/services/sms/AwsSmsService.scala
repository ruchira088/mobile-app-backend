package services.sms

import javax.inject.{Inject, Singleton}

import com.amazonaws.services.sns.model.{MessageAttributeValue, PublishRequest}
import com.amazonaws.services.sns.{AmazonSNSAsync, AmazonSNSAsyncClientBuilder}
import constants.{ConfigValues, EnvVariables}
import constants.aws.SnsConstants
import utils.ConfigUtils._
import utils.ScalaUtils

import scala.concurrent.{ExecutionContext, Future}
import scala.collection.JavaConverters._

@Singleton
class AwsSmsService @Inject()(implicit executionContext: ExecutionContext) extends SmsService
{
  val asyncSnsClient: AmazonSNSAsync = AmazonSNSAsyncClientBuilder.defaultClient()

  private def messageAttributeValue(stringValue: String, dataType: MessageAttributeDataType): MessageAttributeValue =
    new MessageAttributeValue().withStringValue(stringValue).withDataType(dataType.getType)

  def senderId(): String =
    getEnvValueOrDefault(EnvVariables.SMS_SENDER_ID, ConfigValues.DEFAULT_SMS_SENDER_ID)

  def smsType(): String =
    getEnvValueOrDefault(EnvVariables.SMS_TYPE, ConfigValues.DEFAULT_SMS_TYPE)

  def maxSmsPrice(): String =
    getEnvValueOrDefault(EnvVariables.SMS_MAX_PRICE, ConfigValues.DEFAULT_SMS_MAX_PRICE)

  def messageAttributes(): Map[String, MessageAttributeValue] = Map(
    SnsConstants.SENDER_ID -> messageAttributeValue(senderId(), StringType),
    SnsConstants.SMS_TYPE -> messageAttributeValue(smsType(), StringType),
    SnsConstants.MAX_PRICE -> messageAttributeValue(maxSmsPrice(), NumberType)
  )

  override def sendMessage(mobileNumber: PhoneNumber, textMessage: String): Future[String] =
  {
    for {
      sanitizedPhoneNumber <- Future.fromTry(sanitize(mobileNumber))

      publishRequest = new PublishRequest()
        .withPhoneNumber(sanitizedPhoneNumber)
        .withMessage(textMessage)
        .withMessageAttributes(messageAttributes().asJava)

      publishResult <- ScalaUtils.simpleConversionToScalaFuture(asyncSnsClient.publishAsync(publishRequest))

    } yield publishResult.getMessageId
  }
}
