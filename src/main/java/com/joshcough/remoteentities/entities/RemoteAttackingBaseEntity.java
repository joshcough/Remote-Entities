package com.joshcough.remoteentities.entities;

import com.joshcough.remoteentities.EntityManager;
import net.minecraft.server.v1_7_R1.EntityLiving;
import org.bukkit.entity.LivingEntity;
import com.joshcough.remoteentities.api.Fightable;
import com.joshcough.remoteentities.api.RemoteEntityType;
import com.joshcough.remoteentities.utilities.NMSUtil;
import com.joshcough.remoteentities.utilities.WorldUtilities;

public abstract class RemoteAttackingBaseEntity<T extends LivingEntity> extends RemoteBaseEntity<T> implements Fightable
{
	public RemoteAttackingBaseEntity(int inID, RemoteEntityType inType, EntityManager inManager)
	{
		super(inID, inType, inManager);
	}

	@Override
	public void attack(LivingEntity inTarget)
	{
		if(this.m_entity == null)
			return;

		NMSUtil.setGoalTarget(this.m_entity, WorldUtilities.getNMSEntity(inTarget));
	}

	@Override
	public void loseTarget()
	{
		if(this.m_entity == null)
			return;

		NMSUtil.setGoalTarget(this.m_entity, null);
	}

	@Override
	public LivingEntity getTarget()
	{
		if(this.m_entity == null)
			return null;

		EntityLiving target = NMSUtil.getGoalTarget(this.m_entity);
		if(target != null)
			return (LivingEntity)target.getBukkitEntity();

		return null;
	}
}