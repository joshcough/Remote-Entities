package com.joshcough.remoteentities.api

import net.minecraft.server.v1_7_R1.EntityLiving
import com.joshcough.remoteentities.entities._

case class RemoteEntityType(remoteClass: Class[_ <: RemoteEntity], entityClass: Class[_ <: EntityLiving]){
  val name = remoteClass.getSimpleName.substring(6) // drop "Remote" (eg. RemoteHuman -> Human)
  val isNamed = name == "Human" // only Humans are named, apparently.
  override def toString: String = this.name
}

object RemoteEntityType {
  val Human       = create(classOf[RemotePlayer],      classOf[RemotePlayerEntity])
  val Zombie      = create(classOf[RemoteZombie],      classOf[RemoteZombieEntity])
  val Spider      = create(classOf[RemoteSpider],      classOf[RemoteSpiderEntity])
  val Creeper     = create(classOf[RemoteCreeper],     classOf[RemoteCreeperEntity])
  val Skeleton    = create(classOf[RemoteSkeleton],    classOf[RemoteSkeletonEntity])
  val Blaze       = create(classOf[RemoteBlaze],       classOf[RemoteBlazeEntity])
  val CaveSpider  = create(classOf[RemoteCaveSpider],  classOf[RemoteCaveSpiderEntity])
  val Chicken     = create(classOf[RemoteChicken],     classOf[RemoteChickenEntity])
  val Cow         = create(classOf[RemoteCow],         classOf[RemoteCowEntity])
  val EnderDragon = create(classOf[RemoteEnderDragon], classOf[RemoteEnderDragonEntity])
  val Enderman    = create(classOf[RemoteEnderman],    classOf[RemoteEndermanEntity])
  val Ghast       = create(classOf[RemoteGhast],       classOf[RemoteGhastEntity])
  val IronGolem   = create(classOf[RemoteIronGolem],   classOf[RemoteIronGolemEntity])
  val LavaSlime   = create(classOf[RemoteLavaSlime],   classOf[RemoteLavaSlimeEntity])
  val Mushroom    = create(classOf[RemoteMushroom],    classOf[RemoteMushroomEntity])
  val Ocelot      = create(classOf[RemoteOcelote],     classOf[RemoteOceloteEntity])
  val Pig         = create(classOf[RemotePig],         classOf[RemotePigEntity])
  val Pigmen      = create(classOf[RemotePigmen],      classOf[RemotePigmenEntity])
  val Sheep       = create(classOf[RemoteSheep],       classOf[RemoteSheepEntity])
  val Silverfish  = create(classOf[RemoteSilverfish],  classOf[RemoteSilverfishEntity])
  val Slime       = create(classOf[RemoteSlime],       classOf[RemoteSlimeEntity])
  val Snowman     = create(classOf[RemoteSnowman],     classOf[RemoteSnowmanEntity])
  val Squid       = create(classOf[RemoteSquid],       classOf[RemoteSquidEntity])
  val Villager    = create(classOf[RemoteVillager],    classOf[RemoteVillagerEntity])
  val Wolf        = create(classOf[RemoteWolf],        classOf[RemoteWolfEntity])
  val Witch       = create(classOf[RemoteWitch],       classOf[RemoteWitchEntity])
  val Wither      = create(classOf[RemoteWither],      classOf[RemoteWitherEntity])
  val Bat         = create(classOf[RemoteBat],         classOf[RemoteBatEntity])
  val Horse       = create(classOf[RemoteHorse],       classOf[RemoteHorseEntity])

  private var byName: Map[String, RemoteEntityType] = Map()
  private var byRemoteClass: Map[Class[_ <: RemoteEntity], RemoteEntityType] = Map()

  private def create(remoteClass: Class[_ <: RemoteEntity], entityClass: Class[_ <: EntityLiving]): RemoteEntityType = {
    val t = RemoteEntityType(remoteClass, entityClass)
    byName = byName + (t.name -> t)
    byRemoteClass = byRemoteClass + (remoteClass -> t)
    t
  }

  def findByName(name: String) = byName.get(name)

  def findByEntityClass(c: Class[_ <: EntityLiving]): Option[RemoteEntityType] =
    byName.values.find(t => t.entityClass == c || t.entityClass.getSuperclass == c || t.entityClass.isAssignableFrom(c))

  def findByRemoteClass(c: Class[_ <: RemoteEntity]): Option[RemoteEntityType] = byRemoteClass.get(c)
}
