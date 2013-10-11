package com.imaginea.serverlocator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.imaginea.serverlocator.util.ApplicationConstants;

public class ConcreteTopologyPublisher implements ApplicationConstants {
	private JSONObject genericTopologyJson;
	private Map<String, JSONObject> privateIpToInstanceJsonProp = new HashMap<String, JSONObject>();
	JSONArray newInstanceNodesDrvdFmNetstat = new JSONArray();
	private volatile int jsonNodesLength = 0;
	List<JSONObject> lsNewDerivedNodesFmNetStat = new ArrayList<JSONObject>();

	public void loadGenericTopologyFromFile(String filePath) {
		BufferedReader jsonFileReader;
		try {
			jsonFileReader = new BufferedReader(new InputStreamReader(
					new FileInputStream(filePath)));
			StringBuilder strGenericTopologyJson = new StringBuilder();
			String topologyJsonLine = "";
			while ((topologyJsonLine = jsonFileReader.readLine()) != null) {
				strGenericTopologyJson.append(topologyJsonLine);
			}
			genericTopologyJson = new JSONObject(
					strGenericTopologyJson.toString());
			genericTopologyJson.remove("links");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void loadJsonInstNodesToMap() {
		JSONArray instNodes;
		try {
			instNodes = genericTopologyJson.getJSONArray("nodes");
			jsonNodesLength = instNodes.length();
			if (instNodes != null) {
				for (int i = 0; i < jsonNodesLength; i++) {
					JSONObject instanceNode = instNodes.getJSONObject(i);
					privateIpToInstanceJsonProp.put((String) instanceNode
							.get(TOPOLOGY_INSTANCE_PRIVATE_IP_PROP_NAME),
							instanceNode);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void appendLinksToTopologyJson(String localIp, String foreignIp,
			boolean isLocalServer) {
		JSONObject localInstJsonProp = privateIpToInstanceJsonProp.get(localIp);
		JSONObject foreignInstJsonProp = privateIpToInstanceJsonProp
				.get(foreignIp);

		if (foreignInstJsonProp == null) {
			synchronized (this) {
				foreignInstJsonProp = privateIpToInstanceJsonProp
						.get(foreignIp);
				if (foreignInstJsonProp == null) {
					foreignInstJsonProp = new JSONObject();
					try {
						foreignInstJsonProp.put(
								TOPOLOGY_INSTANCE_PRIVATE_IP_PROP_NAME,
								foreignIp);
						foreignInstJsonProp.put(
								TOPOLOGY_INSTANCE_IS_START_POINT, false);
						foreignInstJsonProp.put(
								TOPOLOGY_INSTANCE_NODE_SERIAL_NO,
								++jsonNodesLength);
						privateIpToInstanceJsonProp.put(foreignIp,
								foreignInstJsonProp);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		}
		try {
			String localNodeSeqNo = localInstJsonProp
					.getString(TOPOLOGY_INSTANCE_NODE_SERIAL_NO);
			String foreignNodeSeqNo = foreignInstJsonProp
					.getString(TOPOLOGY_INSTANCE_NODE_SERIAL_NO);

			JSONObject linkJson = new JSONObject();
			if (isLocalServer) {
				linkJson.put(TOPOLOGY_INSTANCE_LINK_SOURCE, foreignNodeSeqNo);
				linkJson.put(TOPOLOGY_INSTANCE_LINK_TARGET, localNodeSeqNo);
			} else {
				linkJson.put(TOPOLOGY_INSTANCE_LINK_SOURCE, localNodeSeqNo);
				linkJson.put(TOPOLOGY_INSTANCE_LINK_TARGET, foreignNodeSeqNo);
			}
			linkJson.put(TOPOLOGY_INSTANCE_LINK_RELATIONSHIP, "rel");
			synchronized (genericTopologyJson) {
				genericTopologyJson.append(TOPOLOGY_INSTANCE_LINKS_PARENT,
						linkJson);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void publishInstanceReltaionshipsFromNetStatToJson() {

	}

	public static void main(String[] args) {
		new ConcreteTopologyPublisher()
				.loadGenericTopologyFromFile(ApplicationConstants.RESOURCES_LOCATION_PATH
						+ "only_data.txt");
	}
}
