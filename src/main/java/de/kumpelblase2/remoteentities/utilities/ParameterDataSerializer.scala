package de.kumpelblase2.remoteentities.utilities

import java.lang.reflect.Type
import org.bukkit.craftbukkit.libs.com.google.gson._
import de.kumpelblase2.remoteentities.persistence.EntityData
import de.kumpelblase2.remoteentities.persistence.ParameterData

class ParameterDataSerializer extends JsonSerializer[ParameterData] {
  def serialize(arg0: ParameterData, arg1: Type, arg2: JsonSerializationContext): JsonElement = {
    val data: JsonObject = new JsonObject
    data.addProperty("type", arg0.typ)
    data.addProperty("pos", arg0.pos)
    data.addProperty("special", arg0.special)
    data.add("value", arg2.serialize(EntityData.objectParser.serialize(arg0.value)))
    data
  }
}