package com.imaginea.serverlocator.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import com.imaginea.serverlocator.util.Utils;

public class MySQLLocator implements ServerLocator{

	@Override
	public ServerProperties parseToServerProp(InetAddress iNetAddr, int port) {
			try {
				Utils.getDataInStreamFromServer(iNetAddr, port);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
		
		
		return null;
	}
	
}
