ThisBuild / organization := "com.example"
ThisBuild / scalaVersion := "3.7.1"
ThisBuild / version := "0.1.0-SNAPSHOT"

lazy val root = project
  .in(file("."))
  .settings(
    name := "junit-jupiter-starter-sbt",
    libraryDependencies ++= Seq(
      "net.aichler" % "jupiter-interface" % JupiterKeys.jupiterVersion.value % Test,
      "org.junit.jupiter" % "junit-jupiter" % "6.0.0-M1" % Test,
      "org.junit.platform" % "junit-platform-launcher" % "6.0.0-M1" % Test,
    ),
    testOptions += Tests.Argument(jupiterTestFramework, "--display-mode=tree"),
    resolvers += "Maven Central Snapshots" at "https://central.sonatype.com/repository/maven-snapshots"
  )
