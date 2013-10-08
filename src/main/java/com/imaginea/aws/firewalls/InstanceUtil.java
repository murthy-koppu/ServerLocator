package com.imaginea.aws.firewalls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.DescribeSecurityGroupsResult;
import com.amazonaws.services.ec2.model.GroupIdentifier;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceState;
import com.amazonaws.services.ec2.model.InstanceStateName;
import com.amazonaws.services.ec2.model.IpPermission;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.SecurityGroup;
import com.imaginea.serverlocator.util.OptimizedIpPerms;
import com.imaginea.serverlocator.util.LoadAWSConfigUtility;
import com.imaginea.serverlocator.util.PermissibleSocketModel;

public class InstanceUtil {
	Map<String, SecurityGroup> mapSGroupsWithId = new HashMap<String, SecurityGroup>();
	Map<Instance, OptimizedIpPerms> ipPermsToEachInstance = new HashMap<Instance, OptimizedIpPerms>();
	List<Instance> lsInstances = new ArrayList<Instance>();
	static final int START_POINT_ACCESS_PORT = 80;

	public static void main(String[] args) {
		InstanceUtil obj = new InstanceUtil();
		obj.getEC2Instances();
		System.out.println(obj.findStartPointInstances());
		obj.publishInstanceConnectionsToJson();
	}

	public void publishInstanceConnectionsToJson() {
		JSONObject rootInstanceRel = new JSONObject();
		Instance instance = null;
		for (int k = 0; k < lsInstances.size(); k++) {
			instance = lsInstances.get(k);
			JSONObject jsonInstances = new JSONObject();
			try {
				jsonInstances.put("name", instance.getPrivateIpAddress());
				jsonInstances.put("serialNo", k);
				jsonInstances.put("instanceId", instance.getInstanceId());
				rootInstanceRel.accumulate("nodes", jsonInstances);
				OptimizedIpPerms ipPermDtls = ipPermsToEachInstance
						.get(instance);
				for (int t = 0; t < lsInstances.size(); t++) {
					if (t == k) {
						continue;
					}
					Instance otherInstance = lsInstances.get(t);
					if (canTalkOn(ipPermDtls, otherInstance)) {
						JSONObject linkJson = new JSONObject();
						linkJson.put("source", k);
						linkJson.put("target", t);
						linkJson.put("relationship", "rel");
						rootInstanceRel.accumulate("links", linkJson);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		System.out.println(rootInstanceRel.toString());
	}

	public void getEC2Instances() {
		loadSecurityGroups();
		DescribeInstancesResult instancesResult = LoadAWSConfigUtility
				.getAmazonEC2().describeInstances();

		if (instancesResult != null
				&& instancesResult.getReservations() != null) {
			for (Reservation reservation : instancesResult.getReservations()) {
				lsInstances.addAll(reservation.getInstances());
			}
		}
		for (Instance instance : lsInstances) {
			ipPermsToEachInstance.put(instance,
					getPermissibleIpPermsForInstance(instance));
		}
	}

	public void loadSecurityGroups() {
		DescribeSecurityGroupsResult securityGroupRslt = LoadAWSConfigUtility
				.getAmazonEC2().describeSecurityGroups();
		if (securityGroupRslt != null
				&& securityGroupRslt.getSecurityGroups() != null
				&& !securityGroupRslt.getSecurityGroups().isEmpty()) {
			for (SecurityGroup sGroup : securityGroupRslt.getSecurityGroups()) {
				mapSGroupsWithId.put(sGroup.getGroupId(), sGroup);
			}
		}
	}

	public OptimizedIpPerms getPermissibleIpPermsForInstance(Instance instance) {
		List<GroupIdentifier> instanceSGroupIdentifiers = instance
				.getSecurityGroups();
		OptimizedIpPerms instOptimizedIpPerms = new OptimizedIpPerms();
		for (GroupIdentifier sGroupIdentifier : instanceSGroupIdentifiers) {
			SecurityGroup instSGroup = mapSGroupsWithId.get(sGroupIdentifier
					.getGroupId());
			List<IpPermission> sGroupIpPerms = instSGroup.getIpPermissions();
			for (int k = 0; k < sGroupIpPerms.size(); k++) {
				instOptimizedIpPerms.merge(sGroupIpPerms.get(k));
			}
		}
		return instOptimizedIpPerms;
	}

	public List<Instance> findStartPointInstances() {
		return findInstancesWithPort(START_POINT_ACCESS_PORT);
	}

	public List<Instance> findInstancesWithPort(int port) {
		Set<Map.Entry<Instance, OptimizedIpPerms>> instanceWithIpPerms = ipPermsToEachInstance
				.entrySet();
		List<Instance> instancesOpenToPort = new ArrayList<Instance>();
		for (Map.Entry<Instance, OptimizedIpPerms> instanceIpPermsEntry : instanceWithIpPerms) {
			OptimizedIpPerms instanceIpPerms = instanceIpPermsEntry.getValue();
			List<PermissibleSocketModel> lsSkts = instanceIpPerms
					.getPermissibleSktsToProtocol().get("tcp");
			if (lsSkts != null && !lsSkts.isEmpty()) {
				for (PermissibleSocketModel permissibleSkt : lsSkts) {
					if (permissibleSkt.isAllIps()
							&& permissibleSkt.isAllowedPort(port)) {
						instancesOpenToPort.add(instanceIpPermsEntry.getKey());
						break;
					}
				}
			}
		}
		return instancesOpenToPort;
	}

	private boolean canTalkOn(OptimizedIpPerms ipPermDtls, Instance instance) {
		List<PermissibleSocketModel> lsSkts = ipPermDtls
				.getPermissibleSktsToProtocol().get("tcp");
		if (lsSkts != null && !lsSkts.isEmpty()) {
			for (PermissibleSocketModel permissibleSkt : lsSkts) {
				if (permissibleSkt.isAllIps()) {
					return true;
				}
			}
		}
		for (GroupIdentifier sGroupIdentifier : instance.getSecurityGroups()) {
			String sGroupId = sGroupIdentifier.getGroupId();
			if (ipPermDtls.getAssociatedSGroupIds().contains(sGroupId)) {
				return true;
			}
		}
		return false;
	}

}
