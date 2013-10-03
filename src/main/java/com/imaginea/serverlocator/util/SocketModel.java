package com.imaginea.serverlocator.util;

import java.util.Set;

import org.apache.log4j.Logger;

public class SocketModel {
	private static Logger log = Logger.getLogger(SocketModel.class);
	private String ipAddress;
	private int port;
	private boolean isAllPorts;
	private boolean isAllIps;
	private boolean isLocalIp;	
	
	public SocketModel(String ipAddress, String port, Set<String> localIps) throws Exception {
		super();
		setIpAddress(ipAddress, localIps);
		setPort(port);
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress, Set<String> localIps) {
		if(localIps.contains(ipAddress))
			isLocalIp = true;
		this.ipAddress = ipAddress;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	public void setPort(String port) throws Exception{
		try{
			if(port.equals("*")){
				this.port = -1;
				this.isAllPorts = true;
			}else				
				this.port = Integer.parseInt(port);
		}catch(Exception e){
			log.error("Invalid Port Number parsed "+e);
			throw e;
		}		
	}

	public boolean isAllPorts() {
		return isAllPorts;
	}

	public void setAllPorts(boolean isAllPorts) {
		this.isAllPorts = isAllPorts;
	}

	public boolean isAllIps() {
		return isAllIps;
	}

	public void setAllIps(boolean isAllIps) {
		this.isAllIps = isAllIps;
	}

	public boolean isLocalIp() {
		return isLocalIp;
	}

	public void setLocalIp(boolean isLocalIp) {
		this.isLocalIp = isLocalIp;
	}
	

}
