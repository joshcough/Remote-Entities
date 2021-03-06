package com.joshcough.remoteentities.entities;

import java.util.HashMap;
import java.util.Map;

import com.joshcough.remoteentities.EntityManager;
import org.bukkit.entity.Ocelot;
import com.joshcough.remoteentities.api.EntitySound;
import com.joshcough.remoteentities.api.RemoteEntityType;

public class RemoteOcelote extends RemoteAttackingBaseEntity<Ocelot>
{
	public RemoteOcelote(int inID, EntityManager inManager)
	{
		this(inID, null, inManager);
	}

	public RemoteOcelote(int inID, RemoteOceloteEntity inEntity, EntityManager inManager)
	{
		super(inID, RemoteEntityType.Ocelot(), inManager);
		this.m_entity = inEntity;
	}

	@Override
	public String getNativeEntityName()
	{
		return "Ozelot";
	}

	@Override
	protected void setupSounds()
	{
		Map<String, String> randoms = new HashMap<String, String>();
		randoms.put("purr", "mob.cat.purr");
		randoms.put("purreow", "mob.cat.purreow");
		randoms.put("meow", "mob.cat.meow");
		this.setSounds(EntitySound.RANDOM, randoms);
		this.setSound(EntitySound.HURT, "mob.cat.hitt");
		this.setSound(EntitySound.DEATH, "mob.cat.hitt");
	}
}