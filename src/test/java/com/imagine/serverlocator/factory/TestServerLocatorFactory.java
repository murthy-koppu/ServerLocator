package com.imagine.serverlocator.factory;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.imaginea.serverlocator.factory.ServerLocatorFactory;
import com.imaginea.serverlocator.impl.ServerProperties;
import com.imaginea.serverlocator.util.ConnectionProperties;
import com.imaginea.serverlocator.util.ServersEnum;

public class TestServerLocatorFactory {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetServerLocatorStringIntMySQL() {
		String hostAddress = "localhost";
		ServerProperties serverProp = ServerLocatorFactory.getServerLocator(hostAddress,3306);
		Assert.assertEquals(serverProp.getConnectionStatus(), ConnectionProperties.SERVER_LISTENING);
		Assert.assertEquals(serverProp.getHostName(), hostAddress);
		Assert.assertEquals(serverProp.getPortNo(), 3306);
		Assert.assertEquals(serverProp.getServerName(), ServersEnum.MY_SQL.toString());
		Assert.assertEquals(serverProp.getVersion(), "5.5.27");		
	}
	
	@Test
	public void testGetServerLocatorStringIntFailOnIp() {
		String hostAddress = "localhos";
		ServerProperties serverProp = ServerLocatorFactory.getServerLocator(hostAddress,3306);
		Assert.assertEquals(serverProp.getConnectionStatus(), ConnectionProperties.HOST_UNREACHABLE);
	}
	
	@Test
	public void testGetServerLocatorStringIntAppServer() {
		String hostAddress = "localhost";
		int port = 8080;
		ServerProperties serverProp = ServerLocatorFactory.getServerLocator(hostAddress,port);
		Assert.assertEquals(serverProp.getConnectionStatus(), ConnectionProperties.SERVER_LISTENING);
		Assert.assertEquals(serverProp.getHostName(), hostAddress);
		Assert.assertEquals(serverProp.getPortNo(), port);
		Assert.assertEquals(serverProp.getServerName(), "Apache-Coyote/1.1");
	}
	
	@Test
	public void testGetServerLocatorStringIntOracle() {
		String hostAddress = "127.0.0.1";
		int port = 9005;
		ServerProperties serverProp = ServerLocatorFactory.getServerLocator(hostAddress,port);
		Assert.assertEquals(serverProp.getConnectionStatus(), ConnectionProperties.SERVER_LISTENING);
		Assert.assertEquals(serverProp.getHostName(), hostAddress);
		Assert.assertEquals(serverProp.getPortNo(), port);
		Assert.assertEquals(serverProp.getServerName(), "Oracle Database");
	}


}
