package dao

import models.db.Stylist
import services.types.PhoneNumber
import utils.FutureO

import scala.concurrent.{ExecutionContext, Future}

trait StylistDao
{
  def insert(stylist: Stylist): Future[Int]

  def findByMobileNumber(phoneNumber: PhoneNumber): FutureO[Stylist]

  def findByAirtableId(airtableId: String): FutureO[Stylist]
}
