package com.joshcough.remoteentities.examples;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import com.joshcough.remoteentities.EntityManager;
import com.joshcough.remoteentities.RemoteEntities;
import com.joshcough.remoteentities.api.RemoteEntity;
import com.joshcough.remoteentities.api.RemoteEntityType;
import com.joshcough.remoteentities.api.features.RemoteTamingFeature;
import com.joshcough.remoteentities.api.features.TamingFeature;

public class ExampleMain extends JavaPlugin implements Listener
{
	private EntityManager npcManager;
	
	public void onEnable()
	{
		Bukkit.getPluginManager().registerEvents(this, this);
		this.npcManager = RemoteEntities.createManager(this);
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent inEvent) throws Exception
	{
		RemoteEntity entity = npcManager.createNamedEntity(RemoteEntityType.Human, inEvent.getPlayer().getLocation(), "test");
		TamingFeature feature = new RemoteTamingFeature(entity);
		feature.tame(inEvent.getPlayer());
		entity.getFeatures().addFeature(feature);
		entity.getMind().addMovementDesire(new DesireFollowTamer(entity, 5, 15), entity.getMind().getHighestMovementPriority() + 1);
	}
}