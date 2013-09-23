package com.imaginea.serverlocator.impl;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;

import com.imaginea.serverlocator.factory.ServersEnum;
import com.imaginea.serverlocator.util.PacketBuffer;
import com.imaginea.serverlocator.util.Utils;

public class MySQLLocator implements ServerLocator{

	PacketBuffer bufferPacket = null;
	
	//@Override
	public ServerProperties parseToServerProp(InetAddress iNetAddr, int port) {
			try {
				DataInputStream dInStream = Utils.getDataInStreamFromServer(iNetAddr, port);
				bufferPacket = getDataPacket(dInStream);
				if(bufferPacket == null)
					return null;				
				bufferPacket.skipByte(1);
				//TODO Need to replicate issue
				byte protocolVersion = bufferPacket.readByte();
				if(protocolVersion == -1)
					return null;						 
				
				String version = bufferPacket.readString();
				if(!isValidVersion(version))
					return null;
				
				ServerProperties serverProp = new ServerProperties();
				serverProp.setHostName(iNetAddr.getHostName());
				serverProp.setPortNo(port);
				serverProp.setServerName(ServersEnum.MY_SQL.toString());
				serverProp.setVersion(version);
				
				return serverProp;
			} catch (Exception e) {
				return null;
			}	
	}
	
		
	private boolean isValidVersion(String version){
		byte[] versionBytes = version.getBytes();
		for(byte versionByte: versionBytes){
			if((!(versionByte > 47 && versionByte < 58) && versionByte != 46))
				return false;
		}
		return true;
	}
	
	private PacketBuffer getDataPacket(DataInputStream dInStream) throws IOException {
		byte[] packetLengthInBytes = new byte[3];
		int noOfLengthBytes = dInStream.read(packetLengthInBytes, 0, 3);
		if(PacketBuffer.validateInt(packetLengthInBytes) || noOfLengthBytes < 3)
			return null;
		
		int packetLength = (packetLengthInBytes[0] & 0xff) +
		((packetLengthInBytes[1] & 0xff) << 8) +
		((packetLengthInBytes[2] & 0xff) << 16);
		byte[] bytePacket = new byte[packetLength+1];
		int noOfPacktetBytes = dInStream.read(bytePacket, 0, packetLength);
		if(noOfPacktetBytes != packetLength)
			return null;
		return  new PacketBuffer(bytePacket);
	}
	
}
