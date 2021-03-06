package com.joshcough.remoteentities;

import java.util.*;

import com.joshcough.remoteentities.EntityManager;
import org.bukkit.Location;
import com.joshcough.remoteentities.api.RemoteEntity;
import com.joshcough.remoteentities.api.RemoteEntityType;
import com.joshcough.remoteentities.api.features.Feature;
import com.joshcough.remoteentities.exceptions.NoNameException;
import com.joshcough.remoteentities.exceptions.NoTypeException;

public class CreateEntityContext
{
	private RemoteEntityType m_type;
	private String m_name;
	private int m_id;
	private Location m_location;
	private List<Feature> m_features;
	private final EntityManager m_manager;
	private boolean m_stationary = false;
	private boolean m_pushable = true;
	private double m_speed = -1;
	private double m_maxHealth = -1;
	private double m_pathfindingRange = -1;

	public CreateEntityContext(EntityManager inManager)
	{
		this.m_features = new ArrayList<Feature>();
		this.m_manager = inManager;
		this.m_id = inManager.getNextFreeID();
	}

	/**
	 * Sets the type of the entity
	 *
	 * @param inType	Type
	 * @return			Context
	 */
	public CreateEntityContext withType(RemoteEntityType inType)
	{
		this.m_type = inType;
		return this;
	}

	/**
	 * Sets the location to spawn at. When the location is null, it won't get spawned.
	 *
	 * @param inLocation	Location to spawn at
	 * @return				Context
	 */
	public CreateEntityContext atLocation(Location inLocation)
	{
		this.m_location = inLocation;
		return this;
	}

	/**
	 * Sets the id of the entity. When the ID is already used, the next free ID will be used.
	 * That means that you might not get the wanted entity id
	 *
	 * @param inID	Id of the entity
	 * @return		Context
	 */
	public CreateEntityContext withID(int inID)
	{
		this.m_id = inID;
		return this;
	}

	/**
	 * Sets the name of the entity
	 *
	 * @param inName	Name
	 * @return			Context
	 */
	public CreateEntityContext withName(String inName)
	{
		this.m_name = inName;
		return this;
	}

	/**
	 * Features that the entity should have on creation
	 *
	 * @param inFeatures	Features
	 * @return				Context
	 */
	public CreateEntityContext withFeatures(Feature... inFeatures)
	{
		this.m_features.clear();
		this.m_features.addAll(Arrays.asList(inFeatures));
		return this;
	}

	/**
	 * Sets the stationary status on creation
	 *
	 * @param inStationary	Stationary
	 * @return				Context
	 */
	public CreateEntityContext asStationary(boolean inStationary)
	{
		this.m_stationary = inStationary;
		return this;
	}

	/**
	 * Sets the pushable status on creation
	 *
	 * @param inPushable	Pushable
	 * @return				Context
	 */
	public CreateEntityContext asPushable(boolean inPushable)
	{
		this.m_pushable = inPushable;
		return this;
	}

	/**
	 * Sets the speed on creation
	 *
	 * @param inSpeed	Speed
	 * @return			Context
	 */
	public CreateEntityContext withSpeed(double inSpeed)
	{
		this.m_speed  = inSpeed;
		return this;
	}

	/**
	 * Sets the max health on creation
	 *
	 * @param inMaxHealth	Max Health
	 * @return				Context
	 */
	public CreateEntityContext withMaxHealth(double inMaxHealth)
	{
		this.m_maxHealth = inMaxHealth;
		return this;
	}

	/**
	 * Sets the initial pathfinding range
	 *
	 * @param inRange   the range
	 * @return          context
	 */
	public CreateEntityContext withPathfindingRange(double inRange)
	{
		this.m_pathfindingRange = inRange;
		return this;
	}

	/**
	 * Creates the entity with the earlier specified parameters
	 *
	 * @return					Created entity
	 * @throws NoTypeException	When no type is specified
	 * @throws NoNameException	When no name is specified while trying to spawn a named entity
	 * @throws InternalError    When an error occurred during creation process
	 */
	public RemoteEntity create()
	{
		RemoteEntity created;

		if(this.m_type == null)
			throw new NoTypeException();

		this.m_id = this.m_manager.getNextFreeID(this.m_id);

		if(this.m_type.isNamed())
		{
			if(this.m_name == null)
				throw new NoNameException("Tried to spawn a named entity without name");

			created = this.m_manager.createNamedEntity(this.m_type, this.m_id, this.m_name);
		}
		else
			created = this.m_manager.createEntity(this.m_type, this.m_id);

		if(created == null)
			throw new InternalError("Was not able to create entity with given type and id. Type was " + this.m_type + " and id " + this.m_id);

		for(Feature feature : this.m_features)
		{
			created.getFeatures().addFeature(feature);
		}

		created.setStationary(this.m_stationary);
		created.setPushable(this.m_pushable);
		if(this.m_speed != -1)
			created.setSpeed(this.m_speed);

		if(this.m_location != null)
			created.spawn(this.m_location);

		if(this.m_maxHealth != -1 && created.getBukkitEntity() != null)
			created.getBukkitEntity().setMaxHealth(this.m_maxHealth);

		if(this.m_pathfindingRange != -1)
			created.setPathfindingRange(this.m_pathfindingRange);

		return created;
	}
}