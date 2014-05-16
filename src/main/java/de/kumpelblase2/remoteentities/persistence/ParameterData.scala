package de.kumpelblase2.remoteentities.persistence

import org.bukkit.configuration.serialization.ConfigurationSerializable
import scala.collection.JavaConverters._

object ParameterData {
  def apply(data: java.util.Map[String, AnyRef]): ParameterData =
    new ParameterData(
      data.get("type").asInstanceOf[String],
      data.get("value"),
      data.get("pos").asInstanceOf[Integer],
      data.get("special").asInstanceOf[String]
    )
}

case class ParameterData(typ: String, value: AnyRef, pos: Int, special: String = "") extends ConfigurationSerializable {
  def serialize: java.util.Map[String, AnyRef] = Map(
    "pos" -> pos.asInstanceOf[AnyRef],
    "type" -> typ,
    "value" -> EntityData.objectParser.serialize(this.value),
    "special" -> special
  ).asJava
}