package com.joshcough.remoteentities.persistence

trait SerializableData {
  /**
   * Returns all the parameters that can be serialized
   *
   * @return Serializeable parameters
   */
  def getSerializableData: Array[ParameterData]
}