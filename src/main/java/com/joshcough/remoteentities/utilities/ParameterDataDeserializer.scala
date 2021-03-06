package com.joshcough.remoteentities.utilities

import java.lang.reflect.Type
import org.bukkit.craftbukkit.libs.com.google.gson._
import com.joshcough.remoteentities.persistence.EntityData
import com.joshcough.remoteentities.persistence.ParameterData

class ParameterDataDeserializer extends JsonDeserializer[ParameterData] {
  def deserialize(inJsonElement: JsonElement, inType: Type, inJsonDeserializationContext: JsonDeserializationContext): ParameterData = {
    val o: JsonObject = inJsonElement.getAsJsonObject
    val data: ParameterData = new ParameterData(
      o.get("type").getAsString,
      o.get("value").getAsString,
      o.get("pos").getAsInt,
      if (o.has("special")) o.get("special").getAsString else ""
    )
    data.copy(value = EntityData.objectParser.deserialize(data))
  }
}