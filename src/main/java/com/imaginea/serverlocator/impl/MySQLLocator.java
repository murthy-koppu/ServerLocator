package com.imaginea.serverlocator.impl;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;

import org.apache.log4j.Logger;

import com.imaginea.serverlocator.util.ApplicationConstants;
import com.imaginea.serverlocator.util.PacketBuffer;
import com.imaginea.serverlocator.util.ServersEnum;
import com.imaginea.serverlocator.util.Utils;

public class MySQLLocator implements ServerLocator,ApplicationConstants{

	PacketBuffer bufferPacket = null;
	static Logger log = Logger.getLogger(MySQLLocator.class);
	private int connectionTimeOut = DB_SERVER_TIME_OUT_PERIOD;
	
	@Override
	public ServerProperties parseToServerProp(InetAddress iNetAddr, int port, boolean isLimitedTimeOut) {
			log.debug("********** Entered into MySQLLocator --> parseToServerProp() ********");
			try {
				DataInputStream dInStream = null;
				try{
					dInStream = Utils.getDataInStreamFromServer(iNetAddr, port,connectionTimeOut,isLimitedTimeOut);
				}catch(IOException e){
					log.error("Unable to get DataInputStream",e);
					return null;
				}				
				bufferPacket = getDataPacket(dInStream);
				dInStream.close();
				if(bufferPacket == null)
					return null;				
				bufferPacket.skipByte(1);
				//TODO Need to replicate issue
				byte protocolVersion = bufferPacket.readByte();
				if(protocolVersion == -1){
					log.error("Parsed data identifies error result");
					return null;						 
				}
				String version = bufferPacket.readString();
				if(!isValidVersion(version)){
					log.error("Version data from DataStream validation failed");
					return null;
				}
							
				ServerProperties serverProp = new ServerProperties();
				serverProp.setServerName(ServersEnum.MY_SQL.toString());
				serverProp.setVersion(version);
				
				return serverProp;
			} catch (Exception e) {
				log.error("Unable to parse socket output as MySQL response"+e);
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
		log.debug("********** Entered into MySQLLocator --> getDataPacket() ********");
		byte[] packetLengthInBytes = new byte[3];
		int noOfLengthBytes = dInStream.read(packetLengthInBytes, 0, 3);
		if(PacketBuffer.validateInt(packetLengthInBytes) || noOfLengthBytes < 3){
			log.error("Unable to parse socket output as MySQL response");
			return null;
		}			
		
		int packetLength = (packetLengthInBytes[0] & 0xff) +
		((packetLengthInBytes[1] & 0xff) << 8) +
		((packetLengthInBytes[2] & 0xff) << 16);
		byte[] bytePacket = new byte[packetLength+1];
		log.debug("Packet data read from stream is "+ packetLength);
		int noOfPacktetBytes = dInStream.read(bytePacket, 0, packetLength);
		log.debug("Byte data from DataStream is "+bytePacket);
		if(noOfPacktetBytes != packetLength){
			log.error("Unable to parse socket output as MySQL response");
			return null;
		}			
		return  new PacketBuffer(bytePacket);
	}
	
}
