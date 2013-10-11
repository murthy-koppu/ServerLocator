package com.imaginea.serverlocator;

import javax.swing.JOptionPane;

import org.json.JSONException;
import org.json.JSONObject;

import com.imaginea.serverlocator.util.AWSInstanceKeyPairLoader;
import com.imaginea.serverlocator.util.ApplicationConstants;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class RemoteSshProcessor implements Runnable,ApplicationConstants{

	private ConcreteTopologyPublisher parentTopologyPublisher;
	private JSONObject instanceJsonProperties;
	
	@Override
	public void run() {
		 JSch jsch=new JSch();
		 try {
			jsch.addIdentity("");
			 String user="ec2-user";
		     String host="ec2-50-19-178-220.compute-1.amazonaws.com";
		     Session session=jsch.getSession(user, host, 22);
		} catch (JSchException e) {
			e.printStackTrace();
		}
		

		
	}

}
