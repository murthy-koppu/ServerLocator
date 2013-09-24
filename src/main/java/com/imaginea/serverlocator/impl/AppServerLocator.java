package com.imaginea.serverlocator.impl;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;

import com.imaginea.serverlocator.util.ApplicationConstants;

public class AppServerLocator implements ServerLocator,ApplicationConstants{
	static Logger log = Logger.getLogger(AppServerLocator.class);
	
	@Override
	public ServerProperties parseToServerProp(InetAddress iNetAddr, int portNo) {
		log.debug("********** Entered into ApacheServerLocator --> parseToServerProp() ********");
		String urlAddress = "http://"+iNetAddr.getHostAddress()+":"+portNo+"/"+((((Double)Math.random()).byteValue()) | ((Double)Math.random()).intValue());
		URL url = null;
		URLConnection connection = null;
		try {
			url = new URL(urlAddress);
			connection = url.openConnection();
			//connection.setConnectTimeout(10000);
			connection.setDoOutput(true);
			String serverDetails = connection.getHeaderField(WEB_SERVER_HEADER_NAME);
			if(serverDetails != null && !serverDetails.isEmpty()){
				ServerProperties serverProp = new ServerProperties();
				serverProp.setServerName(serverDetails.trim());
				return serverProp;
			}
		} catch (MalformedURLException e) {
			log.error("Invalid server Address details "+e);
			return null;
		} catch (IOException e) {
			log.error("Unable to connect to server "+e);
			return null;
		}finally{
			try{
				((HttpURLConnection)connection).disconnect();
			}catch(Exception e){
				log.error("Unable to close Url connection",e);
			}
		}
		log.debug("Current execution Server details failed for Apache Test");
		return null;
	}
	

}
