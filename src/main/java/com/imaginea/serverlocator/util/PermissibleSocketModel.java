package com.imaginea.serverlocator.util;

import com.amazonaws.services.ec2.model.IpPermission;

public class PermissibleSocketModel {
	private String ipAddress = "0.0.0.0";
	private int subNetMask = 0;
	private int fromPort;
	private int toPort;
	private boolean isAllPorts;
	private boolean isRange;

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public int getSubNetMask() {
		return subNetMask;
	}

	public void setSubNetMask(int subNetMask) {
		this.subNetMask = subNetMask;
	}

	public PermissibleSocketModel(IpPermission ipPermission) {
		super();
		String[] ipAndSubnetArr = ipPermission.getIpRanges().get(0).split("/");
		this.subNetMask = Integer.parseInt(ipAndSubnetArr[1]);
		this.ipAddress = ipAndSubnetArr[0];
		this.fromPort = ipPermission.getFromPort();
		this.toPort = ipPermission.getToPort();
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

	public int getRangeStatus(int inputPort) {
		if (inputPort < fromPort) {
			return -1;
		} else if (inputPort > toPort) {
			return 1;
		} else {
			return 0;
		}
	}

}
