package com.imaginea.serverlocator.util;

public class NetStatProcessModel {
	private String localIp;
	private String foreignIp;
	private String processName;
	private String state;
	private int localPort;
	private int foreignPort;
	private int processId;
	
	public String getLocalIp() {
		return localIp;
	}
	public void setLocalIp(String localIp) {
		this.localIp = localIp;
	}
	public String getForeignIp() {
		return foreignIp;
	}
	public void setForeignIp(String foreignIp) {
		this.foreignIp = foreignIp;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public int getLocalPort() {
		return localPort;
	}
	public void setLocalPort(int localPort) {
		this.localPort = localPort;
	}
	public int getForeignPort() {
		return foreignPort;
	}
	public void setForeignPort(int foreignPort) {
		this.foreignPort = foreignPort;
	}
	public int getProcessId() {
		return processId;
	}
	public void setProcessId(int processId) {
		this.processId = processId;
	}	

}
