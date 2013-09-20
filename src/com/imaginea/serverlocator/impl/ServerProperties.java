package com.imaginea.serverlocator.impl;

import java.util.Properties;

public class ServerProperties {
	private String hostName;
	private int portNo;
	private String version;
	private String serverName;
	private Properties otherProperties = new Properties();
	
	public String getHostName() {
		return hostName;
	}
	public int getPortNo() {
		return portNo;
	}
	public String getVersion() {
		return version;
	}
	public String getServerName() {
		return serverName;
	}
	public Properties getOtherProperties() {
		return otherProperties;
	}
	
	public Object getPropertyValue(String name){
		return otherProperties.getProperty(name);
	}
	
	public String getStringPropertyValue(String name){
		return otherProperties.getProperty(name).toString();
	}
	
	
	void setHostName(String hostName) {
		this.hostName = hostName;
	}
	void setPortNo(int portNo) {
		this.portNo = portNo;
	}
	void setVersion(String version) {
		this.version = version;
	}
	void setServerName(String serverName) {
		this.serverName = serverName;
	}
	Object addProperty(String name, String value){
		return this.otherProperties.put(name, value);
	}
		
}
