addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.11.2")

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.6.0")

seq(bintrayResolverSettings:_*)

resolvers += Resolver.url(
  "bintray-sbt-plugin-releases",
  url("http://dl.bintray.com/content/sbt/sbt-plugin-releases"))(
    Resolver.ivyStylePatterns)

resolvers += Resolver.url(
  "Josh Cough sbt plugins",
  url("http://dl.bintray.com/content/joshcough/sbt-plugins"))(
    Resolver.ivyStylePatterns)

resolvers += ("Bukkit" at "http://repo.bukkit.org/content/repositories/releases")

addSbtPlugin("me.lessis" % "bintray-sbt" % "0.1.1")

addSbtPlugin("com.joshcough" % "minecraft-sbt-plugin" % "0.3.3")
