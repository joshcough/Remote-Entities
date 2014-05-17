package com.joshcough.remoteentities.persistence.serializers

import com.joshcough.remoteentities.EntityManager
import com.joshcough.remoteentities.RemoteEntities
import org.bukkit.plugin.Plugin
import com.joshcough.remoteentities._
import com.joshcough.remoteentities.api.RemoteEntity
import com.joshcough.remoteentities.persistence._

/**
 * This is a base seralizer which creates the data from an entity and creates
 * an entity from the data, but it does not save it or load it to a file.
 */
abstract class PreparationSerializer(val m_plugin: Plugin) extends IEntitySerializer {

  def prepare(e: RemoteEntity) = new EntityData(e)

  def create(inData: EntityData): RemoteEntity = {
    val manager: EntityManager = RemoteEntities.getManagerOfPlugin(this.m_plugin.getName)
    val context: CreateEntityContext = manager.prepareEntity(inData.`type`)
    context.withName(inData.name).asPushable(inData.pushable).asStationary(inData.stationary).withID(inData.id)
    context.withSpeed(inData.speed).withPathfindingRange(inData.pathfindingRange)
    if (inData.location != null) context.atLocation(inData.location.toBukkitLocation)
    val entity: RemoteEntity = context.create
    for (data <- inData.features) entity.getFeatures.addFeature(data.create(entity))
    entity
  }
}