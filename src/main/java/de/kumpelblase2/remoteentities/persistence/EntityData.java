package de.kumpelblase2.remoteentities.persistence;

import java.util.*;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;
import de.kumpelblase2.remoteentities.api.features.Feature;
import de.kumpelblase2.remoteentities.entities.RemoteBaseEntity;

public class EntityData implements ConfigurationSerializable
{
	public int id;
	public RemoteEntityType type;
	public String name;
	public LocationData location;
	public boolean stationary;
	public boolean pushable;
	public double speed;
	public double pathfindingRange = 32;
	public FeatureData[] features = new FeatureData[0];
	public static transient ObjectParser objectParser = new ObjectParser();

	public EntityData()
	{
	}

	public EntityData(RemoteEntity inEntity)
	{
		if(inEntity == null)
			return;

		this.id = inEntity.getID();
		this.type = inEntity.getType();
		this.name = inEntity.getName() != null && inEntity.getName().length() > 0 ? inEntity.getName() : "";
		if(inEntity.isSpawned())
			this.location = new LocationData(inEntity.getBukkitEntity().getLocation());
		else
		{
			if(inEntity instanceof RemoteBaseEntity)
			{
				RemoteBaseEntity base = (RemoteBaseEntity)inEntity;
				if(base.getUnloadedLocation() != null)
					this.location = new LocationData(base.getUnloadedLocation());
				else
					this.location = new LocationData();
			}
			else
				this.location = new LocationData();
		}

		this.stationary = inEntity.isStationary();
		this.pushable = inEntity.isPushable();
		this.speed = inEntity.getSpeed();
		this.pathfindingRange = inEntity.getPathfindingRange();
		List<FeatureData> featureList = new ArrayList<FeatureData>();
		for(int i = 0; i < inEntity.getFeatures().getAllFeatures().size(); i++)
		{
			Feature f = inEntity.getFeatures().getAllFeatures().get(i);
			if(!f.getClass().isAnnotationPresent(IgnoreSerialization.class))
				featureList.add(new FeatureData(f));
		}

		this.features = featureList.toArray(new FeatureData[featureList.size()]);
	}

	@SuppressWarnings("unchecked")
	public EntityData(Map<String, Object> inData)
	{
		this.id = (Integer)inData.get("id");
		this.type = RemoteEntityType.valueOf((String)inData.get("type"));
		this.name = (String)inData.get("name");
		this.location = new LocationData((Map<String, Object>)inData.get("location"));
		this.stationary = (Boolean)inData.get("stationary");
		this.pushable = (Boolean)inData.get("pushable");
		this.speed = ((Double)inData.get("speed")).floatValue();
		if(inData.containsKey("pathfindingRange"))
			this.pathfindingRange = (Double)inData.get("pathfindingRange");

		List<Map<String, Object>> dataList = (List<Map<String, Object>>)inData.get("actionDesires");

		dataList = (List<Map<String, Object>>)inData.get("features");
		if(dataList != null)
		{
			this.features = new FeatureData[dataList.size()];
			for(int i = 0; i < this.features.length; i++)
			{
				this.features[i] = new FeatureData(dataList.get(i));
			}
		}
	}

	@Override
	public Map<String, Object> serialize()
	{
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("id", this.id);
		data.put("type", this.type.name());
		data.put("name", this.name);
		data.put("location", location.serialize());
		data.put("stationary", this.stationary);
		data.put("pushable", this.pushable);
		data.put("speed", this.speed);
		data.put("pathfindingRange", this.pathfindingRange);
		List<Map<String, Object>> desirelist = new ArrayList<Map<String, Object>>();
		data.put("movementDesires", desirelist);
		List<Map<String, Object>> featureList = new ArrayList<Map<String, Object>>();
		for(FeatureData fd : this.features)
		{
			featureList.add(fd.serialize());
		}
		data.put("features", featureList);
		return data;
	}
}
