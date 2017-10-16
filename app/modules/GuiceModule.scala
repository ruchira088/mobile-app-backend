package modules

import com.google.inject.AbstractModule
import services.SmsService
import services.implementations.sms.AwsSmsService

class GuiceModule extends AbstractModule
{
  override def configure() =
  {
    bind(classOf[SmsService]).to(classOf[AwsSmsService])
  }
}
