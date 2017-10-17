package modules

import com.google.inject.AbstractModule
import services.sms.{AwsSmsService, SmsService}

class GuiceModule extends AbstractModule
{
  override def configure() =
  {
    bind(classOf[SmsService]).to(classOf[AwsSmsService])
  }
}
