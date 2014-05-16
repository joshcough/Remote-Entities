package com.joshcough.remoteentities.persistence

import de.kumpelblase2.remoteentities.persistence.ParameterData

trait SerializableData {
  /**
   * Returns all the parameters that can be serialized
   *
   * @return Serializeable parameters
   */
  def getSerializableData: Array[ParameterData]
}