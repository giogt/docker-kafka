val scala212 = "2.12.6"

val sharedOptions = Seq(
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-explaintypes",
  "-Yrangepos",
  "-feature",
  "-Xfuture",
  "-Ypartial-unification",
  "-language:higherKinds",
  "-language:existentials",
  "-unchecked",
  "-Yno-adapted-args",
  "-Xlint:_,-type-parameter-shadow",
  "-Xsource:2.13",
  "-Ywarn-dead-code",
  "-Ywarn-inaccessible",
  "-Ywarn-infer-any",
  "-Ywarn-nullary-override",
  "-Ywarn-nullary-unit",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Xfatal-warnings",
  "-opt:l:inline",
  "-Ywarn-unused:imports",
  "-Ywarn-unused:_,imports",
  "-opt-warnings",
  "-Xlint:constant",
  "-Ywarn-extra-implicit",
  "-opt-inline-from:<source>"
)

val versionOf = new {
  val logbackClassic = "1.2.3"
  val kafka          = "2.0.0"
  val scalaCheck     = "1.14.0"
  val scalaTest      = "3.0.5"
}

val testDependencies = Seq(
  "ch.qos.logback"   % "logback-classic" % versionOf.logbackClassic % Test,
  "org.apache.kafka" %% "kafka"          % versionOf.kafka          % Test,
  "org.scalacheck"   %% "scalacheck"     % versionOf.scalaCheck     % Test,
  "org.scalatest"    %% "scalatest"      % versionOf.scalaTest      % Test
)

val sharedSettings = Seq(
  organization := "org.giogt",
  resolvers    += "Confluent" at "http://packages.confluent.io/maven/"
)

val kafkaMirrorMaker = project
  .in(file("."))
  .settings(sharedSettings)
  .settings(
    name                := "kafka-mirror-maker",
    libraryDependencies ++= testDependencies,
    addCommandAlias("format", ";scalafmt;test:scalafmt;scalafmtSbt"),
    addCommandAlias(
      "checkFormat",
      ";scalafmtCheck;test:scalafmtCheck;scalafmtSbtCheck"
    )
  )
