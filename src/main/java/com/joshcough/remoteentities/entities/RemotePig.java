package com.joshcough.remoteentities.entities;

import com.joshcough.remoteentities.EntityManager;
import com.joshcough.remoteentities.api.EntitySound;
import com.joshcough.remoteentities.api.RemoteEntityType;

public class RemotePig extends RemoteBaseEntity
{
	public RemotePig(int inID, EntityManager inManager)
	{
		this(inID, null, inManager);
	}

	public RemotePig(int inID, RemotePigEntity inEntity, EntityManager inManager)
	{
		super(inID, RemoteEntityType.Pig, inManager);
		this.m_entity = inEntity;
	}

	@Override
	public String getNativeEntityName()
	{
		return "Pig";
	}

	@Override
	protected void setupSounds()
	{
		this.setSound(EntitySound.RANDOM, "mob.pig.say");
		this.setSound(EntitySound.HURT, "mob.pig.say");
		this.setSound(EntitySound.DEATH, "mob.pig.death");
		this.setSound(EntitySound.STEP, "mob.pig.step");
	}
}