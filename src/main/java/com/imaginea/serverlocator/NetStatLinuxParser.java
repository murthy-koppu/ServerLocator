package com.imaginea.serverlocator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.imaginea.serverlocator.model.NetStatProcessModel;
import com.imaginea.serverlocator.model.NetStatSocketModel;
import com.imaginea.serverlocator.util.ApplicationConstants;
import com.imaginea.serverlocator.util.Utils;

public class NetStatLinuxParser implements ApplicationConstants{
	private static Logger log = Logger.getLogger(NetStatLinuxParser.class);
	List<NetStatProcessModel> lsNetStatRecs = new ArrayList<NetStatProcessModel>();
	Set<String> localIps = new HashSet<String>();
	//Set<String> defaultLocalIps = new HashSet<String>();
	private String systemIpAddr = null;
	public NetStatLinuxParser() {
		localIps.addAll(Utils.getDefaultLocalIps());
	}

/*	private void initializeLocalIps() {
		defaultLocalIps.add("127.0.0.1");
		defaultLocalIps.add("0.0.0.0");
		defaultLocalIps.add("localhost");
		defaultLocalIps.add(":::");
		localIps.addAll(defaultLocalIps);
	}*/

	public List<NetStatProcessModel> getNetStatRecords(String netStatInput)
			throws Exception {
		String[] netStatRecords = netStatInput.split(";");
		for (String netStatRecord : netStatRecords)
			parseNetStatRecord(netStatRecord);
		
		return lsNetStatRecs;
	}

	private void parseNetStatRecord(String netStatRecord) throws Exception {
		try {
			String[] netStatAttribs = netStatRecord.split(",");
			String strLocalSkt = netStatAttribs[0];
			String strForeignSocket = netStatAttribs[1];
			String connectionState = netStatAttribs[2];
			String[] processDtls = netStatAttribs[3].split("/");

			int ipAddrBound = strLocalSkt.lastIndexOf(":");

			NetStatSocketModel localSktMdl = new NetStatSocketModel(strLocalSkt.substring(0,
					ipAddrBound), strLocalSkt.substring(ipAddrBound + 1), true);
			localIps.add(localSktMdl.getIpAddress());

			if (!isInternalProcess(localSktMdl)) {
				ipAddrBound = strForeignSocket.lastIndexOf(":");
				NetStatSocketModel foreignSktMdl = new NetStatSocketModel(
						strForeignSocket.substring(0, ipAddrBound),
						strForeignSocket.substring(ipAddrBound + 1), false);

				if (NET_STAT_PROCESS_COMMUNICATION_STATE.equals(connectionState) || !isInternalProcess(foreignSktMdl)) {
					NetStatProcessModel netStatRec = new NetStatProcessModel(
							processDtls[1], connectionState, processDtls[0],
							localSktMdl, foreignSktMdl);
					lsNetStatRecs.add(netStatRec);
				}
			}

		} catch (Exception e) {
			log.error("Unable to parse NetStat Record " + netStatRecord);
			throw e;
		}

	}

	private boolean isInternalProcess(NetStatSocketModel sktMdlIn) {
		return (sktMdlIn.getPort() < 1025 || sktMdlIn.isAllPorts()) ? true
				: false;
	}

}
