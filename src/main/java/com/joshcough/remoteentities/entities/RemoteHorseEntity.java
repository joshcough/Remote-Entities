package com.joshcough.remoteentities.entities;

import net.minecraft.server.v1_7_R1.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.util.Vector;
import com.joshcough.remoteentities.api.*;
import com.joshcough.remoteentities.api.features.InventoryFeature;
import com.joshcough.remoteentities.nms.PathfinderGoalSelectorHelper;

public class RemoteHorseEntity extends EntityHorse implements RemoteEntityHandle
{
	private final RemoteEntity m_remoteEntity;

	public RemoteHorseEntity(World inWorld)
	{
		this(inWorld, null);
	}

	public RemoteHorseEntity(World inWorld, RemoteEntity inRemoteEntity)
	{
		super(inWorld);
		this.m_remoteEntity = inRemoteEntity;
		new PathfinderGoalSelectorHelper(this.goalSelector).clearGoals();
		new PathfinderGoalSelectorHelper(this.targetSelector).clearGoals();
	}

	@Override
	public RemoteEntity getRemoteEntity()
	{
		return this.m_remoteEntity;
	}

	@Override
	public Inventory getInventory()
	{
		if(!this.m_remoteEntity.getFeatures().hasFeature(InventoryFeature.class))
			return null;

		return this.m_remoteEntity.getFeatures().getFeature(InventoryFeature.class).getInventory();
	}

	@Override
	public void g(double x, double y, double z)
	{
		if(this.m_remoteEntity == null)
		{
			super.g(x, y, z);
			return;
		}

		Vector vector = ((RemoteBaseEntity)this.m_remoteEntity).onPush(x, y, z);
		if(vector != null)
			super.g(vector.getX(), vector.getY(), vector.getZ());
	}

	@Override
	public void move(double d0, double d1, double d2)
	{
		if(this.m_remoteEntity != null && this.m_remoteEntity.isStationary())
			return;

		super.move(d0, d1, d2);
	}

	@Override
	public void e(float inXMotion, float inZMotion)
	{
		float[] motion = new float[] { inXMotion, inZMotion, (float)this.motY };
		this.motY = (double)motion[2];
		super.e(motion[0], motion[1]);
	}

	@Override
	public void collide(Entity inEntity)
	{
		if(this.getRemoteEntity() == null)
		{
			super.collide(inEntity);
			return;
		}

		if(((RemoteBaseEntity)this.m_remoteEntity).onCollide(inEntity.getBukkitEntity()))
			super.collide(inEntity);
	}

	@Override
	public boolean a(EntityHuman entity)
	{
		if(this.getRemoteEntity() == null)
			return super.a(entity);

		if(!(entity.getBukkitEntity() instanceof Player))
			return super.a(entity);

		return ((RemoteBaseEntity)this.m_remoteEntity).onInteract((Player)entity.getBukkitEntity()) && super.a(entity);
	}

	@Override
	public void die(DamageSource damagesource)
	{
		((RemoteBaseEntity)this.m_remoteEntity).onDeath();
		super.die(damagesource);
	}

	@Override
	protected void b(float f) {
		//Taken from EntityHorse.java#276 - 297
		//modified to work with custom sounds
		if (f > 1.0F) {
			this.makeSound(this.m_remoteEntity.getSound(EntitySound.LAND), 0.4F, 1.0F);
		}

		int i = MathHelper.f(f * 0.5F - 3.0F);

		if (i > 0) {
			this.damageEntity(DamageSource.FALL, (float) i);
			if (this.passenger != null) {
				this.passenger.damageEntity(DamageSource.FALL, (float) i);
			}

			Block j = this.world.getType(MathHelper.floor(this.locX), MathHelper.floor(this.locY - 0.2D - (double) this.lastYaw), MathHelper.floor(this.locZ));

			if (j.getMaterial() != Material.AIR) {
				StepSound stepsound = j.stepSound;

				this.world.makeSound(this, stepsound.getStepSound(), stepsound.getVolume1() * 0.5F, stepsound.getVolume2() * 0.75F);
			}
		}
	}
}