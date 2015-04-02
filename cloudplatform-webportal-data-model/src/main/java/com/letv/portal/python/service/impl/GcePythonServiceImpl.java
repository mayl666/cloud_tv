package com.letv.portal.python.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.letv.common.util.HttpClient;
import com.letv.portal.python.service.IGcePythonService;
 
@Service("gcePythonService")
public class GcePythonServiceImpl implements IGcePythonService{
	
	private final static Logger logger = LoggerFactory.getLogger(GcePythonServiceImpl.class);
	
	private final static String URL_HEAD = "http://";
	private final static String URL_PORT = ":8888";	
	private final static String GCE_PORT = ":28930";	
	
	@Override
	public String createContainer(Map<String,String> params,String ip,String username,String password) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(ip).append(URL_PORT).append("/containerCluster");
		String result = HttpClient.post(url.toString(), params,username,password);
		return result;
	}

	@Override
	public String checkContainerCreateStatus(String gceClusterName,String ip,String username,String password) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(ip).append(URL_PORT).append("/containerCluster/createStatus/").append(gceClusterName);
		String result = HttpClient.get(url.toString(),username,password);
		return result;
	}

	@Override
	public String initZookeeper(String nodeIp,String port) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(nodeIp).append(":").append(port).append("/admin/conf");
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("zkAddress", "10.154.156.150");
		map.put("zkPort", "2181");
		
		String result = HttpClient.post(url.toString(), map);
		return result;
	}

	@Override
	public String initUserAndPwd4Manager(String nodeIp,String port,String username,String password) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(nodeIp).append(":").append(port).append("/admin/user");
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("adminUser", username);
		map.put("adminPassword", password);
		
		String result = HttpClient.post(url.toString(), map,username,password);
		return result;
	}

	@Override
	public String createContainer1(Map<String, String> params, String ip,String port,
			String username, String password) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(ip).append(":").append(port).append("/cluster");
		
		String result = HttpClient.post(url.toString(), params,username,password);
		return result;
	}

	@Override
	public String syncContainer2(Map<String, String> params,String nodeIp1,String port,
			String adminUser, String adminPassword) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(nodeIp1).append(":").append(port).append("/cluster/sync");
		
		String result = HttpClient.post(url.toString(), params,adminUser,adminPassword);
		return result;
	}

	@Override
	public String startCluster(String nodeIp1,String port, String adminUser,
			String adminPassword) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(nodeIp1).append(":").append(port).append("/cluster/start");
		
		String result = HttpClient.post(url.toString(), null,adminUser,adminPassword);
		return result;
	}

	@Override
	public String createContainer2(Map<String, String> params, String ip,
			String port, String username, String password) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(ip).append(":").append(port).append("/cluster/node");
		
		String result = HttpClient.post(url.toString(), params,username,password);
		return result;
	}

	@Override
	public String CheckClusterStatus(String nodeIp1, String port,
			String adminUser, String adminPassword) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(nodeIp1).append(":").append(port).append("/cluster");
		
		String result = HttpClient.get(url.toString(),adminUser,adminPassword);
		return result;
	}
}