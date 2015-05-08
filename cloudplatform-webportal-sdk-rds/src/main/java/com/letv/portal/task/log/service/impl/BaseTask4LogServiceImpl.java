package com.letv.portal.task.log.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.letv.common.email.ITemplateMessageSender;
import com.letv.common.email.bean.MailMessage;
import com.letv.common.exception.ValidateException;
import com.letv.portal.constant.Constant;
import com.letv.portal.enumeration.DbStatus;
import com.letv.portal.enumeration.MclusterStatus;
import com.letv.portal.model.HostModel;
import com.letv.portal.model.UserModel;
import com.letv.portal.model.common.ZookeeperInfo;
import com.letv.portal.model.log.LogCluster;
import com.letv.portal.model.log.LogContainer;
import com.letv.portal.model.log.LogServer;
import com.letv.portal.model.task.TaskResult;
import com.letv.portal.model.task.service.IBaseTaskService;
import com.letv.portal.service.IHostService;
import com.letv.portal.service.IUserService;
import com.letv.portal.service.common.IZookeeperInfoService;
import com.letv.portal.service.log.ILogClusterService;
import com.letv.portal.service.log.ILogContainerService;
import com.letv.portal.service.log.ILogServerService;

@Component("baseLogTaskService")
public class BaseTask4LogServiceImpl implements IBaseTaskService{

	@Value("${error.email.to}")
	private String ERROR_MAIL_ADDRESS;
	@Autowired
	private ITemplateMessageSender defaultEmailSender;
	
	@Autowired
	private IHostService hostService;
	@Autowired
	private ILogClusterService LogClusterService;
	@Autowired
	private ILogServerService LogServerService;
	@Autowired
	private ILogContainerService LogContainerService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IZookeeperInfoService zookeeperInfoService;
	
	private final static Logger logger = LoggerFactory.getLogger(BaseTask4LogServiceImpl.class);
	
	@Override
	public void beforExecute(Map<String, Object> params) {
		LogServer log = this.getLogServer(params);
		LogCluster cluster = this.getLogCluster(params);
		if(log.getStatus() != DbStatus.BUILDDING.getValue()) {
			log.setStatus(DbStatus.BUILDDING.getValue());
			this.LogServerService.updateBySelective(log);
		}
		if(log.getStatus() != MclusterStatus.BUILDDING.getValue()) {
			cluster.setStatus(MclusterStatus.BUILDDING.getValue());
			this.LogClusterService.updateBySelective(cluster);
		}
	}
	
	@Override
	public TaskResult execute(Map<String, Object> params) throws Exception {
		TaskResult tr = new TaskResult();
		if(params == null || params.isEmpty()) {
			tr.setResult("params is empty");
			tr.setSuccess(false);
		}
		tr.setParams(params);
		return tr;
	}

	@Override
	public void rollBack(TaskResult tr) {
		Map<String,Object> params = (Map<String, Object>) tr.getParams();
		//发送邮件
		this.buildResultToMgr("log服务创建", tr.isSuccess()?"创建成功":"创建失败", tr.getResult(), ERROR_MAIL_ADDRESS);
		//业务处理
		this.serviceOver(tr);
	}
	
	private void serviceOver(TaskResult tr) {
		Map<String, Object> params = (Map<String, Object>) tr.getParams();
		LogServer log = this.getLogServer(params);
		LogCluster cluster = this.getLogCluster(params);
		
		if(tr.isSuccess()) {
			log.setStatus(DbStatus.NORMAL.getValue());
			cluster.setStatus(MclusterStatus.RUNNING.getValue());
			Map<String, Object> emailParams = new HashMap<String,Object>();
			emailParams.put("logName", log.getLogName());
			this.email4User(emailParams, log.getCreateUser(),"log/createLog.ftl");
		} else {
			log.setStatus(DbStatus.BUILDFAIL.getValue());
			cluster.setStatus(MclusterStatus.BUILDFAIL.getValue());
		}
		this.LogServerService.updateBySelective(log);
		this.LogClusterService.updateBySelective(cluster);
	}

	@Override
	public void callBack(TaskResult tr) {
		
	}

	@SuppressWarnings("unchecked")
	public TaskResult analyzeRestServiceResult(String result){
		TaskResult tr = new TaskResult();
		Map<String, Object> map = transToMap(result);
		if(map == null) {
			tr.setSuccess(false);
			tr.setResult("api connect failed");
			return tr;
		}
		Map<String,Object> meta = (Map<String, Object>) map.get("meta");
		
		boolean isSucess = Constant.PYTHON_API_RESPONSE_SUCCESS.equals(String.valueOf(meta.get("code")));
		tr.setSuccess(isSucess);
		if(isSucess) {
			Map<String,Object> response = (Map<String, Object>) map.get("response");
			tr.setResult((String) response.get("message"));
			tr.setParams(response);
		} else {
			tr.setResult((String) meta.get("errorType") +":"+ (String) meta.get("errorDetail"));
		}
		return tr;
		
	}
	
	public void buildResultToMgr(String buildType,String result,String detail,String to){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("buildType", buildType);
		map.put("buildResult", result);
		map.put("errorDetail", detail);
		MailMessage mailMessage = new MailMessage("乐视云平台web-portal系统", StringUtils.isEmpty(to)?ERROR_MAIL_ADDRESS:to,"乐视云平台web-portal系统通知","buildForMgr.ftl",map);
		defaultEmailSender.sendMessage(mailMessage);
	}
	public void email4User(Map<String,Object> params,Long to,String ftlName){
		UserModel user = this.userService.selectById(to);
		if(null != user) {
			MailMessage mailMessage = new MailMessage("乐视云平台web-portal系统",user.getEmail(),"乐视云平台web-portal系统通知",ftlName,params);
			mailMessage.setHtml(true);
			defaultEmailSender.sendMessage(mailMessage);
		}
	}
	
	@SuppressWarnings("unchecked")
	public Map<String,Object> transToMap(String params){
		if(StringUtils.isEmpty(params))
			return null;
		ObjectMapper resultMapper = new ObjectMapper();
		Map<String,Object> jsonResult = new HashMap<String,Object>();
		try {
			jsonResult = resultMapper.readValue(params, Map.class);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return jsonResult;
	}
	
	public String transToString(Object params){
		if(params == null)
			return null;
		ObjectMapper resultMapper = new ObjectMapper();
		String jsonResult = "";
		try {
			jsonResult = resultMapper.writeValueAsString(params);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return jsonResult;
	}
	
	public Long getLongFromObject(Object o) {
		Long value = null;
		if(o instanceof String)
			value = Long.parseLong((String) o);
		if(o instanceof Integer)
			value = Long.parseLong(((Integer)o).toString());
		if(o instanceof Long)
			value = (Long) o;
		
		return value;
	}
	
	public LogServer getLogServer(Map<String, Object> params) {
		Long logId = getLongFromObject(params.get("logId"));
		if(logId == null)
			throw new ValidateException("params's logId is null");
		
		LogServer LogServer = this.LogServerService.selectById(logId);
		if(LogServer == null)
			throw new ValidateException("LogServer is null by logId:" + logId);
		
		return LogServer;
	}
	
	public LogCluster getLogCluster(Map<String, Object> params) {
		Long logClusterId = getLongFromObject(params.get("logClusterId"));
		if(logClusterId == null)
			throw new ValidateException("params's logClusterId is null");
		
		LogCluster logCluster = this.LogClusterService.selectById(logClusterId);
		if(logCluster == null)
			throw new ValidateException("logCluster is null by logClusterId:" + logClusterId);
		
		return logCluster;
	}
	
	public HostModel getHost(Long hclusterId) {
		if(hclusterId == null)
			throw new ValidateException("hclusterId is null :" + hclusterId);
		HostModel host = this.hostService.getHostByHclusterId(hclusterId);
		if(host == null)
			throw new ValidateException("host is null by hclusterIdId:" + hclusterId);
		
		return host;
	}
	public List<LogContainer> getContainers(Map<String, Object> params) {
		Long LogClusterId = getLongFromObject(params.get("logClusterId"));
		if(LogClusterId == null)
			throw new ValidateException("params's logClusterId is null");
		
		List<LogContainer> logContainers = this.LogContainerService.selectByLogClusterId(LogClusterId);
		if(logContainers == null || logContainers.isEmpty())
			throw new ValidateException("logContainers is null by logClusterId:" + LogClusterId);
		return logContainers;
	}
	
	public ZookeeperInfo getMinusedZk() {
		ZookeeperInfo zk = this.zookeeperInfoService.selectMinusedZk();
		if(zk == null)
			throw new ValidateException("zk is null");
		zk.setUsed(zk.getUsed()+1);
		this.zookeeperInfoService.updateBySelective(zk);
		return zk;
	}
}