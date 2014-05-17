package com.joshcough.remoteentities.nms;

import java.io.IOException;
import java.net.SocketAddress;
import net.minecraft.server.v1_7_R1.*;
import com.joshcough.remoteentities.utilities.ReflectionUtil;

public class RemoteEntityNetworkManager extends NetworkManager
{

	public RemoteEntityNetworkManager(MinecraftServer server) throws IOException
	{
		super(false);

		this.assignReplacementNetworking();
	}

	private void assignReplacementNetworking()
	{
		ReflectionUtil.setNetworkChannel(this, new NullChannel(null));
		ReflectionUtil.setNetworkAddress(this, new SocketAddress(){});
	}
}