package com.imagine.serverlocator.factory;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.imaginea.serverlocator.factory.ConnectionProperties;
import com.imaginea.serverlocator.factory.ServerLocatorFactory;
import com.imaginea.serverlocator.factory.ServersEnum;
import com.imaginea.serverlocator.impl.ServerProperties;

public class TestServerLocatorFactory {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetServerLocatorStringInt() {
		ServerProperties serverProp = ServerLocatorFactory.getServerLocator("localhost",3306);
		Assert.assertEquals(serverProp.getConnectionStatus(), ConnectionProperties.SERVER_LISTENING);
		Assert.assertEquals(serverProp.getHostName(), "localhost");
		Assert.assertEquals(serverProp.getPortNo(), 3306);
		Assert.assertEquals(serverProp.getServerName(), ServersEnum.MY_SQL.toString());
		Assert.assertEquals(serverProp.getVersion(), "5.5.27");
		
	}

}
