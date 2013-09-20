package com.imaginea.serverlocator.impl;

import java.net.InetAddress;

public interface ServerLocator {
	//public ServerProperties 
	public ServerProperties parseToServerProp(InetAddress iNetAddr, int portNo);
}
