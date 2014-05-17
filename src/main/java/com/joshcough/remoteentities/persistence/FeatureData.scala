package com.joshcough.remoteentities.persistence

import java.lang.reflect.Constructor
import java.util._
import com.joshcough.remoteentities.RemoteEntities
import org.apache.commons.lang3.ClassUtils
import org.bukkit.configuration.serialization.ConfigurationSerializable
import com.joshcough.remoteentities.api.RemoteEntity
import com.joshcough.remoteentities.api.features.Feature
import scala.collection.JavaConversions._

object FeatureData {
  def apply(f: Feature): FeatureData = new FeatureData(f.getClass.getName, f.getSerializableData)

  @SuppressWarnings(Array("unchecked")) def apply(data: Map[String, AnyRef]): FeatureData = {
    val typ = data.get("type").asInstanceOf[String]
    val parameterData = data.get("parameters").asInstanceOf[List[Map[String, AnyRef]]]
    val parameters: Array[ParameterData] =
      if (parameterData == null || parameterData.size == 0) new Array[ParameterData](0)
      else {
        val p = new Array[ParameterData](parameterData.size)
        parameterData.map(ParameterData(_)).foreach(pd => p(pd.pos) = pd)
        p
      }
    new FeatureData(typ, parameters)
  }

}

case class FeatureData(typ: String, parameters: Array[ParameterData]) extends ConfigurationSerializable {

  def serialize: Map[String, AnyRef] = {
    val data: Map[String, AnyRef] = new HashMap[String, AnyRef]
    data.put("type", this.typ)
    val parameterData: List[Map[String, AnyRef]] = new ArrayList[Map[String, AnyRef]]
    for (param <- parameters) {
      parameterData.add(param.serialize)
    }
    data.put("parameters", parameterData)
    data
  }

  @SuppressWarnings(Array("unchecked")) def create(e: RemoteEntity): Feature =
    try {
      val c: Class[_ <: Feature] = Class.forName(this.typ).asInstanceOf[Class[_ <: Feature]]
      val con: Constructor[_ <: Feature] = c.getConstructor(this.getParameterClasses:_*)
      if (con == null) return null
      val values: Array[AnyRef] = new Array[AnyRef](parameters.length)

      for(i <- 0 to values.length - 1)
        values(i) = parameters(i).special match {
          case "entity" => e
          case "manager" => e.getManager
          case _ => EntityData.objectParser.deserialize(parameters(i))
        }
      con.newInstance(values)
    }
    catch {
      case e: Exception => {
        RemoteEntities.getInstance.getLogger.warning("Error when trying to deserialize feature with type " + this.typ + ": ")
        RemoteEntities.getInstance.getLogger.warning(e.getMessage)
        null
      }
    }

  @SuppressWarnings(Array("rawtypes")) def getParameterClasses: Array[Class[_]] = {
    val classes: Array[Class[_]] = new Array[Class[_]](parameters.length)
    for(i <- 0 to classes.length - 1) {
      try {
        var c: Class[_] = ClassUtils.getClass(this.getClass.getClassLoader, parameters(i).typ)
        if (ClassUtils.wrapperToPrimitive(c) != null) c = ClassUtils.wrapperToPrimitive(c)
        classes(i) = c
      }
      catch { case e: Exception => e.printStackTrace }
    }
    classes
  }
}