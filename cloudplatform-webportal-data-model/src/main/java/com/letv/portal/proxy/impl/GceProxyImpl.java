package com.letv.portal.proxy.impl;


import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.letv.common.exception.CommonException;
import com.letv.common.exception.TaskExecuteException;
import com.letv.common.exception.ValidateException;
import com.letv.common.result.ApiResultObject;
import com.letv.common.util.AWSS3Util;
import com.letv.common.util.AWSS3Util.AWS3SConn;
import com.letv.portal.constant.Constant;
import com.letv.portal.enumeration.GcePackageStatus;
import com.letv.portal.enumeration.GceStatus;
import com.letv.portal.enumeration.GceType;
import com.letv.portal.enumeration.MclusterStatus;
import com.letv.portal.enumeration.SlbStatus;
import com.letv.portal.model.HostModel;
import com.letv.portal.model.elasticcalc.gce.EcGce;
import com.letv.portal.model.elasticcalc.gce.EcGceExt;
import com.letv.portal.model.elasticcalc.gce.EcGcePackage;
import com.letv.portal.model.gce.GceCluster;
import com.letv.portal.model.gce.GceContainer;
import com.letv.portal.model.gce.GceServer;
import com.letv.portal.model.gce.GceServerExt;
import com.letv.portal.model.log.LogCluster;
import com.letv.portal.model.log.LogServer;
import com.letv.portal.model.task.TaskResult;
import com.letv.portal.model.task.service.IBaseTaskService;
import com.letv.portal.model.task.service.ITaskEngine;
import com.letv.portal.proxy.IGceProxy;
import com.letv.portal.python.service.IGcePythonService;
import com.letv.portal.python.service.IPythonService;
import com.letv.portal.service.IBaseService;
import com.letv.portal.service.IHostService;
import com.letv.portal.service.elasticcalc.gce.IGcePackageService;
import com.letv.portal.service.elasticcalc.gce.IGceService;
import com.letv.portal.service.gce.IGceClusterService;
import com.letv.portal.service.gce.IGceContainerService;
import com.letv.portal.service.gce.IGceServerService;
import com.letv.portal.service.log.ILogClusterService;
import com.letv.portal.service.log.ILogServerService;
import com.letv.portal.util.CommonServiceUtils;

@Component
public class GceProxyImpl extends BaseProxyImpl<GceServer> implements
		IGceProxy{
	
	private final static Logger logger = LoggerFactory.getLogger(GceProxyImpl.class);
	
	@Autowired
	private IGceServerService gceServerService;
	@Autowired
	private IGceService gceService;
	@Autowired
	private IGcePackageService gcePackageService;
	
	@Autowired
	private IGcePythonService gcePythonService;
    @Autowired
    private IPythonService pythonService;
	@Autowired
	private IGceClusterService gceClusterService;
	@Autowired
	private IGceContainerService gceContainerService;
	@Autowired
	private ILogServerService logServerService;
	@Autowired
	private ILogClusterService logClusterService;
	@Autowired
	private IBaseTaskService baseGceTaskService;
	@Autowired
	private ITaskEngine taskEngine;
	@Autowired
	private IHostService hostService;
	
	@Value("${db.auto.build.count}")
	private int DB_AUTO_BUILD_COUNT;
	@Value("${nginx4jetty.code}")
	private String NGINX4JETTY_CODE;
	@Value("${gce.engine.category}")
	private String GCE_ENGINE_CATEGORY;
	@Value("${cluster.engine.category}")
	private String CLUSTER_ENGINE_CATEGORY;
	@Value("${matrix.gce.buy.process}")
	private String GCE_BUY_PROCESS;
	
	@Value("${matrix.gce.file.default.local}")
	private String MATRIX_GCE_FILE_DEFAULT_LOCAL;
	@Value("${matrix.gce.awss3.endpoint}")
	private String AWSS3ENDPOINT;
	@Value("${matrix.gce.awss3.accessKey}")
	private String AWSS3ACCESSKEY;
	@Value("${matrix.gce.awss3.secretKey}")
	private String AWSS3SECRETKEY;
	@Value("${matrix.gce.awss3.bucketName}")
	private String AWSS3BUCKETNAME;
	@Override
	public void saveAndBuild(GceServer gceServer,Long rdsId,Long ocsId) {	
		if(gceServer == null)
			throw new ValidateException("参数不合法");
		
		//参数转换防止XSS跨站漏洞
		gceServer.setGceName(StringEscapeUtils.escapeHtml(gceServer.getGceName()));
		gceServer.setDescn(StringEscapeUtils.escapeHtml(gceServer.getDescn()));
		
		//create logstash
		LogServer log = new LogServer();
		log.setLogName(gceServer.getGceName());
		log.setHclusterId(gceServer.getHclusterId());
		log.setCreateUser(gceServer.getCreateUser());
		log.setType("logstash");
		Map<String,Object> logParams = this.logServerService.save(log);
		
		gceServer.setLogId(log.getId());
		Map<String,Object> params = this.gceServerService.save(gceServer);
		Map<String,Object> nextParams = new HashMap<String,Object>();
		
		params.put("buyNum", gceServer.getBuyNum());
	    params.put("logParams", logParams);
		params.put("isCreateLog", true);
		params.put("isConfig", false);
		
		
		if(null !=rdsId ||null !=ocsId) {
			params.put("rdsId", rdsId);
			params.put("ocsId", ocsId);
			GceServerExt gse = new GceServerExt(gceServer.getId(),rdsId,ocsId);
			this.gceServerService.saveGceExt(gse);
		}
		
		if(gceServer.isCreateNginx()) {
			gceServer.setType(GceType.NGINX_PROXY);
			gceServer.setGceName(NGINX4JETTY_CODE+"_" + gceServer.getGceName());
			gceServer.setGceImageName("");
			nextParams = this.gceServerService.save(gceServer);
			nextParams.put("isContinue", false);
			nextParams.put("isConfig", true);
			nextParams.put("pGceId", params.get("gceId"));
			nextParams.put("pGceClusterId", params.get("gceClusterId"));
			
			nextParams.put("logIp", log.getIp());
			nextParams.put("logParams", logParams);
			nextParams.put("isCreateLog", false);
			
			params.put("isContinue", true);
			params.put("nextParams", nextParams);
		} else {
			params.put("isContinue", false);
		}
		
		this.build(params,gceServer.getType());
	}

	public void delete(GceServer gceServer) {
		GceServer gceProxyServer=this.gceServerService. selectProxyServerByGce(gceServer);
		if(gceProxyServer != null){
			this.delete(gceProxyServer);
		}
		
		this.gceServerService.delete(gceServer);
	}
	
	private void build(Map<String,Object> params,GceType type) {
		if(type.equals(GceType.JETTY)) {
			this.taskEngine.run(GCE_ENGINE_CATEGORY, params);
		} else {
			this.taskEngine.run(CLUSTER_ENGINE_CATEGORY, params);
		}
	}
	
	@Override
	public IBaseService<GceServer> getService() {
		return gceServerService;
	}
	
	@Override
	@Async
	public void restart(Long id) {
		GceServer gce = this.selectById(id);
		gce.setStatus(SlbStatus.STARTING.getValue());
		this.gceServerService.updateBySelective(gce);
		
		GceCluster cluster = this.gceClusterService.selectById(gce.getGceClusterId());
		List<GceContainer> containers = this.gceContainerService.selectByGceClusterId(cluster.getId());
		this.restart(gce,cluster,containers);
		this.checkStatus(gce, cluster, containers,"STARTED","GCE服务重启失败");
	}
	@Override
	@Async
	public void start(Long id) {
		GceServer gce = this.selectById(id);
		gce.setStatus(SlbStatus.STARTING.getValue());
		this.gceServerService.updateBySelective(gce);
		
		GceCluster cluster = this.gceClusterService.selectById(gce.getGceClusterId());
		List<GceContainer> containers = this.gceContainerService.selectByGceClusterId(cluster.getId());
		this.start(gce,cluster,containers);
		this.checkStatus(gce, cluster, containers,"STARTED","GCE服务启动失败");
	}
	@Override
	@Async
	public void stop(Long id) {
		GceServer gce = this.selectById(id);
		gce.setStatus(SlbStatus.STOPPING.getValue());
		this.gceServerService.updateBySelective(gce);
		
		GceCluster cluster = this.gceClusterService.selectById(gce.getGceClusterId());
		List<GceContainer> containers = this.gceContainerService.selectByGceClusterId(cluster.getId());
		this.stop(gce,cluster,containers);
		this.checkStatus(gce, cluster, containers,"STOP","GCE服务停止失败");
	}
	
	private boolean restart(GceServer slb,GceCluster cluster,List<GceContainer> containers) {
		ApiResultObject resultObject = this.gcePythonService.restart(null,containers.get(0).getHostIp(),containers.get(0).getMgrBindHostPort(),cluster.getAdminUser(), cluster.getAdminPassword());
		TaskResult tr = this.baseGceTaskService.analyzeRestServiceResult(resultObject);
		if(!tr.isSuccess()) {
			slb.setStatus(SlbStatus.ABNORMAL.getValue());
			this.gceServerService.updateBySelective(slb);
			throw new TaskExecuteException("SLB service restart error:" + tr.getResult()+",api url:" + resultObject.getUrl());
		}
		return tr.isSuccess();
	}
	
	private boolean stop(GceServer slb,GceCluster cluster,List<GceContainer> containers) {
		ApiResultObject resultObject = this.gcePythonService.stop(null,containers.get(0).getHostIp(),containers.get(0).getMgrBindHostPort(),cluster.getAdminUser(), cluster.getAdminPassword());
		TaskResult tr = this.baseGceTaskService.analyzeRestServiceResult(resultObject);
		if(!tr.isSuccess()) {
			slb.setStatus(SlbStatus.ABNORMAL.getValue());
			this.gceServerService.updateBySelective(slb);
			throw new TaskExecuteException("GCE service stop error:" + tr.getResult()+",api url:" + resultObject.getUrl());
		}
		return tr.isSuccess();
	}
	
	private boolean start(GceServer gce,GceCluster cluster,List<GceContainer> containers) {
		ApiResultObject resultObject = this.gcePythonService.start(null,containers.get(0).getHostIp(),containers.get(0).getMgrBindHostPort(), cluster.getAdminUser(), cluster.getAdminPassword());
		TaskResult tr = this.baseGceTaskService.analyzeRestServiceResult(resultObject);
		if(!tr.isSuccess()) {
			gce.setStatus(SlbStatus.ABNORMAL.getValue());
			this.gceServerService.updateBySelective(gce);
			throw new TaskExecuteException("GCE service start error:" + tr.getResult()+",api url:" + resultObject.getUrl());
		}
		return tr.isSuccess();
	}
	
	private String checkStatus(GceServer gce,GceCluster cluster,List<GceContainer> containers) {
		 TaskResult tr = new TaskResult();
		 ApiResultObject resultObject =  this.gcePythonService.checkStatus(containers.get(0).getHostIp(),containers.get(0).getMgrBindHostPort(), cluster.getAdminUser(), cluster.getAdminPassword());
		tr = this.baseGceTaskService.analyzeRestServiceResult(resultObject);
		if(!tr.isSuccess()) {
			gce.setStatus(SlbStatus.ABNORMAL.getValue());
			this.gceServerService.updateBySelective(gce);
			throw new TaskExecuteException("GCE service check start error:" + tr.getResult() +",api url:" + resultObject.getUrl());
		}
		Map<String,Object> params = (Map<String, Object>) tr.getParams();
		return (String) ((Map<String,Object>)params.get("data")).get("status");
	}
	
	private void checkStatus(GceServer gce,GceCluster cluster,List<GceContainer> containers,String expectStatus,String exception) {
		String status = "";
		for (int i = 0; i < 3; i++) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}
			status = this.checkStatus(gce,cluster,containers);
			if(expectStatus.equals(status))
				break;
		}
		if("".equals(status))
			throw new TaskExecuteException(exception);
		if("STARTED".equals(status))
			gce.setStatus(SlbStatus.NORMAL.getValue());
		if("STOP".equals(status))
			gce.setStatus(SlbStatus.STOPED.getValue());
		this.gceServerService.updateBySelective(gce);
	}

	@Override
	public void capacity(Long clusterId, int multiple) {
		GceServer gce = this.gceServerService.selectByClusterId(clusterId);
		if(gce == null)
			throw new ValidateException("GCE服务不存在");
		if(multiple == 0)
			throw new ValidateException("内存扩容倍数不能为空");
		gce.setMemorySize(gce.getMemorySize()*multiple);
		
		List<GceContainer> gcs = this.gceContainerService.selectByGceClusterId(clusterId);
		Map<String,String> params = new HashMap<String,String>();
		params.put("times", String.valueOf(multiple));
		for (GceContainer gceContainer : gcs) {
			params.put("containerNameList", gceContainer.getContainerName());
			ApiResultObject result = this.gcePythonService.capacity(params, gceContainer.getHostIp(),  "root","root");
			if(StringUtils.isEmpty(result.getResult()) || !result.getResult().contains("\"code\": 200")) {
				throw new ValidateException("扩容失败：相关api  " + result.getUrl());
			}
		}
		this.gceServerService.updateBySelective(gce);
	}

	@Override
    @Async
	public void checkStatus() {
		List<GceCluster> list = this.gceClusterService.selectByMap(null);
		for (GceCluster cluster : list) {
			if(MclusterStatus.BUILDDING.getValue() == cluster.getStatus()|| MclusterStatus.BUILDFAIL.getValue() == cluster.getStatus())
				continue;
			this.checkGceClusterStatus(cluster);
		}
        list.clear();
	}

	private void checkGceClusterStatus(GceCluster cluster) {
		HostModel host = this.hostService.getHostByHclusterId(cluster.getHclusterId());
        if(null == host) {
            cluster.setStatus(MclusterStatus.CRISIS.getValue());
            this.gceClusterService.updateBySelective(cluster);
            return;
        }
		String result = this.pythonService.checkMclusterStatus(cluster.getClusterName(),host.getHostIp(),host.getName(),host.getPassword());
		Map map = CommonServiceUtils.transResult(result);
		if(map.isEmpty()) {
			cluster.setStatus(MclusterStatus.CRISIS.getValue());
			this.gceClusterService.updateBySelective(cluster);
			return;
		}

		if(Constant.PYTHON_API_RESPONSE_SUCCESS.equals(String.valueOf(((Map)map.get("meta")).get("code")))) {
			Integer status = CommonServiceUtils.transStatus((String) ((Map) map.get("response")).get("status"));
			cluster.setStatus(status);
			this.gceClusterService.updateBySelective(cluster);
			if(status == MclusterStatus.NOTEXIT.getValue() || status == MclusterStatus.DESTROYED.getValue()) {
				this.gceClusterService.delete(cluster);
			}
            return;
		}

        if(null !=result && result.contains("not existed")){
			this.gceClusterService.delete(cluster);
            return;
		}

        cluster.setStatus(MclusterStatus.CRISIS.getValue());
        this.gceClusterService.updateBySelective(cluster);
	}
	
	@Transactional
	@Override
	public void createGce(EcGce gce, EcGceExt gceExt) {
		//1.参数转换防止XSS跨站漏洞
		gce.setGceName(StringEscapeUtils.escapeHtml(gce.getGceName()));
		if(!StringUtils.isEmpty(gce.getDescn()))
			gce.setDescn(StringEscapeUtils.escapeHtml(gce.getDescn()));
		//2.保存GCE信息到LogStash
		LogServer log = new LogServer();
		log.setLogName(gce.getGceName());
		log.setHclusterId(gce.getHclusterId());
		log.setCreateUser(gce.getCreateUser());
		log.setType("logstash");
		this.logServerService.save(log);
		//3.保存GCE信息
		gce.setLogId(log.getId());
		gce.setStatus(GceStatus.AVAILABLE.getValue());//可用
		this.gceService.insert(gce);
		//4.保存GCE扩展服务信息
		if(gceExt!=null && (gceExt.getOcsId().longValue()!=0L) && (gceExt.getRdsId().longValue()!=0L)){
			gceExt.setGceId(gce.getId());
			this.gceService.saveGceExt(gceExt);
		}
	}
	@Transactional//任意异常都回滚
	@Override
	public void uploadPackage(MultipartFile file, EcGcePackage gcePackage) {
		// TODO Auto-generated method stub
		//1.校验该GCE是否存在
		EcGce gce = this.gceService.selectById(gcePackage.getGceId());
		if(gce == null)
			throw new ValidateException("该GCE应用不存在");
		//2.接收文件，保存文件到s3上
		String fileName = file.getOriginalFilename();
		String suffix = fileName.substring(fileName.lastIndexOf("."));//.war
		try {
			saveFileToLocal(file);
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
			throw new CommonException("上传应用失败"+e.getMessage());
		}
		String key = gcePackage.getCreateUser()+"_"+gce.getId()+"_"+gcePackage.getVersion();//"createUserId_gceId_version";//不考虑创建GCE和上传包的人不同情况
		String filePath = this.MATRIX_GCE_FILE_DEFAULT_LOCAL+"/" + fileName;
		AWS3SConn.ConnBuilder builder = new AWS3SConn.ConnBuilder();
		AWS3SConn conn = builder.setEndpoint(this.AWSS3ENDPOINT).setAccessKey(this.AWSS3ACCESSKEY)
				.setSecretKey(this.AWSS3SECRETKEY).build();
		AWSS3Util.getInstance(conn).upload(this.AWSS3BUCKETNAME,key,filePath);
		removeFileFromLocal(fileName);
		//3.保存GcePackage到数据库
		gcePackage.setSuffix(suffix);
		gcePackage.setBucketName(this.AWSS3BUCKETNAME);
		gcePackage.setKey(key);
		gcePackage.setStatus(GcePackageStatus.BUILDDING.getValue());
		Map<String,Object> gcePackageParams = this.gcePackageService.save(gce,gcePackage);
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.putAll(gcePackageParams);
		params.put("gceId", gce.getId());
		params.put("gceName", gce.getGceName());
		params.put("buyNum", gce.getInstanceNum());
		
		//TODO 往集合里put后何作用
		LogServer logServer = this.logServerService.selectById(gce.getLogId());
		if(logServer != null) {
			LogCluster logCluster = this.logClusterService.selectById(logServer.getLogClusterId());
			if(logCluster != null){
				Map<String,Object> logParams = new HashMap<String, Object>();
				logParams.put("logClusterId", logCluster.getId());
				logParams.put("logId", logServer.getId());
				logParams.put("serviceName", logServer.getLogName());
				logParams.put("clusterName", logCluster.getClusterName());
				params.put("logParams", logParams);
			}
		}
		//TODO 往集合里put后何用途
		params.put("isCreateLog", true);
		params.put("isConfig", false);
		params.put("isContinue", false);
		//4.创建GCE应用包流程
		this.taskEngine.run(GCE_BUY_PROCESS, params);
	}
	
	private File saveFileToLocal(MultipartFile file) throws IllegalStateException, IOException {
        String fileName = file.getOriginalFilename(); 
        try {
			fileName = new String(fileName.getBytes("ISO8859-1"),"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
        File targetFile = new File(MATRIX_GCE_FILE_DEFAULT_LOCAL, fileName);  
        if(!targetFile.exists()){  
            targetFile.mkdirs();  
        }  
        file.transferTo(targetFile);  
		return targetFile;
	}
	private boolean removeFileFromLocal(String fileName) {
		File targetFile = new File(MATRIX_GCE_FILE_DEFAULT_LOCAL, fileName);  
		return targetFile.delete();
	}
}
