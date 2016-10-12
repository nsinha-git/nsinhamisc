name := "csvimport"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= {
  Seq(
    "org.specs2"          %%  "specs2-core"   % "2.3.11" % "test",
    "org.scaldi" %% "scaldi-akka" % "0.5.3",
    "org.scalatest" %% "scalatest" % "2.2.4" % "test",
    "org.scalikejdbc" %% "scalikejdbc" % "2.2.7",
    "org.scalikejdbc" %% "scalikejdbc-config" % "2.2.7",
    "mysql" % "mysql-connector-java" % "5.1.36",
    "org.skinny-framework" %% "skinny-orm"      % "2.0.5",
    "org.json4s" %% "json4s-native" % "3.2.11",
    "org.json4s" %% "json4s-jackson" % "3.2.11",
    "net.liftweb" % "lift-json_2.11" % "3.0-M8",
    "net.sf.opencsv" % "opencsv" % "2.3",
    "joda-time" % "joda-time" % "2.9.4" ,
    "org.slf4j" % "slf4j-log4j12" % "1.7.13"
  )
}

resolvers += "spray" at "http://repo.spray.io/"

libraryDependencies ++= {
  val akkaV = "2.3.11"
  val sprayV = "1.3.3"
  Seq(
    "io.spray"            %%  "spray-can"     % sprayV,
    "io.spray"            %%  "spray-client"  % sprayV,
    "io.spray"            %%  "spray-servlet" % sprayV,
    "io.spray"            %%  "spray-routing" % sprayV,
    "io.spray"            %%  "spray-json"    % "1.3.2",
    "io.spray"            %%  "spray-testkit" % sprayV  % "test",
    "com.typesafe.akka"   %%  "akka-actor"    % akkaV,
    "com.typesafe.akka"   %%  "akka-slf4j"    % akkaV,
    "com.typesafe.akka"   %%  "akka-testkit"  % akkaV   % "test",
    "org.specs2"          %%  "specs2"        % "2.4.1" % "test"
  )
}


