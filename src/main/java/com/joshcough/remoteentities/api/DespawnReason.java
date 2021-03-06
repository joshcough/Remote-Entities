package com.joshcough.remoteentities.api;

public enum DespawnReason
{
	/**
	 * When the entity dies normally
	 */
	DEATH,
	/**
	 * When the owner plugin gets disabled
	 */
	PLUGIN_DISABLE,
	/**
	 * When a plugin request despawn
	 */
	CUSTOM,
	/**
	 * When the name of the entity changed
	 */
	NAME_CHANGE,
	/**
	 * When, for what ever reason, the chunk the entity is in gets unloaded
	 */
	CHUNK_UNLOAD
}