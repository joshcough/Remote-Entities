package com.joshcough.remoteentities.entities;

import com.joshcough.remoteentities.EntityManager;
import org.bukkit.entity.Horse;
import com.joshcough.remoteentities.api.EntitySound;
import com.joshcough.remoteentities.api.RemoteEntityType;

public class RemoteHorse extends RemoteBaseEntity<Horse>
{
	public RemoteHorse(int inID, EntityManager inManager)
	{
		super(inID, RemoteEntityType.Horse(), inManager);
	}

	public RemoteHorse(int inID, RemoteHorseEntity inEntity, EntityManager inManager)
	{
		this(inID, inManager);
		this.m_entity = inEntity;
	}

	@Override
	public String getNativeEntityName()
	{
		return "EntityHorse";
	}

	@Override
	protected void setupSounds()
	{
		this.setSound(EntitySound.LAND, "mob.horse.land");
	}
}