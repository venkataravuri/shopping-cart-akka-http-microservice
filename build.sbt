import sbt.Keys._

enablePlugins(JavaAppPackaging)

lazy val commonSettings = Seq(
  name := "shopping-cart-akka-http-microservice",
  organization := "com.nikhu.ecommerce",
  version := "1.0",
  scalaVersion := "2.11.8",
  test in assembly := {}
)


scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {
  val akkaV = "2.4.10"
  val scalaTestV = "3.0.0"
  val log4jV = "2.6.2"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-stream" % akkaV,
    "com.typesafe.akka" %% "akka-http-experimental" % akkaV,
    "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaV,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaV,
    "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaV,
    "org.slf4j" % "slf4j-api" % "1.7.21",
    "com.typesafe.scala-logging" % "scala-logging_2.11" % "3.5.0",
    "com.typesafe.akka" % "akka-slf4j_2.11" % akkaV,
    "org.scalatest" %% "scalatest" % scalaTestV % "test"
  )
}

lazy val app = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    mainClass in assembly := Some("com.nikhu.ecommerce.shoppingcart.WebServer")
    // more settings here ...
  )

Revolver.settings