name := "earth"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.11.8"

val akkaVersion = "2.5.2"

lazy val earth = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies ++= Seq(
  cache,
  ws,
  "com.typesafe.akka" %% "akka-stream-kafka" % "0.16"
)

dockerRepository := Some("joshchu00.no-ip.info:25000")
dockerBaseImage := "openjdk:8u121-jre"
dockerExposedPorts in Docker := Seq(9000, 9443)
