import AssemblyKeys._
import com.joshcough.minecraft.Plugin._

name := "remote-entities"

organization := "com.joshcough"

scalaVersion := "2.11.0"

version := "1.7.2-R0.2_0.3.4"

licenses <++= version(v => Seq("MIT" -> url("https://github.com/joshcough/Remote-Entities" + "/blob/%s/LICENSE".format(v))))

resolvers += ("Bukkit" at "http://repo.bukkit.org/content/repositories/releases")

libraryDependencies ++= Seq(
  "org.bukkit"         % "craftbukkit"             % "1.7.2-R0.2",
  "com.joshcough"     %% "scala-minecraft-plugin-api" % "0.3.3",
  "org.apache.commons" % "commons-lang3"           % "3.1",
  "javassist"          % "javassist"               % "3.12.1.GA",
  "junit"              % "junit"                   % "4.11"        % "test",
  "org.mockito"        % "mockito-all"             % "1.9.5"       % "test",
  "org.powermock"      % "powermock-module-junit4" % "1.5.4"       % "test",
  "org.powermock"      % "powermock-api-mockito"   % "1.5.4"       % "test",
  "com.novocode"       % "junit-interface"         % "0.9"         % "test"
)

assemblySettings

excludedJars in assembly <<= (fullClasspath in assembly) map { cp =>
  cp filter { x =>
    x.data.getName.contains("craftbukkit") ||
    x.data.getName.contains("scala-library") ||
    x.data.getName.contains("scala-minecraft")
  }
}

artifact in (Compile, assembly) ~= { art =>
  art.copy(`classifier` = Some("assembly"))
}

addArtifact(artifact in (Compile, assembly), assembly)

seq(bintraySettings:_*)

publishMavenStyle := true

seq(pluginYmlSettings("com.joshcough.remoteentities.RemoteEntities", "JoshCough"):_*)

seq(copyPluginToBukkitSettings(Some("assembly")):_*)
