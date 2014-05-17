package com.joshcough.remoteentities.api

import org.bukkit.inventory.InventoryHolder

trait RemoteEntityHandle extends InventoryHolder {
  /**
   * Gets the RemoteEntity of this entity
   * @return	RemoteEntity
   */
  def getRemoteEntity: RemoteEntity
}