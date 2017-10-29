package exceptions

case class StylistNotFoundException[A](selectorField: String, value: A) extends Exception