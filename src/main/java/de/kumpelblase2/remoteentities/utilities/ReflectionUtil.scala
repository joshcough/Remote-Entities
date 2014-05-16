package de.kumpelblase2.remoteentities.utilities

import java.lang.reflect.Field
import java.net.SocketAddress
import com.joshcough.remoteentities.RemoteEntities
import net.minecraft.util.io.netty.channel.Channel
import org.bukkit.Bukkit
import de.kumpelblase2.remoteentities.persistence.ParameterData
import de.kumpelblase2.remoteentities.persistence.SerializeAs

object ReflectionUtil {

  private final val s_cachedFields = new collection.mutable.HashMap[String, Field]()

  /**
   * Replaces the goal selector of an entity with a new one
   * @param inEntity			entity
   * @param inSelectorName	name of the selector (targetSelector or movementSelector)
   * @param inNewSelector		new selector
   */
  def replaceGoalSelector(inEntity: AnyRef, inSelectorName: String, inNewSelector: AnyRef): Unit =
    try getOrRegisterField(inEntity.getClass, inSelectorName).set(inEntity, inNewSelector)
    catch { case e: Exception => e.printStackTrace }

  /**
   * Registers custom entity class at the native minecraft entity enum.
   * Automatically clears internal maps first @see ReflectionUtil#clearEntityType(String, int)
   * @param inClass	class of the entity
   * @param inName	minecraft entity name
   * @param inID		minecraft entity id
   */
  def registerEntityType(inClass: Class[_], inName: String, inID: Int): Unit =
    try {
      clearEntityType(inName, inID)
      val m = getNMSClassByName("EntityTypes").getDeclaredMethod("a", Array(classOf[Class[_]], classOf[String], classOf[Int]):_*)
      m.setAccessible(true)
      m.invoke(m, inClass, inName, inID.asInstanceOf[AnyRef])
    }
    catch { case e: Exception => e.printStackTrace }

  /**
   * Clears the entity name and entity id from the EntityTypes internal c and e map to allow registering of those names with different values.
   * The other maps are not touched and stay as they are.
   * @param inName    The internal name of the entity
   * @param inID      The internal id of the entity
   */
  def clearEntityType(inName: String, inID: Int): Unit =
    try {
      getOrRegisterNMSField("EntityTypes", "c").get(null).asInstanceOf[java.util.Map[_, _]].remove(inName)
      getOrRegisterNMSField("EntityTypes", "e").get(null).asInstanceOf[java.util.Map[_, _]].remove(inID)
    }
    catch { case e: Exception => e.printStackTrace }

  /**
   * Checks if the entity is jumping.
   * @param inEntity  The entity to check
   * @return          True if it is, otherwise false
   */
  def isJumping(inEntity: AnyRef): Boolean =
    try getOrRegisterNMSField("EntityLiving", "bd").getBoolean(inEntity)
    catch { case e: Exception => false }

  /**
   * Gets the data for the parameters of the classes' constructor
   * @param inClass   The class to get the data for
   * @return          List of data for each parameter in order
   */
  def getParameterDataForClass(inClass: AnyRef): java.util.List[ParameterData] = {
    var clazz: Class[_] = inClass.getClass
    val parameters = new java.util.ArrayList[ParameterData]
    val membersLooked = new collection.mutable.HashSet[String]()
    while (clazz ne classOf[AnyRef]) {
      clazz.getDeclaredFields.foreach(_.setAccessible(true))
      for (field <- clazz.getDeclaredFields; if ! membersLooked.contains(field.getName)) {
        membersLooked.add(field.getName)
        field.getAnnotations.collect{ case c: SerializeAs => c }.headOption.foreach(sas =>
          try parameters.add(new ParameterData(field.getType.getName, field.get(inClass), Math.max(0, sas.pos - 1), sas.special))
          catch { case e: Exception =>
            RemoteEntities.getInstance.getLogger.warning("Unable to add desire parameter. " + e.getMessage)
          }
        )
      }
      clazz = clazz.getSuperclass
    }
    parameters
  }

  /**
   * Gets the current minecraft revision
   * @return  The revision as string in the format "X.X_RX"
   */
  def getMinecraftRevision: String = {
    val remaining = Bukkit.getServer.getClass.getPackage.getName.replace("org.bukkit.craftbukkit.", "")
    remaining.split("\\.")(0)
  }

  /**
   * Gets the nms class with the given name
   * @param inName    The internal name of the class
   * @return          The class
   */
  def getNMSClassByName(inName: String): Class[_] =
    try Class.forName("net.minecraft.server." + RemoteEntities.MINECRAFT_REVISION + "." + inName)
    catch { case e: Exception => e.printStackTrace; null }

  /**
   * Sets the new socket channel of the given manager.
   * @param inManager The manager to change the channel of
   * @param inChannel The new channel
   */
  def setNetworkChannel(inManager: AnyRef, inChannel: Channel): Unit =
    try getOrRegisterNMSField("NetworkManager", "k").set(inManager, inChannel)
    catch { case e: Exception => /* TODO: really? */ }

  /**
   * Sets the network address of the given network manager.
   * @param inManager The manager to change the address of
   * @param inAddress The new address
   */
  def setNetworkAddress(inManager: AnyRef, inAddress: SocketAddress): Unit =
    try getOrRegisterNMSField("NetworkManager", "l").set(inManager, inAddress)
    catch { case e: Exception => /* TODO: really? */ }

  /**
   * Gets a declared field of the given class and caches it.
   * If a field is not cached it will attempt to get it from the given class.
   * @param inSource  The class which has the field
   * @param inField   The field name
   * @return          The field
   */
  def getOrRegisterField(inSource: Class[_], inField: String): Field =
    try {
      val id: String = inSource.getName + "_" + inField
      s_cachedFields.getOrElseUpdate(id, {
        val f = inSource.getDeclaredField(inField)
        f.setAccessible(true)
        f
      })
    }
    catch { case e: Exception => { e.printStackTrace; null } }

  private[utilities] def getOrRegisterNMSField(inNMSClass: String, inField: String): Field =
    try {
      val id: String = inNMSClass + "_" + inField
      s_cachedFields.getOrElseUpdate(id, {
        val f = getNMSClassByName(inNMSClass).getDeclaredField(inField)
        f.setAccessible(true)
        f
      })
    }
    catch { case e: Exception => { e.printStackTrace; null } }
}