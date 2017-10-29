package modules

import com.google.inject.AbstractModule
import constants.EnvVariables
import constants.GeneralConstants._
import dao._
import models.AppConfiguration
import services.kvstore.{InMemoryKeyValueStore, KeyValueStore, RedisKeyValueStore}
import services.sms.{AwsSmsService, MockSmsService, SmsService}
import utils.ConfigUtils
import utils.ConfigUtils.KeyValuePair

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class GuiceModule extends AbstractModule
{
  def configure() =
  {
    ConfigUtils.getEnvValue(EnvVariables.SCALA_ENV) match
    {
      case Some(PRODUCTION_ENV_VALUE) => {
        productionEnvBindings()
      }

      case Some(LOCAL_TEST_ENV_VALUE) => {
        testEnvBindings()
      }

      case _ => {
        developmentEnvBindings()
      }
    }
  }

  def bindAppConfiguration(environment: String) =
  {
    val appConfigValues = for
      {
        applicationInfo <- ConfigUtils.getApplicationInfo()
        keyValuePairs = applicationInfo :+ KeyValuePair("environment", environment)
      }
      yield AppConfiguration(keyValuePairs)

    bind(classOf[AppConfiguration]).toInstance(Await.result(appConfigValues, 30 seconds))
  }

  def integratedEnvBindings() =
  {
    bind(classOf[KeyValueStore]).to(classOf[RedisKeyValueStore])
    bind(classOf[StylistDao]).to(classOf[MongoStylistDao])
    bind(classOf[PushNotificationDao]).to(classOf[MongoPushNotificationDao])
  }

  def productionEnvBindings() =
  {
    integratedEnvBindings()
    bind(classOf[SmsService]).to(classOf[AwsSmsService])
    bindAppConfiguration(PRODUCTION_ENV_VALUE)

    println("Production environment bindings have been applied.")
  }

  def testEnvBindings() =
  {
    bind(classOf[SmsService]).to(classOf[MockSmsService])
    bind(classOf[KeyValueStore]).to(classOf[InMemoryKeyValueStore])
    bind(classOf[StylistDao]).to(classOf[InMemoryStylistDao])
    bindAppConfiguration(LOCAL_TEST_ENV_VALUE)

    println("Test environment bindings have been applied.")
  }

  def developmentEnvBindings() =
  {
    integratedEnvBindings()
    bind(classOf[SmsService]).to(classOf[MockSmsService])
    bindAppConfiguration(DEVELOPMENT_ENV_VALUE)

    println("Development environment bindings have been applied.")
  }
}
