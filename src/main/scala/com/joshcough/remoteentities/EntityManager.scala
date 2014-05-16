package com.joshcough.remoteentities

import java.lang.reflect.Constructor
import java.util._
import java.util.concurrent.ConcurrentHashMap
import org.bukkit.{Bukkit, Location}
import org.bukkit.entity.{HumanEntity, LivingEntity}
import org.bukkit.event.world.ChunkLoadEvent
import org.bukkit.plugin.Plugin
import de.kumpelblase2.remoteentities.api._
import de.kumpelblase2.remoteentities.api.events.RemoteEntityCreateEvent
import de.kumpelblase2.remoteentities.CreateEntityContext
import de.kumpelblase2.remoteentities.exceptions.NoNameException
import de.kumpelblase2.remoteentities.persistence.{EntityData, IEntitySerializer}
import de.kumpelblase2.remoteentities.utilities.NMSUtil
import scala.collection.JavaConversions._

case class EntityManager(m_plugin: Plugin, var m_removeDespawned: Boolean = false) {

  private val m_entities: Map[Integer, RemoteEntity] = new ConcurrentHashMap[Integer, RemoteEntity]
  private val m_entityChunkLoader: ChunkEntityLoader = new ChunkEntityLoader(this)
  protected var m_serializer: IEntitySerializer = null
  private var m_saveOnDisable: Boolean = false

  this.setup(m_plugin)

  protected def setup(inPlugin: Plugin) {
    Bukkit.getPluginManager.registerEvents(this.m_entityChunkLoader, RemoteEntities.getInstance)
    Bukkit.getScheduler.scheduleSyncRepeatingTask(inPlugin, new Runnable {
      def run {
        val it: Iterator[Map.Entry[Integer, RemoteEntity]] = m_entities.entrySet.iterator
        while (it.hasNext) {
          val entity: RemoteEntity = it.next.getValue
          if (entity.getHandle == null) { if(m_removeDespawned) it.remove }
          else {
            entity.getHandle.C
            if (entity.getHandle.dead && entity.despawn(DespawnReason.DEATH)) it.remove
          }
        }
      }
    }, 1L, 1L)
  }

  /**
   * Gets the plugin that created this EntityManager
   * @return	the plugin
   */
  def getPlugin: Plugin = this.m_plugin

  /**
   * Gets the next free id starting from 0
   * @return	next free id
   */
  protected def getNextFreeID: Integer = this.getNextFreeID(0)

  /**
   * Gets the next free id starting from the id provided.
   * If the give it is free, it will get returned as well.
   * @param inStart	starting id
   * @return			next free id
   */
  protected def getNextFreeID(inStart: Int): Integer = {
    val ids: Set[Integer] = this.m_entities.keySet
    var i = inStart
    while (ids contains i) { i += 1 }
    i
  }

  /**
   * Create an entity at given location with given type.
   * @param inType			type of the entity to create
   * @param inLocation		location where it should get created
   * @return					the created entity
   * @throws NoNameException  when trying to spawn a named entity. Use EntityManager#createNamedEntity(RemoteEntityType, Location, String) instead.
   */
  def createEntity(inType: RemoteEntityType, inLocation: Location): RemoteEntity = this.createEntity(inType, inLocation, true)

  /**
   * Creates an entity at given location with given type.
   * You can also specify if you want to setup the default desires/goals of the entity (default is true).
   * @param inType			type of the entity to create
   * @param inLocation		location where it should get created
   * @param inSetupGoals		if default desires/goals should be applied
   * @return					the created entity
   * @throws NoNameException  when trying to spawn a named entity. Use EntityManager#createNamedEntity(RemoteEntityType, Location, String, boolean) instead.
   */
  def createEntity(inType: RemoteEntityType, inLocation: Location, inSetupGoals: Boolean): RemoteEntity = {
    if (inType.isNamed) throw new NoNameException("Tried to spawn a named entity without name")
    val id: Integer = this.getNextFreeID
    val entity: RemoteEntity = this.createEntity(inType, id)
    if (entity == null) return null
    if (inLocation != null) this.m_entityChunkLoader.queueSpawn(entity, inLocation, inSetupGoals)
    entity
  }

  private[remoteentities] def createEntity(inType: RemoteEntityType, inID: Int): RemoteEntity = {
    try {
      val constructor: Constructor[_ <: RemoteEntity] =
        inType.getRemoteClass.getConstructor(classOf[Int], classOf[EntityManager])
      val entity: RemoteEntity = constructor.newInstance(inID.asInstanceOf[AnyRef], this)
      val event: RemoteEntityCreateEvent = new RemoteEntityCreateEvent(entity)
      Bukkit.getPluginManager.callEvent(event)
      if (event.isCancelled) return null
      this.m_entities.put(inID, entity)
      entity
    }
    catch { case e: Exception =>
      e.printStackTrace
      null
    }
  }

  /**
   * Creates a named entity at given location with given type.
   *
   * @param inType		type of the entity to create
   * @param inLocation	location where it should get created
   * @param inName		name of the entity
   * @return				the created entity
   */
  def createNamedEntity(inType: RemoteEntityType, inLocation: Location, inName: String): RemoteEntity =
    this.createNamedEntity(inType, inLocation, inName, true)

  /**
   * Creates a named entity at given location with given type.
   * @param inType		type of the entity to create
   * @param inLocation	location where it should get created
   * @param inName		name of the entity
   * @param inSetupGoals	if default goals/desires should be applied
   * @return				the created entity
   */
  def createNamedEntity(inType: RemoteEntityType, inLocation: Location, inName: String, inSetupGoals: Boolean): RemoteEntity = {
    if (!inType.isNamed) {
      val entity: RemoteEntity = this.createEntity(inType, inLocation, inSetupGoals)
      if (entity == null) return null
      entity.setName(inName)
      return entity
    }
    val id: Integer = this.getNextFreeID
    val entity: RemoteEntity = this.createNamedEntity(inType, id, inName)
    if (entity == null) return null
    if (inLocation != null) this.m_entityChunkLoader.queueSpawn(entity, inLocation, inSetupGoals)
    entity
  }

  private[remoteentities] def createNamedEntity(inType: RemoteEntityType, inID: Int, inName: String): RemoteEntity = {
    try {
      val constructor: Constructor[_ <: RemoteEntity] =
        inType.getRemoteClass.getConstructor(classOf[Int], classOf[String], classOf[EntityManager])
      val entity: RemoteEntity = constructor.newInstance(inID.asInstanceOf[AnyRef], inName, this)
      val event: RemoteEntityCreateEvent = new RemoteEntityCreateEvent(entity)
      Bukkit.getPluginManager.callEvent(event)
      if (event.isCancelled) return null
      this.m_entities.put(inID, entity)
      entity
    }
    catch { case e: Exception =>
      e.printStackTrace
      null
    }
  }

  /**
   * Creates a context that lets you specify more than using the normal methods
   * @param inType	Type of the entity
   * @return			Context that lets you specify the creation parameters
   */
  def prepareEntity(inType: RemoteEntityType): CreateEntityContext = new CreateEntityContext(this).withType(inType)

  /**
   * Removes an entity completely. If the entity is not despawned already, it'll do so.
   * @param inID	ID of the entity to remove
   */
  def removeEntity(inID: Int) { this.removeEntity(inID, true) }

  /**
   * Removes an entity from the list. When inDespawn is true, it'll also try to despawn it.
   * @param inID			ID of the entity to remove
   * @param inDespawn		Whether the entity should get despawned or not
   */
  def removeEntity(inID: Int, inDespawn: Boolean) {
    if (this.m_entities.containsKey(inID) && inDespawn) this.m_entities.get(inID).despawn(DespawnReason.CUSTOM)
    this.m_entities.remove(inID)
  }

  /**
   * Checks whether the provided entity is a RemoteEntity created by this manager
   * @param inEntity	entity to check
   * @return			true if the entity is a RemoteEntity and from this manager, false if not
   */
  def isRemoteEntity(inEntity: LivingEntity): Boolean = {
    if (inEntity.hasMetadata("remoteentity")) {
      inEntity.getMetadata("remoteentity").exists { v =>
        v.value match {
          case re: RemoteEntity => if (re.getManager eq this) true else false
          case _ => false
        }
      }
    }
    else {
      val entity: RemoteEntity = NMSUtil.getRemoteEntityFromEntity(inEntity)
      entity != null && (entity.getManager eq this)
    }
  }

  /**
   * Gets the existing RemoteEntity from the provided entity.
   * @param inEntity	entity
   * @return			instance of the RemoteEntity
   */
  def getRemoteEntityFromEntity(inEntity: LivingEntity): RemoteEntity = {
    if (!this.isRemoteEntity(inEntity)) return null
    this.getAllEntities.find(re => re.getBukkitEntity eq inEntity).getOrElse(
      NMSUtil.getRemoteEntityFromEntity(inEntity)
    )
  }

  /**
   * Gets the RemoteEntity by its ID
   * @param inID	ID of the entity
   * @return		RemoteEntity with given ID
   */
  def getRemoteEntityByID(inID: Int): RemoteEntity = this.m_entities.get(inID)

  /**
   * Returns all entities with the specified name
   * @param inName	Name to look for
   * @return			Entities with that name
   */
  def getRemoteEntitiesByName(inName: String): Array[RemoteEntity] = {
    val entities: List[RemoteEntity] = new ArrayList[RemoteEntity]
    for (entity <- this.getAllEntities; if entity != null && entity.getName == inName) {
      entities.add(entity)
    }
    entities.toArray(new Array[RemoteEntity](entities.size))
  }

  /**
   * Adds an already existing RemoteEntity to the manager
   * @param inID		ID of the entity
   * @param inEntity	entity to add
   */
  def addRemoteEntity(inID: Int, inEntity: RemoteEntity) { this.m_entities.put(inID, inEntity) }

  /**
   * Creates a RemoteEntity from an existing minecraft entity. The old entity will be replaced with the RemoteEntity
   * @param inEntity	entity to replace
   * @return			instance of the RemoteEntity
   */
  def createRemoteEntityFromExisting(inEntity: LivingEntity): RemoteEntity = this.createRemoteEntityFromExisting(inEntity, true)

  /**
   * Creates a RemoteEntity from an existing minecraft entity. The old entity will only get removed when inDeleteOld is true.
   * @param inEntity		entity to copy from
   * @param inDeleteOld	should delete old one
   * @return				created entity
   */
  def createRemoteEntityFromExisting(inEntity: LivingEntity, inDeleteOld: Boolean): RemoteEntity = {
    val typ: RemoteEntityType = RemoteEntityType.getByEntityClass(NMSUtil.getNMSClassFromEntity(inEntity))
    if (typ == null) return null
    val originalSpot: Location = inEntity.getLocation
    if (inDeleteOld) inEntity.remove
    try inEntity match {
      case entity: HumanEntity => this.createNamedEntity(typ, originalSpot, entity.getName, true)
      case _ => this.createEntity(typ, originalSpot, true)
    }
    catch { case e: Exception =>
      e.printStackTrace
      return null
    }
  }

  /**
   * Despawns all entities from this manager
   */
  def despawnAll { this.despawnAll(DespawnReason.CUSTOM) }

  /**
   * Despawns all entities from this manager with the given {@link DespawnReason}.
   * This also saves the entities if the reason is PLUGIN_DISABLE and {@link #shouldSaveOnDisable()} returns true.
   * @param reason	despawn reason
   */
  def despawnAll(reason: DespawnReason) {
    if (this.m_entities.size == 0) return
    if (reason eq DespawnReason.PLUGIN_DISABLE) this.despawnAll(reason, this.shouldSaveOnDisable)
    this.despawnAll(reason, false)
  }

  /**
   * Despawns all entities from this manager with the given {@link DespawnReason}.
   * If inSave is true, it will save all entities before despawning them.
   * @param reason	The despawn reason
   * @param save	If the entities should be save before despawning
   */
  def despawnAll(reason: DespawnReason, save: Boolean) {
    if (save) this.saveEntities
    for (entity <- this.m_entities.values) { entity.despawn(reason) }
    this.m_entities.clear
  }

  /**
   * Gets all entities created by this manager
   * @return	list of all entities
   */
  def getAllEntities: List[RemoteEntity] = new ArrayList[RemoteEntity](this.m_entities.values)

  /**
   * Returns the amount of entities registered
   * @return	entity count
   */
  def getEntityCount: Int = this.m_entities.size

  /**
   * Returns whether despawned entities will automatically get removed or not
   * @return	true when they get removed, false when not
   */
  def shouldRemoveDespawnedEntities: Boolean = this.m_removeDespawned

  /**
   * Sets if despawned entities should get automatically get removed
   * @param inState	True if they should, false if not
   */
  def setRemovingDespawned(inState: Boolean) { this.m_removeDespawned = inState }

  private[remoteentities] def unregisterEntityLoader {
    ChunkLoadEvent.getHandlerList.unregister(this.m_entityChunkLoader)
  }

  /**
   * Sets the serializer which should be used when saving the entities
   * @param inSerializer	serializer to use
   */
  def setEntitySerializer(inSerializer: IEntitySerializer) { this.m_serializer = inSerializer }

  /**
   * Gets the currently used serializer
   * @return	serializer
   */
  def getSerializer: IEntitySerializer = this.m_serializer

  /**
   * Saves all currently available entities
   * @return  The amount of entities saved
   */
  def saveEntities: Int = {
    if (this.m_serializer == null) return 0
    val data: Array[EntityData] = new Array[EntityData](this.m_entities.size)
    var pos: Int = 0
    for (entity <- this.m_entities.values) {
      data(pos) = this.m_serializer.prepare(entity)
      pos += 1
    }
    this.m_serializer.save(data)
    data.length
  }

  /**
   * Loads all saved entities
   * @return  The amount of entities loaded
   */
  def loadEntities: Int = {
    if (this.m_serializer == null) return 0
    val data: Array[EntityData] = this.m_serializer.loadData
    for (entity <- data) { this.m_serializer.create(entity) }
    data.length
  }

  /**
   * Sets whether the entity manager should automatically save the entities when the plugin gets disabled.
   * @param inSave	New save state
   */
  def setSaveOnDisable(inSave: Boolean) { this.m_saveOnDisable = inSave }

  /**
   * Checks whether entities should be save when the plugin gets disabled.
   * @return	state
   */
  def shouldSaveOnDisable: Boolean = this.m_saveOnDisable

  /**
   * Gets all RemoteEntities with the given type.
   * @param inType	The type of entity to look for
   * @return 		List of entities with that type
   */
  def getEntitiesByType(inType: RemoteEntityType): Iterable[RemoteEntity] =
    this.getEntitiesByType(inType, spawnedOnly=false)

  /**
   * Gets all RemoteEntities with the given type which are also currently spawned.
   * @param typ	The type of entity to look for
   * @param spawnedOnly	Whether to ignore despawned entities or not
   * @return		List of entities with the given type
   */
  def getEntitiesByType(typ: RemoteEntityType, spawnedOnly: Boolean): Iterable[RemoteEntity] =
    this.m_entities.values.filter(e => (e.getType eq typ) && (!spawnedOnly || e.isSpawned))
}
