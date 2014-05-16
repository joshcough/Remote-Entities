package de.kumpelblase2.remoteentities.api.features;

import com.joshcough.remoteentities.api.features.Feature;
import org.bukkit.inventory.Inventory;

public interface InventoryFeature extends Feature
{
	/**
	 * Gets the inventory
	 *
	 * @return inventory
	 */
	public Inventory getInventory();
}