package com.joshcough.remoteentities.api

import java.util.Map
import net.minecraft.server.v1_7_R1.EntityLiving
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import com.joshcough.remoteentities.EntityManager
import com.joshcough.remoteentities.api.features.FeatureSet

trait RemoteEntity extends Nameable {
  /**
   * Gets the ID of the entity
   * @return ID
   */
  def getID: Int

  /**
   * Gets the type of the entity
   * @return	type
   */
  def getType: RemoteEntityType

  /**
   * Gets the bukkit entity of this RemoteEntity
   * @return	bukkit entity
   */
  def getBukkitEntity: LivingEntity

  /**
   * Gets the native minecraft entity
   * @return	native entity
   */
  def getHandle: EntityLiving

  /**
   * Gets the features of this entity
   * @return features
   */
  def getFeatures: FeatureSet

  /**
   * Tries to move the entity to a location
   * @param inLocation	location to move to
   * @return				true if it was possible to move the entity, false if not
   */
  def move(inLocation: Location): Boolean

  /**
   * Tries to move the entity to a location with given speed
   * @param inLocation	location to move to
   * @param inSpeed		speed of the entity
   * @return				true if it was possible, false if not
   */
  def move(inLocation: Location, inSpeed: Double): Boolean

  /**
   * Tries to move the entity towards another entity
   * @param inEntity	entity to move to
   * @return			true if it was possible to move the entity, false if not
   */
  def move(inEntity: LivingEntity): Boolean

  /**
   * Tries to move the entity towards another entity with given speed
   * @param inEntity	entity to move to
   * @param inSpeed	speed of the entity
   * @return			true if it was possible, false if not
   */
  def move(inEntity: LivingEntity, inSpeed: Double): Boolean

  /**
   * Sets that yaw of the entity
   * When the entity is stationary, the yaw value will be fixed to the new value.
   * @param inYaw	new yaw
   */
  def setYaw(inYaw: Float)

  /**
   * When inRotate is true, it will smoothly rotate to the new yaw rotation.
   * Otherwise it will just set the new value.
   * When the entity is stationary, the yaw value will be fixed to the new value.
   * @param inYaw		new yaw
   * @param inRotate	If the entity should rotate or snap to the new value
   */
  def setYaw(inYaw: Float, inRotate: Boolean)

  /**
   * Sets the head pitch of the entity
   * When the entity is stationary, the pitch value will be fixed to the new value.
   * @param inPitch	new pitch
   */
  def setPitch(inPitch: Float)

  /**
   * Sets that yaw of the head of the entity
   * When the entity is stationary, the yaw value will be fixed to the new value.
   * @param inHeadYaw	new head yaw
   */
  def setHeadYaw(inHeadYaw: Float)

  /**
   * Lets the entity look at the given location
   * @param inLocation	location to look at
   */
  def lookAt(inLocation: Location)

  /**
   * Lets the entity look at another entity
   * @param inEntity	entity to look at
   */
  def lookAt(inEntity: Entity)

  /**
   * Stops the current movement of the entity
   */
  def stopMoving

  /**
   * Teleports the entity to a location
   * @param inLocation location to teleport to
   */
  def teleport(inLocation: Location)

  /**
   * Spawns the entity at a location
   * @param inLocation location to spawn at
   */
  def spawn(inLocation: Location)

  /**
   * Spawns the entity at a location even when the chunk is not loaded
   * @param inLocation	location to spawn at
   * @param inForce		if the spawn should be forced on a non-loaded chunk or not
   */
  def spawn(inLocation: Location, inForce: Boolean)

  /**
   * Despawns the entity with a specific reason
   *
   * @param inReason	reason for despawning
   */
  def despawn(inReason: DespawnReason): Boolean

  /**
   * Checks if the entity is spawned
   * @return true of spawned, false if not
   */
  def isSpawned: Boolean

  /**
   * Sets the the stationary state of the entity.
   * While being stationary an entity is unable to move.
   * This will also reset the fixed yaw and pitch rotation
   * @param inState	stationary state
   */
  def setStationary(inState: Boolean)

  /**
   * Sets the stationary state of the entity.
   * While being stationary an entity is unable to move.
   * @param inState			stationary state
   * @param inKeepHeadFixed	Determines whether the entity should keep its fixed yaw and pitch when changing its state
   */
  def setStationary(inState: Boolean, inKeepHeadFixed: Boolean)

  /**
   * Gets if the entity is stationary
   * @return true if stationary, false if not
   */
  def isStationary: Boolean

  /**
   * Gets the movement speed of the entity
   * @return speed
   */
  def getSpeed: Double

  /**
   * Sets the speed of the entity
   * @param inSpeed speed
   */
  def setSpeed(inSpeed: Double)

  /**
   * Adds a speed modifier for your entity.
   * @param inAmount      The amount of how much you want to alter the speed
   * @param inAdditive    If it should be additive to the current speed or should be multiplicative
   */
  def addSpeedModifier(inAmount: Double, inAdditive: Boolean)

  /**
   * Removes the speed modifier currently on the entity.
   */
  def removeSpeedModifier

  /**
   * Gets if the entity is pushable or not
   * @return true if pushable, false if not
   */
  def isPushable: Boolean

  /**
   * Sets if the entity is pushable or not
   * @param inState pushable status
   */
  def setPushable(inState: Boolean)

  /**
   * Sets the max range for the pathfinder.
   * @param inRange new range for the pathfinder
   */
  def setPathfindingRange(inRange: Double)

  /**
   * Gets the max range for the pathfinder.
   * @return range
   */
  def getPathfindingRange: Double

  /**
   * Gets the manager that created this entity
   * @return EntityManager
   */
  def getManager: EntityManager

  /**
   * The native name is the name that is used in the EntityTypes enum. This is used internally for automatically registering the entity to the enum.
   * @return native name
   */
  def getNativeEntityName: String

  /**
   * If a ISingleEntitySerializer is present, this entity will be saved alone.
   * @return	true if the entity could get saved, false if not
   */
  def save: Boolean

  /**
   * Gets the sound that should play for the specific sound type.
   * When more than one sound is registered for that type, a random one will be selected.
   * @see RemoteEntity#getSound(EntitySound, String)
   * @param inType    The type of the sound
   * @return          The name of the sound
   */
  def getSound(inType: EntitySound): String

  /**
   * Gets the sound with the specific key from the sounds that are registered on the type of sound.
   * When only one sound is registered, it will return null.
   * @param inType    Type of sound
   * @param inKey     Identifier for the sound
   * @return          The sound name or null if it wasn't found
   */
  def getSound(inType: EntitySound, inKey: String): String

  /**
   * Gets all sounds registered for this type of sound.
   * If only a single sound with no key was registered, it will still return a map and the only entry will be the sound with key 'default'.
   * @param inType    The type of sound
   * @return          Sounds for this type
   */
  def getSounds(inType: EntitySound): Map[String, String]

  /**
   * Checks if the entity has at least one sound registered for that type.
   * @see RemoteEntity#hasSound(EntitySound, String)
   * @param inType    The type of the sound
   * @return          true if it has it, false if not
   */
  def hasSound(inType: EntitySound): Boolean

  /**
   * Checks if the entity has a sound with that key registered for the specific sound type.
   * @param inType    Type of the sound
   * @param inKey     Identifier for the sound
   * @return          True if it has, false if not
   */
  def hasSound(inType: EntitySound, inKey: String): Boolean

  /**
   * Sets the sound name for the specific type of sound.
   * When more than one should has been registered on this type, it will override all previously added sounds.
   * @param inType    Sound type to change
   * @param inSound   The new name for the sound
   */
  def setSound(inType: EntitySound, inSound: String)

  /**
   * Registers a sound with the key for the specific sound type.
   * If a single sound without key was registered earlier it will be overridden.
   * @param inType    Type of the sound
   * @param inKey     Identifier for the sound
   * @param inSound   The sound
   */
  def setSound(inType: EntitySound, inKey: String, inSound: String)

  /**
   * Registers the map of sounds for the specific sound type.
   * If a single sound without key was registered earlier it will be overridden.
   * @param inType    Type of the sound
   * @param inSounds  The sounds
   */
  def setSounds(inType: EntitySound, inSounds: Map[String, String])
}