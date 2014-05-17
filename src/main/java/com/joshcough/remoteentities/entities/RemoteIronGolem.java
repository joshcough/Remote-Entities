package com.joshcough.remoteentities.entities;

import com.joshcough.remoteentities.EntityManager;
import org.bukkit.entity.IronGolem;
import com.joshcough.remoteentities.api.EntitySound;
import com.joshcough.remoteentities.api.RemoteEntityType;

public class RemoteIronGolem extends RemoteAttackingBaseEntity<IronGolem>
{
	public RemoteIronGolem(int inID, EntityManager inManager)
	{
		this(inID, null, inManager);
	}

	public RemoteIronGolem(int inID, RemoteIronGolemEntity inEntity, EntityManager inManager)
	{
		super(inID, RemoteEntityType.IronGolem, inManager);
		this.m_entity = inEntity;
	}

	@Override
	public String getNativeEntityName()
	{
		return "VillagerGolem";
	}

	@Override
	protected void setupSounds()
	{
		this.setSound(EntitySound.RANDOM, "none");
		this.setSound(EntitySound.HURT, "mob.irongolem.hit");
		this.setSound(EntitySound.DEATH, "mob.irongolem.death");
		this.setSound(EntitySound.STEP, "mob.irongolem.walk");
	}
}