package com.imaginea.serverlocator.util;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;

import org.apache.log4j.Logger;


public class Utils {

	static Logger log = Logger.getLogger(Utils.class);
	public static DataInputStream getDataInStreamFromServer(Socket socket) throws IOException{
		InputStream inStream = socket.getInputStream();
		if(inStream == null){
			log.error("Unable to get InputStream from socket");
			throw new IOException("Unable to get InputStream from socket");
		}
		DataInputStream dInStream = new DataInputStream(inStream);
		return dInStream;
	}
	
	public static DataInputStream getDataInStreamFromServer(InetAddress iNetAddr, int port ) throws IOException{
		Socket clientSocket = new Socket(iNetAddr,port);
		return getDataInStreamFromServer(clientSocket);
	}

	
}
