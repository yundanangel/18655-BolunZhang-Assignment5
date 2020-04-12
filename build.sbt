name := "assignment5"
 
version := "1.0" 
      
lazy val `assignment5` = (project in file(".")).enablePlugins(PlayJava)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

resolvers ++= Seq(
  "Typesafe" at "http://repo.typesafe.com/typesafe/releases/",
  "jgap" at "https://mvnrepository.com/artifact/org.jgap/jgap"
)
      
scalaVersion := "2.11.11"

libraryDependencies ++= Seq( javaJdbc , cache , javaWs )
// https://mvnrepository.com/artifact/org.apache.poi/poi
libraryDependencies += "org.apache.poi" % "poi" % "4.1.2"
libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.10"
libraryDependencies += "org.apache.poi" % "poi-ooxml" % "4.1.2"
unmanagedJars in Compile += file("lib/jgap.jar")

      