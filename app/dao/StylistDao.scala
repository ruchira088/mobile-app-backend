package dao

import models.Stylist

import scala.concurrent.{ExecutionContext, Future}

trait StylistDao
{
  def insert(stylist: Stylist)(implicit executionContext: ExecutionContext): Future[Int]
}
