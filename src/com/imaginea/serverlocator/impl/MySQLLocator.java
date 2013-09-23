package com.imaginea.serverlocator.impl;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;

import com.imaginea.serverlocator.util.Utils;

public class MySQLLocator implements ServerLocator{

	int packetPosition = -1;
	int packetLen = -1;
	private int readPktPos() throws Exception{
		if(packetPosition >= packetLen)
			throw new Exception();
		return packetPosition++;
	}
	
	@Override
	public ServerProperties parseToServerProp(InetAddress iNetAddr, int port) {
			try {
				DataInputStream dInStream = Utils.getDataInStreamFromServer(iNetAddr, port);
				byte[] packet = getPacketLength(dInStream);
				if(packet == null)
					return null;
				// First Byte of packet refers to protocol version
				if(packet[readPktPos()] == -1)
					return null;			
				
				String version = readString(packet, readPktPos());										
				
			} catch (Exception e) {
				return null;
			}	
		
		return null;
	}
	
	private byte[] getPacketLength(DataInputStream dInStream) throws IOException {
		byte[] packetLengthInBytes = new byte[4];
		int noOfLengthBytes = dInStream.read(packetLengthInBytes, 0, 3);				
		if(noOfLengthBytes < 4)
			return null;
		int packetLength = (packetLengthInBytes[0] & 0xff) +
		((packetLengthInBytes[1] & 0xff) << 8) +
		((packetLengthInBytes[2] & 0xff) << 16);
		this.packetLen = packetLength;
		byte[] packet = new byte[packetLength+1];
		int noOfPacktetBytes = dInStream.read(packet, 0, packetLength);
		if(noOfPacktetBytes != packetLength)
			return null;
		packet[packetLength] = 0;
		this.packetPosition = 0;
		return packet;
	}
	
	
	private String readString(byte[] packet, int off) throws Exception{
		String data = "";
		byte byteData;
		while((byteData = packet[readPktPos()]) != 0)
			data += byteData;
		
		return data;
	}
	
}
