package com.imaginea.serverlocator.util;

import java.net.InetAddress;

public class CommunicationChannel {
	private InetAddress fromIp;
	private InetAddress toIp;
	private int fromPort;
	private int toPort;
	private int processId;
	private String processName;

	InetAddress getFromIp() {
		return fromIp;
	}

	void setFromIp(InetAddress fromIp) {
		this.fromIp = fromIp;
	}

	InetAddress getToIp() {
		return toIp;
	}

	void setToIp(InetAddress toIp) {
		this.toIp = toIp;
	}

	int getFromPort() {
		return fromPort;
	}

	void setFromPort(int fromPort) {
		this.fromPort = fromPort;
	}

	int getToPort() {
		return toPort;
	}

	void setToPort(int toPort) {
		this.toPort = toPort;
	}

	int getProcessId() {
		return processId;
	}

	void setProcessId(int processId) {
		this.processId = processId;
	}

	String getProcessName() {
		return processName;
	}

	void setProcessName(String processName) {
		this.processName = processName;
	}

}
