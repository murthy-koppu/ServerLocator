package com.imaginea.serverlocator;

import java.util.ArrayList;
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
import com.imaginea.serverlocator.util.Utils;

public class NetStatSysProcessUtil implements ApplicationConstants {
	Set<Integer> listeningPorts = new HashSet<Integer>();
	Map<String, String> foreignSockets = new HashMap<String, String>();
	Set<NetStatProcessModel> communicatingProcess = new HashSet<NetStatProcessModel>();

	public static void main(String[] args) throws Exception {
		String serverSampleData = "0.0.0.0:22,0.0.0.0:*,LISTEN,1107/sshd;127.0.0.1:25,0.0.0.0:*,LISTEN,1140/sendmail;10.243.123.119:22,14.141.6.170:33148,ESTABLISHED,3234/sshd;10.243.123.119:22,14.141.6.170:32832,ESTABLISHED,3207/sshd;:::9005,:::*,LISTEN,1241/java;:::22,:::*,LISTEN,1107/sshd;::ffff:127.0.0.1:9005,::ffff:127.0.0.1:36308,CLOSE_WAIT,1241/java;::ffff:127.0.0.1:9005,::ffff:127.0.0.1:36275,CLOSE_WAIT,1241/java;::ffff:127.0.0.1:9005,::ffff:127.0.0.1:36331,CLOSE_WAIT,1241/java;::ffff:127.0.0.1:9005,::ffff:127.0.0.1:36243,CLOSE_WAIT,1241/java;::ffff:127.0.0.1:9005,::ffff:127.0.0.1:36289,CLOSE_WAIT,1241/java;::ffff:127.0.0.1:9005,::ffff:127.0.0.1:36307,CLOSE_WAIT,1241/java;::ffff:127.0.0.1:9005,::ffff:127.0.0.1:36276,CLOSE_WAIT,1241/java;::ffff:127.0.0.1:9005,::ffff:127.0.0.1:36274,CLOSE_WAIT,1241/java;::ffff:127.0.0.1:9005,::ffff:127.0.0.1:36260,CLOSE_WAIT,1241/java;::ffff:127.0.0.1:9005,::ffff:127.0.0.1:36244,CLOSE_WAIT,1241/java;::ffff:127.0.0.1:9005,::ffff:127.0.0.1:36309,CLOSE_WAIT,1241/java;::ffff:127.0.0.1:9005,::ffff:127.0.0.1:36335,CLOSE_WAIT,1241/java;::ffff:127.0.0.1:9005,::ffff:127.0.0.1:36261,CLOSE_WAIT,1241/java;::ffff:127.0.0.1:9005,::ffff:127.0.0.1:36332,CLOSE_WAIT,1241/java;::ffff:127.0.0.1:9005,::ffff:127.0.0.1:36330,CLOSE_WAIT,1241/java;::ffff:127.0.0.1:9005,::ffff:127.0.0.1:36285,CLOSE_WAIT,1241/java;::ffff:127.0.0.1:9005,::ffff:127.0.0.1:36310,CLOSE_WAIT,1241/java;::ffff:127.0.0.1:9005,::ffff:127.0.0.1:36258,CLOSE_WAIT,1241/java;::ffff:127.0.0.1:9005,::ffff:127.0.0.1:36270,CLOSE_WAIT,1241/java;::ffff:127.0.0.1:9005,::ffff:127.0.0.1:36271,CLOSE_WAIT,1241/java;::ffff:127.0.0.1:9005,::ffff:127.0.0.1:36272,CLOSE_WAIT,1241/java;::ffff:127.0.0.1:9005,::ffff:127.0.0.1:36306,CLOSE_WAIT,1241/java;::ffff:127.0.0.1:9005,::ffff:127.0.0.1:36286,CLOSE_WAIT,1241/java;::ffff:127.0.0.1:9005,::ffff:127.0.0.1:36288,CLOSE_WAIT,1241/java;::ffff:10.243.123.119:9005,::ffff:10.204.97.158:60959,ESTABLISHED,1241/java;::ffff:127.0.0.1:9005,::ffff:127.0.0.1:36242,CLOSE_WAIT,1241/java;::ffff:127.0.0.1:9005,::ffff:127.0.0.1:36246,CLOSE_WAIT,1241/java;::ffff:127.0.0.1:9005,::ffff:127.0.0.1:36247,CLOSE_WAIT,1241/java;::ffff:127.0.0.1:9005,::ffff:127.0.0.1:36287,CLOSE_WAIT,1241/java;::ffff:127.0.0.1:9005,::ffff:127.0.0.1:36336,CLOSE_WAIT,1241/java;::ffff:127.0.0.1:9005,::ffff:127.0.0.1:36245,CLOSE_WAIT,1241/java;::ffff:127.0.0.1:9005,::ffff:127.0.0.1:36333,CLOSE_WAIT,1241/java;::ffff:127.0.0.1:9005,::ffff:127.0.0.1:36248,CLOSE_WAIT,1241/java;::ffff:127.0.0.1:9005,::ffff:127.0.0.1:36259,CLOSE_WAIT,1241/java;::ffff:127.0.0.1:9005,::ffff:127.0.0.1:36273,CLOSE_WAIT,1241/java;::ffff:127.0.0.1:9005,::ffff:127.0.0.1:36290,CLOSE_WAIT,1241/java;::ffff:127.0.0.1:9005,::ffff:127.0.0.1:36334,CLOSE_WAIT,1241/java;::ffff:127.0.0.1:9005,::ffff:127.0.0.1:36257,CLOSE_WAIT,1241/java;::ffff:127.0.0.1:9005,::ffff:127.0.0.1:36311,CLOSE_WAIT,1241/java;::ffff:127.0.0.1:9005,::ffff:127.0.0.1:36291,CLOSE_WAIT,1241/java;";
		String sampleData = "0.0.0.0:22,0.0.0.0:*,LISTEN,1128/sshd;127.0.0.1:25,0.0.0.0:*,LISTEN,1161/sendmail;10.204.97.158:22,182.73.145.150:33084,ESTABLISHED,4572/sshd;10.204.97.158:22,182.73.145.150:33003,ESTABLISHED,4588/sshd;:::22,:::*,LISTEN,1128/sshd;::ffff:10.204.97.158:60959,::ffff:10.243.123.119:9005,ESTABLISHED,2484/java;";
		new NetStatSysProcessUtil().processNetStatRecs(serverSampleData,
				args[1]);
	}

	private void processNetStatRecs(String netStatIn, String systemIps)
			throws Exception {
		JSONObject systemLvlRootJSON = new JSONObject();
		JSONObject rootTopologyJson = new JSONObject();
		JSONObject systemIPsJSON = new JSONObject();

		List<String> standardLocalIps = getStandardSystemIP(systemIps);
		if (standardLocalIps.isEmpty()) {
			throw new Exception("Unable to find Machine IP Address");
		} else {
			for (String systemIP : standardLocalIps) {
				systemIPsJSON.accumulate("PrivateInetAddresses", systemIP);
			}
			systemLvlRootJSON.put("SystemInetAddrs", systemIPsJSON);
		}

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
		JSONObject systemLvlNetStatJson = parseNetStatRecsToJson(netStatRecords);
		systemLvlRootJSON.put("SystemServersTopology", systemLvlNetStatJson);
		rootTopologyJson.accumulate("Instances", systemLvlRootJSON);
		//rootTopologyJson.write(arg0)
	}

	private List<String> getStandardSystemIP(String systemIps) {
		String[] systemIpsArr = systemIps.split(";");
		List<String> standardIps = new ArrayList<String>(systemIpsArr.length);
		for (String systemIp : systemIpsArr) {
			if (Utils.getDefaultLocalIps().contains(systemIp)) {
				continue;
			} else if (systemIp
					.startsWith(IPV6_REPRESENTATION_START_SUB_STRING)) {
				standardIps.add(Utils.downToIpV4(systemIp));
			} else {
				standardIps.add(systemIp);
			}
		}
		return standardIps;
	}

	private JSONObject parseNetStatRecsToJson(
			List<NetStatProcessModel> netStatRecords) {
		JSONObject systemLvlNetStatJson = new JSONObject();
		Set<String> foreignConnChannelIPPort = new HashSet<String>();
		for (NetStatProcessModel netStatRec : communicatingProcess) {
			JSONObject jsonNetStatRec = new JSONObject();
			if (listeningPorts.contains(netStatRec.getLocalSkt().getPort())) {
				setToAndFromSktOnJson(jsonNetStatRec, netStatRec.getLocalSkt(),
						netStatRec.getForeignSkt());
			} else {
				setToAndFromSktOnJson(jsonNetStatRec,
						netStatRec.getForeignSkt(), netStatRec.getLocalSkt());
				// TODO Handle duplication of Foreign Server Addr if required
				JSONObject jsonForeignServer = new JSONObject();
				try {
					String foreignIpV4 = Utils.downToIpV4(netStatRec
							.getForeignSkt().getIpAddress());
					String strIpPortWithSemiColon = foreignIpV4 + ':'
							+ netStatRec.getForeignSkt().getPort();
					if (!foreignConnChannelIPPort
							.contains(strIpPortWithSemiColon)) {
						jsonForeignServer.put("ForeignServerIPAddr",
								foreignIpV4);
						jsonForeignServer.put("ForeignServerPort", netStatRec
								.getForeignSkt().getPort());
						foreignConnChannelIPPort.add(strIpPortWithSemiColon);
					}
					systemLvlNetStatJson.accumulate("ForeignServers",
							jsonForeignServer);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			try {
				jsonNetStatRec.put("ProcessId", netStatRec.getProcessId());
				jsonNetStatRec.put("ProcessName", netStatRec.getProcessName());
				jsonNetStatRec.put("ConnectionState", netStatRec.getState());
				systemLvlNetStatJson.accumulate("CommunicatingProcess",
						jsonNetStatRec);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		for (Integer localServerPort : listeningPorts) {
			JSONObject jsonInternalServer = new JSONObject();
			try {
				jsonInternalServer.put("LocalServerPort", localServerPort);
				systemLvlNetStatJson.accumulate("LocalServers",
						jsonInternalServer);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return systemLvlNetStatJson;
		// System.out.println(rootNetStatJson.toString());
		// new ServerLocator().findServerDetailsOnNetStats(rootNetStatJson);
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
