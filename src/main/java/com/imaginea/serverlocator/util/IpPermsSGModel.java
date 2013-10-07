package com.imaginea.serverlocator.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.amazonaws.services.ec2.model.IpPermission;

public class IpPermsSGModel {
	private String ipAddress = "";
	/*
	 * private int fromPort = -1; private int toPort = -1;
	 */
	private String protocol;
	private int subNetMask = 0;
	List<IpPortDetails> distinctPorts = new ArrayList<IpPortDetails>();

	private boolean merge(IpPermission otherIpPerms) {
		if (this.protocol.equals(otherIpPerms.getIpProtocol())) {
			if (mergeOnIPAddress(otherIpPerms)) {
				mergeOnPorts(otherIpPerms);
			}
		}
		return true;
	}

	private boolean mergeOnIPAddress(IpPermission otherIpPerms) {
		String[] otherIpParts = otherIpPerms.getIpRanges().get(0).split("/");
		int otherSubNetMask = Integer.parseInt(otherIpParts[1]);
		int diffSubNetMask = this.subNetMask - otherSubNetMask;
		if (diffSubNetMask == 0) {
			if (this.ipAddress.equals(otherIpPerms.getIpRanges().get(0))) {
				return true;
			}
			return false;
		} else {
			String[] otherObjIpAddressCIDRParts = otherIpParts[0].split(".");
			String[] ipAddressCIDRParts = ipAddress.split(".");
			int minimumMask = diffSubNetMask < 0 ? subNetMask : otherSubNetMask;
			for (int k = 0; k < 4 && minimumMask > 0; k++, minimumMask -= 8) {
				int otherObjIpAddrSubCIDRPart = Integer
						.parseInt(otherObjIpAddressCIDRParts[k]);
				int ipAddrSubCIDRPart = Integer.parseInt(ipAddressCIDRParts[k]);
				int subNetMaskResult = otherObjIpAddrSubCIDRPart
						& ipAddrSubCIDRPart;
				if (diffSubNetMask < 0) {
					if (subNetMaskResult != ipAddrSubCIDRPart)
						return false;
				} else {
					if (subNetMaskResult != otherObjIpAddrSubCIDRPart)
						return false;
				}
			}
			ipAddress = diffSubNetMask < 0 ? ipAddress : otherIpParts[0];
		}
		return true;
	}

	private boolean mergeOnIPAddress(IpPermsSGModel otherIpPerms) {
		int diffSubNetMask = this.subNetMask - otherIpPerms.subNetMask;
		if (diffSubNetMask == 0) {
			if (this.ipAddress.equals(otherIpPerms.ipAddress)) {
				return true;
			}
			return false;
		} else {
			String[] otherObjIpAddressCIDRParts = otherIpPerms.getIpAddress()
					.split(".");
			String[] ipAddressCIDRParts = ipAddress.split(".");
			int minimumMask = diffSubNetMask < 0 ? subNetMask
					: otherIpPerms.subNetMask;
			for (int k = 0; k < 4 && minimumMask > 0; k++, minimumMask -= 8) {
				int otherObjIpAddrSubCIDRPart = Integer
						.parseInt(otherObjIpAddressCIDRParts[k]);
				int ipAddrSubCIDRPart = Integer.parseInt(ipAddressCIDRParts[k]);
				int subNetMaskResult = otherObjIpAddrSubCIDRPart
						& ipAddrSubCIDRPart;
				if (diffSubNetMask < 0) {
					if (subNetMaskResult != ipAddrSubCIDRPart)
						return false;
				} else {
					if (subNetMaskResult != otherObjIpAddrSubCIDRPart)
						return false;
				}
			}
			ipAddress = diffSubNetMask < 0 ? ipAddress : otherIpPerms.ipAddress;
		}
		return true;
	}

	private void mergeOnPorts(IpPermission otherIpPerms) {
		int otherObjFromPort = otherIpPerms.getFromPort();
		int otherObjToPort = otherIpPerms.getToPort();
		IpPortDetails otherObjIpPort = new IpPortDetails(otherObjFromPort,
				otherObjToPort);

		if (distinctPorts.isEmpty() == true) {
			distinctPorts.add(otherObjIpPort);
		}else if (otherObjFromPort == -1) {
			distinctPorts.clear();
			distinctPorts.add(otherObjIpPort);
			return;
		}else {
			IpPortDetails existingPorts = null;
			existingPorts = distinctPorts.get(0);
			if (existingPorts.isAllPorts()) {
				return;
			}
			for (int index = 0; index < distinctPorts.size(); index++) {
				existingPorts = distinctPorts.get(index);
				if(otherObjFromPort < existingPorts.getFromPort()){
					existingPorts.setFromPort(otherObjFromPort);
				}else if(existingPorts.getFromPort() < otherObjFromPort && existingPorts.getToPort() > otherObjToPort){
					break;
				}
				if(existingPorts.getToPort() < otherObjToPort){
					while(index+1 < distinctPorts.size() && distinctPorts.get(index+1).getToPort() < otherObjToPort){
						distinctPorts.remove(index+1);
					}
					if(index+1 == distinctPorts.size() || distinctPorts.get(index+1).getFromPort() > otherObjToPort){
						existingPorts.setToPort(otherObjToPort);
					}else if(distinctPorts.get(index+1).getFromPort() <= otherObjToPort){
						existingPorts.setToPort(distinctPorts.get(index+1).getToPort());
						distinctPorts.remove(index+1);						
					}
				}
			}
		}
		if (otherIpPerms.getFromPort() == otherIpPerms.getToPort()) {
			if (otherIpPerms.getFromPort() == -1) {
				distinctPorts.clear();
				distinctPorts.add(new IpPortDetails(-1, -1));
			} else {

			}
		}
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public int getSubNetMask() {
		return subNetMask;
	}

	public void setSubNetMask(int subNetMask) {
		this.subNetMask = subNetMask;
	}

}
