package com.joshcough.remoteentities.entities;

import com.joshcough.remoteentities.EntityManager;
import com.joshcough.remoteentities.api.EntitySound;
import com.joshcough.remoteentities.api.RemoteEntityType;

public class RemoteSheep extends RemoteBaseEntity
{
	public RemoteSheep(int inID, EntityManager inManager)
	{
		this(inID, null, inManager);
	}

	public RemoteSheep(int inID, RemoteSheepEntity inEntity, EntityManager inManager)
	{
		super(inID, RemoteEntityType.Sheep(), inManager);
		this.m_entity = inEntity;
	}

	@Override
	public String getNativeEntityName()
	{
		return "Sheep";
	}

	@Override
	protected void setupSounds()
	{
		this.setSound(EntitySound.RANDOM, "mob.sheep.say");
		this.setSound(EntitySound.HURT, "mob.sheep.say");
		this.setSound(EntitySound.DEATH, "mob.sheep.say");
		this.setSound(EntitySound.STEP, "mob.sheep.step");
	}
}