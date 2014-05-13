package de.kumpelblase2.remoteentities.persistence.serializers;

import com.joshcough.remoteentities.EntityManager;
import org.bukkit.plugin.Plugin;
import de.kumpelblase2.remoteentities.*;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.persistence.*;

/**
 * This is a base seralizer which creates the data from an entity and creates an entity from the data, but it does not save it or load it to a file.
 */
public abstract class PreparationSerializer implements IEntitySerializer
{
	protected final Plugin m_plugin;

	public PreparationSerializer(Plugin inPlugin)
	{
		this.m_plugin = inPlugin;
	}

	@Override
	public EntityData prepare(RemoteEntity inEntity)
	{
		return new EntityData(inEntity);
	}

	@Override
	public RemoteEntity create(EntityData inData)
	{
		EntityManager manager = RemoteEntities.getManagerOfPlugin(this.m_plugin.getName());
		CreateEntityContext context = manager.prepareEntity(inData.type);
		context.withName(inData.name).asPushable(inData.pushable).asStationary(inData.stationary).withID(inData.id);
		context.withSpeed(inData.speed).withPathfindingRange(inData.pathfindingRange);
		if(inData.location != null)
			context.atLocation(inData.location.toBukkitLocation());

		RemoteEntity entity = context.create();
		for(FeatureData data : inData.features)
		{
			entity.getFeatures().addFeature(data.create(entity));
		}

		return entity;
	}
}
