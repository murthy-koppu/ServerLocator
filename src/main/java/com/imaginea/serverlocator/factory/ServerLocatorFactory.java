package com.imaginea.serverlocator.factory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;




import com.imaginea.serverlocator.impl.MySQLLocator;
import com.imaginea.serverlocator.impl.ServerProperties;

public class ServerLocatorFactory {
	static Logger log = Logger.getLogger(ServerLocatorFactory.class);
	
	public static ServerProperties getServerLocator(ServersEnum[] optimizedChoices,InetAddress iNetAddr, int port) {	
		ServerProperties serverProp = new ServerProperties();
		try {
			iNetAddr.isReachable(10000);
			try{
				Socket clientSocket = new Socket(iNetAddr, port);	
			}catch (IOException e) {
				log.error("Unable to connect to server", e);
				serverProp.setConnectionStatus(ConnectionProperties.SERVER_UNREACHABLE);
				return serverProp;
			}
			
		} catch (IOException e) {
			log.error("Unable to connect to host", e);
			return serverProp;
		}
		
		serverProp = chooseServerLocatorFmChoices(optimizedChoices, iNetAddr, port,
				serverProp);
		if(serverProp.getConnectionStatus() == ConnectionProperties.SERVER_LISTENING)
			return serverProp;
		log.debug("Unable to find from list of known servers");
		serverProp.setConnectionStatus(ConnectionProperties.UNIDENTIFIED_SERVER_LISTENING);
		return serverProp;
	}

	private static ServerProperties chooseServerLocatorFmChoices(
			ServersEnum[] optimizedChoices, InetAddress iNetAddr, int port,
			ServerProperties serverProp) {
		if(optimizedChoices == null){
			optimizedChoices = ServersEnum.values();
		}
		for(ServersEnum inServerChoice: optimizedChoices){
			serverProp = findServerDetails(iNetAddr, port, inServerChoice);
			if(serverProp != null){
				serverProp.setConnectionStatus(ConnectionProperties.SERVER_LISTENING);
				return serverProp;
			}				
		}
		return serverProp;
	}
	
	public static ServerProperties getServerLocator(InetAddress iNetAddr, int port) {
		return getServerLocator(null, iNetAddr, port);
	}
	
	public static ServerProperties getServerLocator(ServersEnum[] optimizedChoices,String hostName, int port) {
		InetAddress iNetAddr = null;
		try {
			iNetAddr = InetAddress.getByName(hostName);
		} catch (UnknownHostException e) {
			log.error("Unable to identify provided hostname", e);
			return new ServerProperties();
		}
		return getServerLocator(optimizedChoices, iNetAddr, port);
	}
	
	public static ServerProperties getServerLocator(String hostName, int port){
		return getServerLocator(null, hostName, port);
	}

	private static ServerProperties findServerDetails(InetAddress iNetAddr, int port,
			ServersEnum inServerChoice) {
		switch(inServerChoice){
		case MY_SQL:{
			return new MySQLLocator().parseToServerProp(iNetAddr, port);			
		}
		
		}
		return null;
	}
}
