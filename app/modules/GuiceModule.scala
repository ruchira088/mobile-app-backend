package modules

import com.google.inject.AbstractModule
import dao.{MongoStylistDao, StylistDao}
import services.kvstore.{KeyValueStore, RedisKeyValueStore}
import services.sms.{AwsSmsService, SmsService}

class GuiceModule extends AbstractModule
{
  def configure() =
  {
    bind(classOf[SmsService]).to(classOf[AwsSmsService])
    bind(classOf[KeyValueStore]).to(classOf[RedisKeyValueStore])
    bind(classOf[StylistDao]).to(classOf[MongoStylistDao])
  }
}
