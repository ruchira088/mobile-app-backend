package controllers.actions

sealed trait ActionType

case object Read extends ActionType

case object Write extends ActionType
