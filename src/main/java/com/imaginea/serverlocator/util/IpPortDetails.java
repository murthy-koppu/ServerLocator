package com.imaginea.serverlocator.util;

public class IpPortDetails {
	private int fromPort;
	private int toPort;
	private boolean isAllPorts;
	private boolean isRange;

	public IpPortDetails(int fromPort, int toPort) {
		super();
		this.fromPort = fromPort;
		this.toPort = toPort;
		if (fromPort == toPort) {
			isRange = true;
		}
		if (fromPort == -1) {
			isAllPorts = true;
		}

	}

	public int getFromPort() {
		return fromPort;
	}

	public void setFromPort(int fromPort) {
		this.fromPort = fromPort;
	}

	public int getToPort() {
		return toPort;
	}

	public void setToPort(int toPort) {
		this.toPort = toPort;
	}

	public boolean isAllPorts() {
		return isAllPorts;
	}

	public boolean isRange() {
		return isRange;
	}
	
	public int getRangeStatus(int inputPort){
		if(inputPort < fromPort){
			return -1;
		}else if(inputPort > toPort){
			return 1;
		}else{
			return 0;
		}				
	}	

}
