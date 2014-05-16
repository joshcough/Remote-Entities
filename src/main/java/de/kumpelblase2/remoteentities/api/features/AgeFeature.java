package de.kumpelblase2.remoteentities.api.features;

import com.joshcough.remoteentities.api.features.Feature;

public interface AgeFeature extends Feature
{
	/**
	 * Gets the age of the entity
	 *
	 * @return age
	 */
	public int getAge();

	/**
	 * Sets the age of the entity
	 *
	 * @param inAge age
	 */
	public void setAge(int inAge);
}