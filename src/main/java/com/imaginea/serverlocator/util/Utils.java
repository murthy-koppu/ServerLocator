package com.imaginea.serverlocator.util;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;


public class Utils {

	public static DataInputStream getDataInStreamFromServer(Socket socket) throws IOException{
		if(socket == null){
			throw new IOException("Invalid socket passed to connect to server");
		}
		InputStream inStream = socket.getInputStream();
		if(inStream != null)
			throw new IOException("Unable to get InputStream from socket");
		DataInputStream dInStream = new DataInputStream(inStream);
		return dInStream;
	}
	
	public static DataInputStream getDataInStreamFromServer(InetAddress iNetAddr, int port ) throws IOException{
		Socket clientSocket = new Socket(iNetAddr,port);
		return getDataInStreamFromServer(clientSocket);
	}

	
}
