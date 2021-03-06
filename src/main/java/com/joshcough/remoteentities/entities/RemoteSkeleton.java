package com.joshcough.remoteentities.entities;

import com.joshcough.remoteentities.EntityManager;
import net.minecraft.server.v1_7_R1.EntityLiving;
import org.bukkit.entity.Skeleton;
import com.joshcough.remoteentities.api.EntitySound;
import com.joshcough.remoteentities.api.RemoteEntityType;

public class RemoteSkeleton extends RemoteAttackingBaseEntity<Skeleton>
{
	public RemoteSkeleton(int inID, EntityManager inManager)
	{
		this(inID, null, inManager);
	}

	public RemoteSkeleton(int inID, EntityLiving inEntity, EntityManager inManager)
	{
		super(inID, RemoteEntityType.Skeleton(), inManager);
		this.m_entity = inEntity;
	}

	@Override
	public String getNativeEntityName()
	{
		return "Skeleton";
	}

	@Override
	protected void setupSounds()
	{
		this.setSound(EntitySound.RANDOM, "mob.skeleton.say");
		this.setSound(EntitySound.HURT, "mob.skeleton.hurt");
		this.setSound(EntitySound.DEATH, "mob.skeleton.death");
		this.setSound(EntitySound.STEP, "mob.skeleton.step");
		this.setSound(EntitySound.ATTACK, "random.bow");
	}
}