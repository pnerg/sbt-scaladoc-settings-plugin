resolvers +=
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.1.0")

addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.0.0")

addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "0.5.1")

addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.4")

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.0.0")

addSbtPlugin("org.dmonix.sbt" % "sbt-publish-settings-plugin" % "0.5")

// This project is its own plugin :)
// The library dependencies is a bit of ugly hack to be able to start SBT as the plugin requires certain dependencies
// Adding the source code to unmanagedSourceDirectories requires that the dependencies are declared
unmanagedSourceDirectories in Compile += baseDirectory.value.getParentFile / "src" / "main" / "scala"
libraryDependencies ++= Seq(
  "net.sourceforge.plantuml" % "plantuml" % "8056",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test"
)
