package com.imaginea.serverlocator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import com.imaginea.serverlocator.factory.ServerLocator;
import com.imaginea.serverlocator.util.ApplicationConstants;
import com.imaginea.serverlocator.util.NetStatProcessModel;
import com.imaginea.serverlocator.util.SocketModel;

public class NetStatSysProcessUtil implements ApplicationConstants {
	Set<Integer> listeningPorts = new HashSet<Integer>();
	Map<String, String> foreignSockets = new HashMap<String, String>();
	Set<NetStatProcessModel> communicatingProcess = new HashSet<NetStatProcessModel>();
	
	public static void main(String[] args) throws Exception {
		//String sampleData ="0.0.0.0:22,0.0.0.0:*,LISTEN,1108/sshd;127.0.0.1:25,0.0.0.0:*,LISTEN,1141/sendmail;172.16.12.236:22,182.73.145.150:32937,ESTABLISHED,20872/sshd;172.16.12.236:22,182.73.145.150:33142,ESTABLISHED,20917/sshd;:::9005,:::*,LISTEN,21227/java;:::22,:::*,LISTEN,1108/sshd;::ffff:172.16.12.236:9005,::ffff:10.179.9.248:39442,CLOSE_WAIT,21227/java;::ffff:172.16.12.236:9005,::ffff:10.179.9.248:39444,ESTABLISHED,21227/java";
		new NetStatSysProcessUtil().processNetStatRecs(args[0]);
	}
	
	
	private void processNetStatRecs(String netStatIn) throws Exception {
		NetStatLinuxParser linuxParser = new NetStatLinuxParser();
		List<NetStatProcessModel> netStatRecords = linuxParser
				.getNetStatRecords(netStatIn);
		for (NetStatProcessModel netStatRec : netStatRecords) {
			String communicationState = netStatRec.getState();
			if (communicationState.equals(NET_STAT_PROCESS_COMMUNICATION_STATE)) {
				listeningPorts.add(netStatRec.getLocalSkt().getPort());
			} else {
				communicatingProcess.add(netStatRec);
			}			
		}		
		parseNetStatRecsToJson(netStatRecords);
		
	}

	private void parseNetStatRecsToJson(List<NetStatProcessModel> netStatRecords) {
		JSONObject rootNetStatJson = new JSONObject();
		for (NetStatProcessModel netStatRec : communicatingProcess) {
			JSONObject jsonNetStatRec = new JSONObject();
			if (listeningPorts.contains(netStatRec.getLocalSkt().getPort())) {
				setToAndFromSktOnJson(jsonNetStatRec, netStatRec.getLocalSkt(),
						netStatRec.getForeignSkt());
			} else {
				setToAndFromSktOnJson(jsonNetStatRec,
						netStatRec.getForeignSkt(), netStatRec.getLocalSkt());
				//TODO Handle duplication of Foreign Server Addr
				JSONObject jsonForeignServer = new JSONObject();
				try{
					jsonForeignServer.put("ForeignServerIPAddr",  netStatRec.getForeignSkt().getIpAddress());
					jsonForeignServer.put("ForeignServerPort",  netStatRec.getForeignSkt().getPort());
					rootNetStatJson.accumulate("ForeignServers", jsonForeignServer);
				}catch(JSONException e){
					e.printStackTrace();
				}
			}
			try {
				jsonNetStatRec.put("ProcessId", netStatRec.getProcessId());
				jsonNetStatRec.put("ProcessName", netStatRec.getProcessName());
				jsonNetStatRec.put("ConnectionState", netStatRec.getState());
				rootNetStatJson.accumulate("CommunicatingProcess", jsonNetStatRec);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		for(Integer localServerPort: listeningPorts){
			JSONObject jsonInternalServer = new JSONObject();
			try {
				jsonInternalServer.put("LocalServerPort", localServerPort);
				rootNetStatJson.accumulate("LocalServers", jsonInternalServer);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println(rootNetStatJson.toString());
		new ServerLocator().findServerDetailsOnNetStats(rootNetStatJson);
	}

	private void setToAndFromSktOnJson(JSONObject jsonNetStatRec,
			SocketModel toSktMdl, SocketModel fromSktMdl) {
		try {
			jsonNetStatRec.put("ToIpAddr", toSktMdl.getIpAddress());
			jsonNetStatRec.put("ToPort", toSktMdl.getPort());
			jsonNetStatRec.put("FromIpAddr", fromSktMdl.getIpAddress());
			jsonNetStatRec.put("FromPort", fromSktMdl.getPort());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
}
