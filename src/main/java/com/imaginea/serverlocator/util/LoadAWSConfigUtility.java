package com.imaginea.serverlocator.util;

import java.io.InputStream;
import java.util.Properties;

import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.RegionUtils;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagement;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClient;

public class LoadAWSConfigUtility {
	private static Properties properties = new Properties();
	private static AmazonEC2 amazonEC2 = new AmazonEC2Client(
			new ClasspathPropertiesFileCredentialsProvider());
	private static AmazonIdentityManagement amazonIM = new AmazonIdentityManagementClient(
			new ClasspathPropertiesFileCredentialsProvider());
	private static Region region;
	private static String accountId;

	static {
		try {
			InputStream propFileInputStream = LoadAWSConfigUtility.class
					.getClassLoader().getResourceAsStream("config.properties");
			properties.load(propFileInputStream);
			region = RegionUtils.getRegion(properties
					.getProperty("request.region"));
			amazonEC2.setRegion(region);
			String awsArn = amazonIM.getUser().getUser().getArn();
			int accountIdStartPos = awsArn.indexOf("::") + 2;
			accountId = awsArn.substring(accountIdStartPos,
					awsArn.indexOf(":user/", accountIdStartPos));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Properties getProperties() {
		return properties;
	}

	public static AmazonEC2 getAmazonEC2() {
		return amazonEC2;
	}

	public static Region getRegion() {
		return region;
	}

	public static String getAccountId() {
		return accountId;
	}

}
