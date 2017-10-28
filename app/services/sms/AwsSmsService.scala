package services.sms

import javax.inject.{Inject, Singleton}

import akka.actor.ActorSystem
import com.amazonaws.services.sns.model.{MessageAttributeValue, PublishRequest}
import com.amazonaws.services.sns.{AmazonSNSAsync, AmazonSNSAsyncClientBuilder}
import constants.aws.SnsConstants
import constants.{ConfigValues, EnvVariables}
import services.types.PhoneNumber
import utils.ConfigUtils._
import utils.ScalaUtils

import scala.collection.JavaConverters._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AwsSmsService @Inject()(implicit executionContext: ExecutionContext, actorSystem: ActorSystem) extends SmsService
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

  override def sendMessage(phoneNumber: PhoneNumber, textMessage: String): Future[String] =
  {
    val publishRequest = new PublishRequest()
      .withPhoneNumber(phoneNumber.longFormat)
      .withMessage(textMessage)
      .withMessageAttributes(messageAttributes().asJava)

    for {
      publishResult <- ScalaUtils.simpleConversionToScalaFuture(
        asyncSnsClient.publishAsync(publishRequest)
      )
    } yield publishResult.getMessageId
  }
}
