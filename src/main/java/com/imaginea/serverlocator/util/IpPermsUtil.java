package com.imaginea.serverlocator.util;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.amazonaws.services.ec2.model.IpPermission;

public class IpPermsUtil {
	//private String ipAddress = "";
	/*
	 * private int fromPort = -1; private int toPort = -1;
	 */
	private Map<String, List<PermissibleSocketModel>> permissibleSktsToProtocol = new HashMap<String, List<PermissibleSocketModel>>();
	
	private String protocol;
	//private int subNetMask = 0;
	List<PermissibleSocketModel> permissibleSkts = new ArrayList<PermissibleSocketModel>();

	private void merge(IpPermission inIpPermission) {
		List<PermissibleSocketModel> permissibleSockets = permissibleSktsToProtocol.get(inIpPermission.getIpProtocol());
		boolean isMerged = false;
		if(permissibleSockets != null){
			for(int k=0; k  < permissibleSockets.size() && !isMerged; k++){
				isMerged = mergeOnIPAddress(permissibleSockets.get(k), inIpPermission);		
			}
		}else{
			permissibleSockets = new ArrayList<PermissibleSocketModel>();
		}
		if(permissibleSockets == null || isMerged){			
			permissibleSockets.add(new PermissibleSocketModel(inIpPermission));
		}
	}
	
	private boolean mergeOnIPAddress(PermissibleSocketModel existingSkt, IpPermission newIpPerms) {
		String[] newIpParts = newIpPerms.getIpRanges().get(0).split("/");
		int newSubNetMask = Integer.parseInt(newIpParts[1]);
		int subnetMaskDelta = existingSkt.getSubNetMask() - newSubNetMask;
		if (subnetMaskDelta == 0) {
			if (existingSkt.getIpAddress().equals(newIpParts[0])) {
				return true;
			}
			return false;
		} else {
			String[] newIpAddrCIDRParts = newIpParts[0].split(".");
			String[] existingIpAddrCIDRParts = existingSkt.getIpAddress().split(".");
			int minimumMask = subnetMaskDelta < 0 ? existingSkt.getSubNetMask() : newSubNetMask;
			for (int k = 0; k < 4 && minimumMask > 0; k++, minimumMask -= 8) {
				int newIpCIDRPart = Integer
						.parseInt(newIpAddrCIDRParts[k]);
				int existingIpCIDRPart = Integer.parseInt(existingIpAddrCIDRParts[k]);
				int subNetMaskResult = newIpCIDRPart
						& existingIpCIDRPart;
				if (subnetMaskDelta < 0) {
					if (subNetMaskResult != existingIpCIDRPart)
						return false;
				} else {
					if (subNetMaskResult != newIpCIDRPart)
						return false;
				}
			}
			existingSkt.setIpAddress(subnetMaskDelta < 0 ? existingSkt.getIpAddress() : newIpParts[0]);
			existingSkt.setSubNetMask(minimumMask);
			mergeOnPorts(newIpPerms);
			return true;
		}		
	}

/*	private boolean mergeOnIPAddress(IpPermsUtil otherIpPerms) {
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
*/
	private void mergeOnPorts(IpPermission otherIpPerms) {
		int otherObjFromPort = otherIpPerms.getFromPort();
		int otherObjToPort = otherIpPerms.getToPort();
		PermissibleSocketModel otherObjIpPort = new PermissibleSocketModel(otherIpPerms);

		if (permissibleSkts.isEmpty() == true) {
			permissibleSkts.add(otherObjIpPort);
		}else if (otherObjFromPort == -1) {
			permissibleSkts.clear();
			permissibleSkts.add(otherObjIpPort);
			return;
		}else {
			PermissibleSocketModel existingPorts = null;
			existingPorts = permissibleSkts.get(0);
			if (existingPorts.isAllPorts()) {
				return;
			}
			for (int index = 0; index < permissibleSkts.size(); index++) {
				existingPorts = permissibleSkts.get(index);
				if(otherObjFromPort < existingPorts.getFromPort()){
					existingPorts.setFromPort(otherObjFromPort);
				}else if(existingPorts.getFromPort() < otherObjFromPort && existingPorts.getToPort() > otherObjToPort){
					break;
				}
				if(existingPorts.getToPort() < otherObjToPort){
					while(index+1 < permissibleSkts.size() && permissibleSkts.get(index+1).getToPort() < otherObjToPort){
						permissibleSkts.remove(index+1);
					}
					if(index+1 == permissibleSkts.size() || permissibleSkts.get(index+1).getFromPort() > otherObjToPort){
						existingPorts.setToPort(otherObjToPort);
					}else if(permissibleSkts.get(index+1).getFromPort() <= otherObjToPort){
						existingPorts.setToPort(permissibleSkts.get(index+1).getToPort());
						permissibleSkts.remove(index+1);						
					}
				}
			}
		}
		if (otherIpPerms.getFromPort() == otherIpPerms.getToPort()) {
			if (otherIpPerms.getFromPort() == -1) {
				permissibleSkts.clear();
				//ipAndPortPerms.add(new IpPortDetails(-1, -1));
			} else {

			}
		}
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

}
