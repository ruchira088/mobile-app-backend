name := "mobile-app-backend"
organization := "com.ruchij"

version := "0.0.1"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.3"

libraryDependencies ++= Seq(guice, ws)
libraryDependencies += "com.amazonaws" % "aws-java-sdk-sns" % "1.11.213"
libraryDependencies += "org.reactivemongo" % "play2-reactivemongo_2.12" % "0.12.7-play26"
libraryDependencies += "com.github.etaty" % "rediscala_2.12" % "1.8.0"
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test


// Adds additional packages into Twirl
//TwirlKeys.templateImports += "au.com.flayr.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "au.com.flayr.binders._"
