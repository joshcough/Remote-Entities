package com.joshcough.remoteentities;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import com.joshcough.remoteentities.api.RemoteEntity;
import com.joshcough.remoteentities.api.RemoteEntityType;
import com.joshcough.remoteentities.api.features.InventoryFeature;
import com.joshcough.remoteentities.entities.RemotePlayer;
import com.joshcough.remoteentities.exceptions.NoNameException;
import com.joshcough.remoteentities.exceptions.NoTypeException;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CreateEntityContextTest
{
	@Mock
    EntityManager m_entityManager;
	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	RemoteEntity m_remoteEntity;
	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	RemotePlayer m_remotePlayer;

	@Before
	public void setup()
	{
		when(this.m_entityManager.getNextFreeID(anyInt())).thenReturn(1);
		when(this.m_entityManager.createEntity(any(RemoteEntityType.class), anyInt())).thenReturn(this.m_remoteEntity);
		when(this.m_entityManager.createNamedEntity(any(RemoteEntityType.class), anyInt(), anyString())).thenReturn(this.m_remotePlayer);
		when(this.m_remotePlayer.getName()).thenCallRealMethod();
	}

	@Test(expected = NoTypeException.class)
	public void testFailType()
	{
		new CreateEntityContext(this.m_entityManager).create();
	}

	@Test(expected = NoNameException.class)
	public void testFailNoName()
	{
		new CreateEntityContext(this.m_entityManager).withType(RemoteEntityType.Human()).create();
	}

	@Test
	public void testCreateNotNamed()
	{
		RemoteEntity created = new CreateEntityContext(this.m_entityManager).withType(RemoteEntityType.Pig()).withFeatures(mock(InventoryFeature.class)).create();
		assertEquals("Created entity and saved entity should be equal", this.m_remoteEntity, created);
		verify(created.getFeatures(), times(1)).addFeature(any(InventoryFeature.class));
		verify(created, never()).setName(anyString());
		verify(created, never()).setSpeed(anyDouble());
		verify(created, times(1)).setStationary(eq(false));
		verify(created, times(1)).setPushable(eq(true));
	}

	@Test
	public void testCreateNamed()
	{
		RemoteEntity created = new CreateEntityContext(this.m_entityManager).withType(RemoteEntityType.Human()).withName("Test").create();
		assertEquals("Created entity and saved entity should be equal", this.m_remotePlayer, created);
		verify(created, never()).setSpeed(anyDouble());
		verify(created, times(1)).setStationary(eq(false));
		verify(created, times(1)).setPushable(eq(true));
	}
}