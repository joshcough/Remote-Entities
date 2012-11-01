package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityTameableAnimal;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.features.TamingFeature;
import de.kumpelblase2.remoteentities.exceptions.NotTameableException;

public class DesireNonTamedAttackNearest extends DesireAttackNearest
{
	protected EntityLiving m_animal;

	public DesireNonTamedAttackNearest(RemoteEntity inEntity, Class<? extends EntityLiving> inTargetClass, float inDistance, boolean inShouldCheckSight, boolean inShouldMeele, int inChance) throws Exception
	{
		super(inEntity, inTargetClass, inDistance, inShouldCheckSight, inShouldMeele, inChance);
		if(!(this.getRemoteEntity().getHandle() instanceof EntityTameableAnimal) && !this.getRemoteEntity().getFeatures().hasFeature("TAMING"))
			throw new NotTameableException();
		
		this.m_animal = this.getRemoteEntity().getHandle();
	}
	
	@Override
	public boolean shouldExecute()
	{
		return this.isTamed() ? false : super.shouldExecute();
	}
	
	protected boolean isTamed()
	{
		if(this.m_animal instanceof EntityTameableAnimal)
			return ((EntityTameableAnimal)this.m_animal).isTamed();
		else
			return ((TamingFeature)this.getRemoteEntity().getFeatures().getFeature("TAMING")).isTamed();
	}
}
