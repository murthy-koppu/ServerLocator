package com.imaginea.servertracker;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

public class ServerLocator {

	public static void findServerConfig(String ipAddress, int port){
		try {
			URL ipUrl = new URL(ipAddress);
			
			try {
				//Utils.
				Socket clientSocket = new Socket(InetAddress.getByName("ipAddress"), port);
				InputStream inStream = clientSocket.getInputStream();
				DataInputStream dInStream = new DataInputStream(inStream);
				
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
}
