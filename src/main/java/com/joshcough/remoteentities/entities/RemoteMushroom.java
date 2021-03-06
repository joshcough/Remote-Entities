package com.joshcough.remoteentities.entities;

import com.joshcough.remoteentities.EntityManager;
import com.joshcough.remoteentities.api.EntitySound;
import com.joshcough.remoteentities.api.RemoteEntityType;

public class RemoteMushroom extends RemoteBaseEntity
{
	public RemoteMushroom(int inID, EntityManager inManager)
	{
		this(inID, null, inManager);
	}

	public RemoteMushroom(int inID, RemoteMushroomEntity inEntity, EntityManager inManager)
	{
		super(inID, RemoteEntityType.Mushroom(), inManager);
		this.m_entity = inEntity;
	}

	@Override
	public String getNativeEntityName()
	{
		return "MushroomCow";
	}

	@Override
	protected void setupSounds()
	{
		this.setSound(EntitySound.RANDOM, "mob.cow.say");
		this.setSound(EntitySound.HURT, "mob.cow.hurt");
		this.setSound(EntitySound.DEATH, "mob.cow.hurt");
		this.setSound(EntitySound.STEP, "mob.cow.step");
	}
}