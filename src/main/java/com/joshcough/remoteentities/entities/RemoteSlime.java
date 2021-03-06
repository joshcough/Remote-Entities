package com.joshcough.remoteentities.entities;

import java.util.HashMap;
import java.util.Map;

import com.joshcough.remoteentities.EntityManager;
import org.bukkit.entity.Slime;
import com.joshcough.remoteentities.api.EntitySound;
import com.joshcough.remoteentities.api.RemoteEntityType;

public class RemoteSlime extends RemoteAttackingBaseEntity<Slime>
{
	public RemoteSlime(int inID, EntityManager inManager)
	{
		this(inID, null, inManager);
	}

	public RemoteSlime(int inID, RemoteSlimeEntity inEntity, EntityManager inManager)
	{
		super(inID, RemoteEntityType.Slime(), inManager);
		this.m_entity = inEntity;
	}

	@Override
	public String getNativeEntityName()
	{
		return "Slime";
	}

	@Override
	protected void setupSounds()
	{
		Map<String, String> hurt = new HashMap<String, String>();
		hurt.put("big", "mob.slime.big");
		hurt.put("small", "mob.slime.small");
		this.setSounds(EntitySound.HURT, hurt);
		this.setSounds(EntitySound.DEATH, new HashMap<String, String>(hurt));
		this.setSound(EntitySound.ATTACK, "mob.attack");
		this.setSounds(EntitySound.STEP, new HashMap<String, String>(hurt));
	}
}