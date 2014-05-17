package com.joshcough.remoteentities;

import com.joshcough.remoteentities.RemoteEntities;
import org.bukkit.command.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import com.joshcough.remoteentities.exceptions.PluginNotEnabledException;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RemoteEntitiesTest
{
	@Mock
    RemoteEntities m_remoteEntities;

	@Before
	public void setup()
	{
		when(this.m_remoteEntities.getConfig()).thenReturn(new YamlConfiguration());
		when(this.m_remoteEntities.onCommand(any(CommandSender.class), any(Command.class), anyString(), any(String[].class))).thenCallRealMethod();
	}

	@Test(expected = PluginNotEnabledException.class)
	public void testCreateManagerFail()
	{
		RemoteEntities.createManager(null);
	}
}