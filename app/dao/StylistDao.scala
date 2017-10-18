package dao

import models.Stylist
import services.types.PhoneNumber
import utils.FutureO

import scala.concurrent.{ExecutionContext, Future}

trait StylistDao
{
  def insert(stylist: Stylist)(implicit executionContext: ExecutionContext): Future[Int]

  def findByMobileNumber(phoneNumber: PhoneNumber): FutureO[Stylist]
}
