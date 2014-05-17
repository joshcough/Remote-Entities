package com.joshcough.remoteentities.persistence.serializers

import java.io._
import com.joshcough.remoteentities.RemoteEntities
import org.bukkit.craftbukkit.libs.com.google.gson.Gson
import org.bukkit.craftbukkit.libs.com.google.gson.GsonBuilder
import org.bukkit.plugin.Plugin
import com.joshcough.remoteentities.persistence.EntityData
import com.joshcough.remoteentities.persistence.ParameterData
import com.joshcough.remoteentities.utilities.ParameterDataDeserializer
import com.joshcough.remoteentities.utilities.ParameterDataSerializer

/**
 * This represents a serializer which outputs the serialized data into a json format/file.
 */
class JSONSerializer(p: Plugin, m_dataClass: Class[_ <: Array[EntityData]]) extends PreparationSerializer(p) {

  protected var m_gson: Gson = new GsonBuilder().setPrettyPrinting.registerTypeAdapter(
    classOf[ParameterData], new ParameterDataSerializer
  ).registerTypeAdapter(classOf[ParameterData], new ParameterDataDeserializer).create

  def this(inPlugin: Plugin) = this(inPlugin, classOf[Array[EntityData]])

  def save(inData: Array[EntityData]): Boolean = this.writeToFile(this.m_gson.toJson(inData))

  def loadData: Array[EntityData] = {
    val jsonFile = new File(RemoteEntities.getInstance.getDataFolder, this.m_plugin.getName + File.separator + "entities.json")
    if (!jsonFile.exists) new Array[EntityData](0)
    else try this.m_gson.fromJson(new FileReader(jsonFile), this.m_dataClass)
         catch { case e: Exception => new Array[EntityData](0) }
  }

  protected def writeToFile(inJson: String): Boolean = {
    try {
      val fileFolder = new File(RemoteEntities.getInstance.getDataFolder, this.m_plugin.getName)
      if (!fileFolder.exists && !fileFolder.mkdirs) false
      else {
        val jsonFile: File = new File(fileFolder, "entities.json")
        if (!jsonFile.exists && !jsonFile.createNewFile) false
        else {
          val writer = new FileWriter(jsonFile)
          writer.write(inJson)
          writer.close
          true
        }
      }
    }
    catch { case e: Exception => false }
  }
}