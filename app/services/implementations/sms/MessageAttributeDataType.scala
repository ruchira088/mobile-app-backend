package services.implementations.sms

sealed trait MessageAttributeDataType
{
  def getType: String
}

case object StringType extends MessageAttributeDataType
{
  def getType = "String"
}

case object NumberType extends MessageAttributeDataType
{
  def getType: String = "Number"
}
