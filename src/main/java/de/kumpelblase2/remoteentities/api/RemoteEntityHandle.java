package de.kumpelblase2.remoteentities.api;

import org.bukkit.inventory.InventoryHolder;

public interface RemoteEntityHandle extends InventoryHolder
{
	/**
	 * Gets the RemoteEntity of this entity
	 *
	 * @return	RemoteEntity
	 */
	public RemoteEntity getRemoteEntity();

}