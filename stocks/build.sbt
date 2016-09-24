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
    "net.sf.opencsv" % "opencsv" % "2.3",
    "org.slf4j" % "slf4j-log4j12" % "1.7.13"
  )
}