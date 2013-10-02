package com.imaginea.serverlocator;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import com.imaginea.serverlocator.util.NetStatProcessModel;

public class NetStatSysProcessUtil {
	Set<String> listeningPorts = new HashSet<String>();
	Map<String,String> foreignSockets = new HashMap<String,String>();
	
	private void parseProcessNetStatToJSON(){
		
	}
	
	private void parseNetStatInputFromLinuxIns(String netStatInput){
		List<NetStatProcessModel> lsNetStatProcess = null;
		
	}
	public void addToJsonCommunication(String communicationPoint){
		String[] communicationDetails = communicationPoint.split(",");
		String toSkt = communicationDetails[0];
		String fromSkt = communicationDetails[1];
		String state = communicationDetails[2];
		String[] processDtls = communicationDetails[3].split("/");
		//String[] toSktArr =
		int ipAddrBound = toSkt.lastIndexOf(":");
		String toPort = toSkt.substring(ipAddrBound+1);
		String toIPAddr = toSkt.substring(0, ipAddrBound);
		
		int fromAddrBound = fromSkt.lastIndexOf(":");
		String fromPort = fromSkt.substring(fromAddrBound+1);
		String fromIPAddr = fromSkt.substring(0, fromAddrBound);
		
		String processId = processDtls[0];
		String processName = processDtls[1];
		if(!isInternalCommunication(fromIPAddr, toIPAddr,fromPort,toPort,state)){
			JSONObject jsonCommunication = new JSONObject();
			listeningPorts.add(toPort);
			communicatingPorts.put(fromIPAddr,fromPort);
			try {
				jsonCommunication.put("ToIpAddr", toIPAddr);
				jsonCommunication.put("ToPort", toPort);
				jsonCommunication.put("FromIpAddr", fromIPAddr);
				jsonCommunication.put("FromPort", fromPort);
				jsonCommunication.put("ProcessId", processId);
				jsonCommunication.put("ProcessName", processName);
				jsonCommunication.put("ConnectionState", state);				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			try {
				jsonRootEle.accumulate("CommunicationElement", jsonCommunication);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		/*for(int k=0; k < communicationDetails.length; k++){
			String toCommSkt = communicationDetails[k];
			String fromIp = addrArgs[i+2];
			String toPort = (addrArgs[i+1]);
			String fromPort = (addrArgs[i+3]);
		}*/
	}

	public void getJsonFromAddrSplitOn(String addrArgsSplit){
		String[] communicationPointArr = addrArgsSplit.split(";");
		for(String communicationPoint : communicationPointArr)
			if(!communicationPoint.equals(""))
				addToJsonCommunication(communicationPoint);
		Set<Map.Entry<String, String>> entrySetOfComm = communicatingPorts.entrySet();
		for(Map.Entry<String, String> communicationEntry: entrySetOfComm){
			if(!listeningPorts.contains(communicationEntry.getValue())){
				JSONObject externalCommDtlsToVerify = new JSONObject();				
				try {
					externalCommDtlsToVerify.put("ServerIPAddr", communicationEntry.getKey());
					externalCommDtlsToVerify.put("ServerPort", communicationEntry.getValue());
					jsonRootEle.accumulate("ExternalServer", externalCommDtlsToVerify);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		for(String listeningPort: listeningPorts){
			try{
				if(Integer.parseInt(listeningPort) > 1024){
					JSONObject internalServerPort = new JSONObject();
					JSONObject internalCommDtlsToVerify = new JSONObject();				
					try {
						internalCommDtlsToVerify.put("ServerPort", listeningPort);
						jsonRootEle.accumulate("InternalServer", internalCommDtlsToVerify);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}catch(Exception e){
				
			}
						
		}
			
		
	}
	
	
	
}
