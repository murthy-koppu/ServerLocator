package com.imaginea.serverlocator.factory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.imaginea.serverlocator.impl.AppServerLocator;
import com.imaginea.serverlocator.impl.MySQLLocator;
import com.imaginea.serverlocator.impl.ServerProperties;
import com.imaginea.serverlocator.util.ConnectionProperties;
import com.imaginea.serverlocator.util.ServersEnum;

public class ServerLocatorFactory {
	static Logger log = Logger.getLogger(ServerLocatorFactory.class);
	
	public static ServerProperties getServerLocator(ServersEnum[] optimizedChoices,InetAddress iNetAddr, int port) {	
		ServerProperties serverProp = new ServerProperties();
		Socket clientSocket = null;
		try{
			clientSocket = new Socket(iNetAddr, port);	
		}catch (IOException e) {
			log.error("Unable to connect to server", e);
			serverProp.setConnectionStatus(ConnectionProperties.SERVER_UNREACHABLE);
			return serverProp;
		}finally{
			try {
				clientSocket.close();
			} catch (IOException e) {
				log.error("Unable to close the client socket", e);
			}
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
		for(int i = 0; i < 2; i++){
			for(ServersEnum inServerChoice: optimizedChoices){
				serverProp = findServerDetails(iNetAddr, port, inServerChoice, i==0?true:false);
				if(serverProp != null){
					serverProp.setHostName(iNetAddr.getHostName());
					serverProp.setPortNo(port);
					serverProp.setConnectionStatus(ConnectionProperties.SERVER_LISTENING);
					log.debug("Found Server Details with Server Name "+ serverProp.getHostName()+" and version "+serverProp.getVersion());
					return serverProp;
				}				
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
			ServersEnum inServerChoice, boolean isLimitedTimeOut) {
		switch(inServerChoice){
		
		case APP_SERVER:{
			return new AppServerLocator().parseToServerProp(iNetAddr, port, isLimitedTimeOut);
		}
		case MY_SQL:{
			return new MySQLLocator().parseToServerProp(iNetAddr, port, isLimitedTimeOut);			
		}
		
		}
		return null;
	}
}
