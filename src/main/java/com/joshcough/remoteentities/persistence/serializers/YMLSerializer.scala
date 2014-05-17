package com.joshcough.remoteentities.persistence.serializers

import java.io.File
import java.util.ArrayList
import java.util.List
import com.joshcough.remoteentities.RemoteEntities
import org.bukkit.configuration.file.{FileConfiguration, YamlConfiguration}
import org.bukkit.configuration.serialization.ConfigurationSerialization
import org.bukkit.plugin.Plugin
import com.joshcough.remoteentities.persistence.{EntityData, ISingleEntitySerializer}

/**
 * This represents a serializer which outputs the serialized data to a yml string/file.
 */
class YMLSerializer(p: Plugin) extends PreparationSerializer(p) with ISingleEntitySerializer {

  protected var m_config: FileConfiguration = null
  protected var m_configFile: File = null

  ConfigurationSerialization.registerClass(classOf[EntityData])

  def save(inData: Array[EntityData]): Boolean =
    try {
      if ((this.m_config == null || this.m_configFile == null) && !this.loadConfig) false
      else {
        this.m_config.set("entities", inData)
        this.m_config.save(this.m_configFile)
        true
      }
    }
    catch { case e: Exception => false }

  @SuppressWarnings(Array("unchecked"))
  def loadData: Array[EntityData] = {
    if (this.m_configFile == null || this.m_config == null) {
      if (!this.loadConfig) return new Array[EntityData](0)
    }
    if (!this.m_config.contains("entities")) return new Array[EntityData](0)
    var entitydata: List[EntityData] = this.m_config.getList("entities").asInstanceOf[List[EntityData]]
    if (entitydata == null) {
      entitydata = new ArrayList[EntityData]
      this.m_config.set("entities", entitydata)
    }
    entitydata.toArray(new Array[EntityData](entitydata.size))
  }

  @SuppressWarnings(Array("unchecked"))
  def save(inData: EntityData) {
    try
      if (this.m_config == null || this.m_configFile == null && !this.loadConfig) ()
      else {
        this.m_config.getList("entities").asInstanceOf[List[EntityData]].add(inData)
        this.m_config.save(this.m_configFile)
      }
    catch { case e: Exception => e.printStackTrace }
  }

  def load(p: AnyRef): EntityData = {
    this.loadData.find(ed => p match {
      case s: String if s == ed.name => true
      case i: Integer if i == ed.id =>  true
      case _ => false
    }).getOrElse(null)
  }

  protected def loadConfig: Boolean =
    try {
      val fileFolder: File = new File(RemoteEntities.getInstance.getDataFolder, this.m_plugin.getName)
      if (!fileFolder.exists && !fileFolder.mkdirs) false
      else {
        this.m_configFile = new File(fileFolder, "entities.yml")
        if (!this.m_configFile.exists && !this.m_configFile.createNewFile) false
        else {
          this.m_config = YamlConfiguration.loadConfiguration(this.m_configFile)
          true
        }
      }
    }
    catch { case e: Exception => e.printStackTrace; false }
}