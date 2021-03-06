package com.joshcough.remoteentities.entities;

import com.joshcough.remoteentities.EntityManager;
import org.bukkit.entity.CaveSpider;
import com.joshcough.remoteentities.api.EntitySound;
import com.joshcough.remoteentities.api.RemoteEntityType;

public class RemoteCaveSpider extends RemoteAttackingBaseEntity<CaveSpider>
{
	public RemoteCaveSpider(int inID, EntityManager inManager)
	{
		this(inID, null, inManager);
	}

	public RemoteCaveSpider(int inID, RemoteCaveSpiderEntity inEntity, EntityManager inManager)
	{
		super(inID, RemoteEntityType.CaveSpider(), inManager);
		this.m_entity = inEntity;
	}

	@Override
	public String getNativeEntityName()
	{
		return "CaveSpider";
	}

	@Override
	protected void setupSounds()
	{
		this.setSound(EntitySound.RANDOM, "mob.spider.say");
		this.setSound(EntitySound.HURT, "mob.spider.say");
		this.setSound(EntitySound.DEATH, "mob.spider.death");
		this.setSound(EntitySound.STEP, "mob.spider.step");
	}
}