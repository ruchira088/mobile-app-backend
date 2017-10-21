package modules

import com.google.inject.AbstractModule
import constants.{EnvVariables, GeneralConstants}
import dao.{InMemoryStylistDao, MongoStylistDao, StylistDao}
import services.kvstore.{InMemoryKeyValueStore, KeyValueStore, RedisKeyValueStore}
import services.sms.{AwsSmsService, MockSmsService, SmsService}
import utils.ConfigUtils

class GuiceModule extends AbstractModule
{
  def configure() =
  {
    ConfigUtils.getEnvValue(EnvVariables.SCALA_ENV) match
    {
      case Some(GeneralConstants.PRODUCTION_ENV_VALUE) => {
        productionEnvBindings()
      }

      case Some(GeneralConstants.TEST_ENV_VALUE) => {
        testEnvBindings()
      }

      case _ => {
        developmentEnvBindings()
      }
    }
  }

  def integratedEnvBindings() =
  {
    bind(classOf[KeyValueStore]).to(classOf[RedisKeyValueStore])
    bind(classOf[StylistDao]).to(classOf[MongoStylistDao])
  }

  def productionEnvBindings() =
  {
    integratedEnvBindings()
    bind(classOf[SmsService]).to(classOf[AwsSmsService])

    println("Production environment bindings have been applied.")
  }

  def testEnvBindings() =
  {
    bind(classOf[SmsService]).to(classOf[MockSmsService])
    bind(classOf[KeyValueStore]).to(classOf[InMemoryKeyValueStore])
    bind(classOf[StylistDao]).to(classOf[InMemoryStylistDao])

    println("Test environment bindings have been applied.")
  }

  def developmentEnvBindings() =
  {
    integratedEnvBindings()
    bind(classOf[SmsService]).to(classOf[MockSmsService])

    println("Development environment bindings have been applied.")
  }
}
