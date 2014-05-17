package com.joshcough.remoteentities.api.features

import com.joshcough.remoteentities.api.RemoteEntity
import com.joshcough.remoteentities.persistence.SerializableData

trait Feature extends SerializableData {
  /**
   * Gets the name of the feature
   *
   * @return name
   */
  def getName: String

  /**
   * Gets the entity that owns this feature
   *
   * @return entity
   */
  def getEntity: RemoteEntity

  /**
   * Method that gets executed when the feature gets added to the entity
   */
  def onAdd

  /**
   * Method that gets executed when the feature gets added to the given entity
   *
   * @param inEntity  Entity the feature gets added to
   */
  def onAdd(inEntity: RemoteEntity)

  /**
   * Method that gets executed when the feature gets removed from the entity
   */
  def onRemove
}