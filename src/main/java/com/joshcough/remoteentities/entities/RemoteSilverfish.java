package com.joshcough.remoteentities.entities;

import com.joshcough.remoteentities.EntityManager;
import org.bukkit.entity.Silverfish;
import com.joshcough.remoteentities.api.EntitySound;
import com.joshcough.remoteentities.api.RemoteEntityType;

public class RemoteSilverfish extends RemoteAttackingBaseEntity<Silverfish>
{
	public RemoteSilverfish(int inID, EntityManager inManager)
	{
		this(inID, null, inManager);
	}

	public RemoteSilverfish(int inID, RemoteSilverfishEntity inEntity, EntityManager inManager)
	{
		super(inID, RemoteEntityType.Silverfish(), inManager);
		this.m_entity = inEntity;
	}

	@Override
	public String getNativeEntityName()
	{
		return "Silverfish";
	}

	@Override
	protected void setupSounds()
	{
		this.setSound(EntitySound.RANDOM, "mob.silverfish.say");
		this.setSound(EntitySound.HURT, "mob.silverfish.hit");
		this.setSound(EntitySound.DEATH, "mob.silverfish.kill");
		this.setSound(EntitySound.STEP, "mob.silverfish.step");
	}
}