package com.joshcough.remoteentities.entities;

import com.joshcough.remoteentities.EntityManager;
import org.bukkit.entity.Wither;
import com.joshcough.remoteentities.api.EntitySound;
import com.joshcough.remoteentities.api.RemoteEntityType;

public class RemoteWither extends RemoteAttackingBaseEntity<Wither>
{
	public RemoteWither(int inID, EntityManager inManager)
	{
		super(inID, RemoteEntityType.Wither, inManager);
	}

	@Override
	public String getNativeEntityName()
	{
		return "WitherBoss";
	}

	@Override
	protected void setupSounds()
	{
		this.setSound(EntitySound.RANDOM, "mob.wither.idle");
		this.setSound(EntitySound.HURT, "mob.wither.hurt");
		this.setSound(EntitySound.DEATH, "mob.wither.death");
	}
}