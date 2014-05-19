package com.joshcough.remoteentities.entities;

import com.joshcough.remoteentities.EntityManager;
import com.joshcough.remoteentities.api.EntitySound;
import com.joshcough.remoteentities.api.RemoteEntityType;

public class RemoteCow extends RemoteBaseEntity
{
	public RemoteCow(int inID, EntityManager inManager)
	{
		this(inID, null, inManager);
	}

	public RemoteCow(int inID, RemoteCowEntity inEntity, EntityManager inManager)
	{
		super(inID, RemoteEntityType.Cow(), inManager);
		this.m_entity = inEntity;
	}

	@Override
	public String getNativeEntityName()
	{
		return "Cow";
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