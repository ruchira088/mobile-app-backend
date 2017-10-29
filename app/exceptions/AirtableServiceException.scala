package exceptions

import play.api.libs.json.JsValue

case class AirtableServiceException(errorJson: JsValue) extends Exception
