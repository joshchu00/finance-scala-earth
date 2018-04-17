name := "finance-scala-earth"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.12.5"

val akkaVersion = "2.5.12"

lazy val earth = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies ++= Seq(
  ehcache,
  guice,
  ws,
  "com.typesafe.akka" %% "akka-stream-kafka" % "0.16",
  "org.jsoup" % "jsoup" % "1.10.3"
)

dockerRepository := Some("127.0.0.1:5000")
dockerBaseImage := "openjdk:8u141-jre"
dockerExposedPorts in Docker := Seq(9000, 9443)
