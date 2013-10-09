package com.imaginea.serverlocator.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.amazonaws.auth.AWSCredentials;

public class AWSCredentialsLoader implements AWSCredentials{
	private static Properties awsCredentialProperties = new Properties();
	static{
		 FileInputStream file;
		try {
			file = new FileInputStream("./AwsCredentials.properties");
			awsCredentialProperties.load(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		 
	}
	
	@Override
	public String getAWSAccessKeyId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAWSSecretKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
