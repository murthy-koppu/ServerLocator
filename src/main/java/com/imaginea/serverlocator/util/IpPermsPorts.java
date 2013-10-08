package com.imaginea.serverlocator.util;

public class IpPermsPorts {
	int fromPort;
	int toPort;
	boolean isRange;

	public IpPermsPorts(int fromPort, int toPort) {
		super();
		this.fromPort = fromPort;
		this.toPort = toPort;
		if (fromPort != toPort) {
			isRange = true;
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

	public boolean isRange() {
		return isRange;
	}

}
