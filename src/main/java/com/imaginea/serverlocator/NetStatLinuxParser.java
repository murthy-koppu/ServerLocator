package com.imaginea.serverlocator;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import com.imaginea.serverlocator.util.NetStatProcessModel;
import com.imaginea.serverlocator.util.SocketModel;

public class NetStatLinuxParser {
	private static Logger log = Logger.getLogger(NetStatLinuxParser.class);
	List<NetStatProcessModel> lsNetStatRecs = new ArrayList<NetStatProcessModel>();
	Set<String> localIps = new HashSet<String>();

	{
		localIps.add("127.0.0.1");
		localIps.add("0.0.0.0");
		localIps.add("localhost");
		localIps.add(":::");
		try {
			String[] strIP4LocAddr = Inet4Address.getLocalHost().toString()
					.split("/");
			if (strIP4LocAddr != null && strIP4LocAddr[0] != null
					&& !strIP4LocAddr[0].trim().isEmpty()) {
				localIps.add(strIP4LocAddr[0]);
				if (strIP4LocAddr.length == 2 && strIP4LocAddr[1] != null
						&& !strIP4LocAddr[1].trim().isEmpty()) {
					localIps.add("::ffff:" + strIP4LocAddr[1]);
					localIps.add(strIP4LocAddr[1]);
				}
			}
			String[] strIP6LocAddr = Inet6Address.getLocalHost().toString()
					.split("/");
			if (strIP6LocAddr != null && strIP6LocAddr[0] != null
					&& !strIP6LocAddr[0].trim().isEmpty()) {
				localIps.add(strIP6LocAddr[0]);
				if (strIP6LocAddr.length == 2 && strIP6LocAddr[1] != null
						&& !strIP6LocAddr[1].trim().isEmpty()) {
					localIps.add(strIP6LocAddr[1]);
				}
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public List<NetStatProcessModel> getNetStatRecords(String netStatInput) throws Exception {
		String[] netStatRecords = netStatInput.split(";");
		for(String netStatRecord : netStatRecords)
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
			
			SocketModel localSktMdl = new SocketModel(strLocalSkt.substring(0,
					ipAddrBound), strLocalSkt.substring(ipAddrBound + 1),
					localIps);
			
			ipAddrBound = strForeignSocket.lastIndexOf(":");
			SocketModel foreignSktMdl = new SocketModel(strForeignSocket.substring(0,
					ipAddrBound), strForeignSocket.substring(ipAddrBound + 1),
					localIps);
			if(!isInternalProcess(localSktMdl, connectionState)){
				NetStatProcessModel netStatRec = new NetStatProcessModel(processDtls[1], connectionState, processDtls[0], localSktMdl, foreignSktMdl);
				lsNetStatRecs.add(netStatRec);
			}			
		} catch (Exception e) {
			log.error("Unable to parse NetStat Record " + netStatRecord);
			throw e;
		}

	}

	private boolean isInternalProcess(SocketModel sktMdlIn,
			String connectionState) {
		return ((sktMdlIn.isLocalIp() && sktMdlIn.getPort() < 1025)
				|| sktMdlIn.isAllPorts()) ? true : false;
	}

}
