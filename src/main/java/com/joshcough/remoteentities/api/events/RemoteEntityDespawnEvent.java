package com.joshcough.remoteentities.api.events;

import org.bukkit.event.HandlerList;
import com.joshcough.remoteentities.api.DespawnReason;
import com.joshcough.remoteentities.api.RemoteEntity;

/**
 * Event that gets fired when an entity gets despawned
 */
public class RemoteEntityDespawnEvent extends RemoteEvent
{
	private static final HandlerList handlers = new HandlerList();
	private final DespawnReason m_reason;

	public RemoteEntityDespawnEvent(RemoteEntity inEntity)
	{
		this(inEntity, DespawnReason.CUSTOM);
	}

	public RemoteEntityDespawnEvent(RemoteEntity inEntity, DespawnReason inReason)
	{
		super(inEntity);
		this.m_reason = inReason;
	}

	@Override
	public HandlerList getHandlers()
	{
		return handlers;
	}

	public static HandlerList getHandlerList()
	{
		return handlers;
	}

	/**
	 * Gets the reason why the entity gets despawned
	 *
	 * @return reason
	 */
	public DespawnReason getReason()
	{
		return this.m_reason;
	}
}