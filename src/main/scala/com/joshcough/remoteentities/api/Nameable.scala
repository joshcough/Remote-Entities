package com.joshcough.remoteentities.api

trait Nameable {
  /**
   * Gets the name of the entity
   * @return	name
   */
  def getName: String

  /**
   * Sets the name of the entity
   * @param name new name
   */
  def setName(name: String)
}