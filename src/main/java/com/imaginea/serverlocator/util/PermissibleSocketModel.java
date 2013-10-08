package com.imaginea.serverlocator.util;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.ec2.model.IpPermission;

public class PermissibleSocketModel {
	private String ipAddress = "0.0.0.0";
	private int subNetMask = 0;
	private boolean isAllPorts;
	private boolean[] isAllowedPort = new boolean[65536];
	
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
		if (ipPermission.getFromPort() == -1) {
			isAllPorts = true;
		}else{
			addToPermissiblePorts(ipPermission.getFromPort(),ipPermission.getToPort());
		}
		
	}
	
	public void addToPermissiblePorts(int fromPort, int toPort){
		for(int k=fromPort; k <= fromPort; k++){
			isAllowedPort[k] = true;
		}
	}

	public boolean isAllowedPort(int queriedPort){
		return isAllowedPort[queriedPort];
	}
	
	public boolean isAllPorts() {
		return isAllPorts;
	}

}
