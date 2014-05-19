package com.joshcough.remoteentities.entities;

import com.joshcough.remoteentities.EntityManager;
import org.bukkit.entity.Spider;
import com.joshcough.remoteentities.api.EntitySound;
import com.joshcough.remoteentities.api.RemoteEntityType;

public class  RemoteSpider extends RemoteAttackingBaseEntity<Spider>
{
	public RemoteSpider(int inID, EntityManager inManager)
	{
		this(inID, null, inManager);
	}

	public RemoteSpider(int inID, RemoteSpiderEntity inEntity, EntityManager inManager)
	{
		super(inID, RemoteEntityType.Spider(), inManager);
		this.m_entity = inEntity;
	}

	@Override
	public String getNativeEntityName()
	{
		return "Spider";
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