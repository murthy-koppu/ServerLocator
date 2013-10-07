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
import com.imaginea.serverlocator.util.IpPermsSGModel;
import com.imaginea.serverlocator.util.LoadAWSConfigUtility;

public class InstanceUtil {
	
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
	
	public Map<String,SecurityGroup> loadSecurityGroups(){
		DescribeSecurityGroupsResult securityGroupRslt =  LoadAWSConfigUtility.getAmazonEC2().describeSecurityGroups();
		if(securityGroupRslt != null && securityGroupRslt.getSecurityGroups() != null && !securityGroupRslt.getSecurityGroups().isEmpty()){
			Map<String,SecurityGroup> mapSGroupsWithId = new HashMap<String, SecurityGroup>();
			for(SecurityGroup sGroup: securityGroupRslt.getSecurityGroups()){
				mapSGroupsWithId.put(sGroup.getGroupId(), sGroup);
			}
		}
		return null;
	}
	
	public List<IpPermsSGModel> getPermissibleIpPermsForInstance(Instance instance){
		List<IpPermsSGModel> permissibleIPPermsSGModels = new ArrayList<IpPermsSGModel>();
		List<GroupIdentifier> instanceSGroupIdentifiers = instance.getSecurityGroups();
		
		return null;
	}
}
