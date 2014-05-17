package com.joshcough.remoteentities

import java.util._
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.plugin.Plugin
import com.joshcough.remoteentities.exceptions.PluginNotEnabledException
import com.joshcough.remoteentities.api.DespawnReason
import com.joshcough.remoteentities.api.RemoteEntity
import com.joshcough.remoteentities.api.RemoteEntityType
import scala.collection.JavaConversions._
import com.joshcough.remoteentities.utilities.ReflectionUtil
import com.joshcough.minecraft.ScalaPlugin

object RemoteEntities {

  lazy val MINECRAFT_REVISION = ReflectionUtil.getMinecraftRevision

  private var s_instance: RemoteEntities = null

  /**
   * Creates a manager for your plugin
   * @param inPlugin	plugin using that manager
   * @return			instance of a manager
   */
  def createManager(inPlugin: Plugin): EntityManager = createManager(inPlugin, false)

  /**
   * Creates a manager for your plugin
   * You can also specify whether despawned entities should be removed or not
   * @param inPlugin				plugin using that manager
   * @param removeDespawned		automatically removed despawned entities
   * @return						instance of a manager
   */
  def createManager(inPlugin: Plugin, removeDespawned: Boolean): EntityManager = {
    if (getInstance == null) throw new PluginNotEnabledException
    val manager: EntityManager = new EntityManager(inPlugin, removeDespawned)
    registerCustomManager(manager, inPlugin.getName)
    manager
  }

  /**
   * Adds custom created manager to internal map
   * @param inManager custom manager
   * @param inName	name of the plugin using it
   */
  def registerCustomManager(inManager: EntityManager, inName: String) {
    if (getInstance == null) return
    getInstance.m_managers.put(inName, inManager)
  }

  /**
   * Gets the manager of a specific plugin
   * @param inName	name of the plugin
   * @return			EntityManager of that plugin
   */
  def getManagerOfPlugin(inName: String): EntityManager = {
    if (getInstance == null) return null
    getInstance.m_managers.get(inName)
  }

  /**
   * Checks if a specific plugin has registered a manager
   * @param inName	name of the plugin
   * @return			true if the given plugin has a manager, false if not
   */
  def hasManagerForPlugin(inName: String): Boolean =
    getInstance != null && getInstance.m_managers.containsKey(inName)

  /**
   * Gets the instance of the RemoteEntities plugin
   * @return RemoteEntities
   */
  def getInstance: RemoteEntities = s_instance

  /**
   * Checks if the given entity is a RemoteEntity created by any manager.
   * @param inEntity	entity to check
   * @return			true if it is a RemoteEntity, false if not
   */
  def isRemoteEntity(inEntity: LivingEntity): Boolean = {
    if (getInstance == null) false
    else getInstance.m_managers.values.exists(_.isRemoteEntity(inEntity))
  }

  /**
   * Gets the RemoteEntity from a given entity which can be created by any manager.
   * @param inEntity	entity
   * @return			RemoteEntity
   */
  def getRemoteEntityFromEntity(inEntity: LivingEntity): RemoteEntity = {
    if (getInstance == null) return null
    import scala.collection.JavaConversions._
    for (manager <- getInstance.m_managers.values) {
      val entity: RemoteEntity = manager.getRemoteEntityFromEntity(inEntity)
      if (entity != null) return entity
    }
    null
  }
}

class RemoteEntities extends ScalaPlugin {

  import RemoteEntities._

  override def onEnable {
    s_instance = this
    RemoteEntityType.update
    this.getServer.getPluginManager.registerEvents(new DisableListener, this)
  }

  override def onDisable {
    import scala.collection.JavaConversions._
    for (manager <- m_managers.values) {
      manager.despawnAll(DespawnReason.PLUGIN_DISABLE)
      manager.unregisterEntityLoader
    }
    s_instance = null
  }

  private final val m_managers: Map[String, EntityManager] = new HashMap[String, EntityManager]

  private[remoteentities] class DisableListener extends Listener {
    @EventHandler def onPluginDisable(event: PluginDisableEvent) {
      val manager: EntityManager = RemoteEntities.getManagerOfPlugin(event.getPlugin.getName)
      if (manager != null) {
        manager.despawnAll(DespawnReason.PLUGIN_DISABLE)
        manager.unregisterEntityLoader
      }
    }
  }
}