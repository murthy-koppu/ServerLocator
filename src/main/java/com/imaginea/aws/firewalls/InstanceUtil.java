package com.imaginea.aws.firewalls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.DescribeSecurityGroupsResult;
import com.amazonaws.services.ec2.model.GroupIdentifier;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.SecurityGroup;
import com.imaginea.serverlocator.util.IpPermsUtil;
import com.imaginea.serverlocator.util.LoadAWSConfigUtility;

public class InstanceUtil {
	Map<String,SecurityGroup> mapSGroupsWithId = new HashMap<String, SecurityGroup>();
	public List<Instance> getEC2Instances(){
		List<Instance> lsInstances = new ArrayList<Instance>();
		DescribeInstancesResult instancesResult = LoadAWSConfigUtility.getAmazonEC2().describeInstances();
		if(instancesResult != null && instancesResult.getReservations() != null){
			for(Reservation reservation: instancesResult.getReservations()){
				lsInstances.addAll(reservation.getInstances());
			}
		}
		return lsInstances;
	}
	
	public void loadSecurityGroups(){
		DescribeSecurityGroupsResult securityGroupRslt =  LoadAWSConfigUtility.getAmazonEC2().describeSecurityGroups();
		if(securityGroupRslt != null && securityGroupRslt.getSecurityGroups() != null && !securityGroupRslt.getSecurityGroups().isEmpty()){			
			for(SecurityGroup sGroup: securityGroupRslt.getSecurityGroups()){
				mapSGroupsWithId.put(sGroup.getGroupId(), sGroup);
			}
		}
	}
	
	
	
	public List<IpPermsUtil> getPermissibleIpPermsForInstance(Instance instance){
		List<IpPermsUtil> permissibleIPPermsSGModels = new ArrayList<IpPermsUtil>();
		List<GroupIdentifier> instanceSGroupIdentifiers = instance.getSecurityGroups();
		
		for(GroupIdentifier sGroupIdentifier: instanceSGroupIdentifiers){
			SecurityGroup instSGroup = mapSGroupsWithId.get(sGroupIdentifier.getGroupId());
			
		}
		
		return null;
	}
}
