package models

import org.joda.time.DateTime

case class AuthToken(bearerToken: String, expireAt: DateTime)
