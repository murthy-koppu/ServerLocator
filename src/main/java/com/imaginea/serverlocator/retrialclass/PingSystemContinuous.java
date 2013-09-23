package com.imaginea.serverlocator.retrialclass;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;


public class PingSystemContinuous {

	
	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket client = new Socket("localhost", 3306);
		 client.setKeepAlive(true);
		System.out.println("Inet address is "+client.getInetAddress());
		InputStream s1In = client.getInputStream();
		
		 DataInputStream dis = new DataInputStream(s1In);
		 Byte byteMsg = null;
		  long l =0;
		 while(l < 77){
			 if((byteMsg = dis.readByte()) != null)
				 System.out.print((char)(byteMsg.byteValue()));
			 l++;
		 }
		 SocketAddress serverAddr = client.getRemoteSocketAddress();
		 //client.bind(serverAddr);
		 DataOutputStream dos = new DataOutputStream(client.getOutputStream());
		 
		 dos.writeChars("Hello");
		 dos.flush();
		 while(true){
			 try{
				 if((byteMsg = dis.readByte()) != null)
					 System.out.print((char)(byteMsg.byteValue()));
				 			 
				 
			 }catch(Exception e){
				// e.printStackTrace();
				 break;
			 }
			 
		 }
	}
}
