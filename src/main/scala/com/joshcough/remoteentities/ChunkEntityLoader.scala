package com.joshcough.remoteentities

import org.bukkit._
import org.bukkit.entity.LivingEntity
import org.bukkit.event._
import org.bukkit.event.world.{ ChunkLoadEvent, ChunkUnloadEvent }
import de.kumpelblase2.remoteentities.api.{ RemoteEntity, DespawnReason }
import de.kumpelblase2.remoteentities.utilities.WorldUtilities
import scala.collection.JavaConversions._
import scala.collection.mutable

class ChunkEntityLoader(m_manager: EntityManager) extends Listener {

  private final val m_toSpawn = new mutable.HashSet[EntityLoadData]

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  def onChunkLoad(event: ChunkLoadEvent) {
    val c: Chunk = event.getChunk
    for (e <- this.m_manager.getAllEntities; if e.isSpawned) {
      if ((e.getBukkitEntity.getLocation.getChunk eq c) && e.getHandle != null)
        WorldUtilities.updateEntityTracking(e, c)
    }
    Bukkit.getScheduler.runTask(RemoteEntities.getInstance, new Runnable {
      def run {
        val s = m_toSpawn.filter(_.loc.getChunk eq c)
        s.foreach(spawn)
        s.foreach(m_toSpawn.remove)
      }
    })
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  def onChunkUnload(event: ChunkUnloadEvent) {
    for (entity <- event.getChunk.getEntities; if entity.isInstanceOf[LivingEntity]) {
      val rentity = RemoteEntities.getRemoteEntityFromEntity(entity.asInstanceOf[LivingEntity])
      if (rentity != null && rentity.isSpawned) {
        m_toSpawn.add(new EntityLoadData(rentity, entity.getLocation))
        rentity.despawn(DespawnReason.CHUNK_UNLOAD)
      }
    }
  }

  /**
   * Checks if an entity can be directly be spawned at given location.
   * @param loc	Location to check for
   * @return				true if it can be spawned, false if not
   */
  def canSpawnAt(loc: Location): Boolean = loc.getChunk.isLoaded

  /**
   * Queues an entity to spawn whenever the chunk is loaded.
   * @param e		Entity to spawn
   * @param loc	Location to spawn at
   * @return				true if it gets queued, false if it could be spawned directly
   */
  def queueSpawn(e: RemoteEntity, loc: Location): Boolean = this.queueSpawn(e, loc, false)

  /**
   * Queues an entity to spawn whenever the chunk is loaded.
   * @param e		Entity to spawn
   * @param inLocation	Location to spawn at
   * @param inSetupGoals	Whether standard goals should be applied or not
   * @return				true if it gets queued, false if it could be spawned directly
   */
  def queueSpawn(e: RemoteEntity, inLocation: Location, inSetupGoals: Boolean): Boolean = {
    val spawnData = new EntityLoadData(e, inLocation, inSetupGoals)
    if (this.canSpawnAt(inLocation)) { this.spawn(spawnData); false }
    else { this.m_toSpawn.add(spawnData); true }
  }

  protected def spawn(eld: EntityLoadData) { eld.entity.spawn(eld.loc) }

  private case class EntityLoadData(entity: RemoteEntity, loc: Location, setupGoals: Boolean = false)
}