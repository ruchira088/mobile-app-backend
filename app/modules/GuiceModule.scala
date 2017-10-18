package modules

import com.google.inject.AbstractModule
import constants.{EnvVariables, GeneralConstants}
import constants.GeneralConstants._
import dao.{MongoStylistDao, StylistDao}
import services.kvstore.{KeyValueStore, RedisKeyValueStore}
import services.sms.{AwsSmsService, MockSmsService, SmsService}
import utils.ConfigUtils

class GuiceModule extends AbstractModule
{
  def configure() =
  {
    commonBindings()

    ConfigUtils.getEnvValue(EnvVariables.SCALA_ENV) match {
      case Some(`PRODUCTION_ENV_VALUE`) => productionEnvBindings()
      case _ => developmentEnvBindings()
    }
  }

  def developmentEnvBindings() =
  {
    bind(classOf[SmsService]).to(classOf[MockSmsService])

    println("Development environment bindings have been applied.")
  }

  def commonBindings() =
  {
    bind(classOf[KeyValueStore]).to(classOf[RedisKeyValueStore])
    bind(classOf[StylistDao]).to(classOf[MongoStylistDao])
  }

  def productionEnvBindings() =
  {
    bind(classOf[SmsService]).to(classOf[AwsSmsService])

    println("Production environment bindings have been applied.")
  }
}
