package com.letv.portal.proxy.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.letv.portal.model.ContainerModel;
import com.letv.portal.model.MonitorDetailModel;
import com.letv.portal.model.MonitorIndexModel;
import com.letv.portal.proxy.IMonitorProxy;
import com.letv.portal.python.service.IBuildTaskService;
import com.letv.portal.service.IContainerService;
import com.letv.portal.service.IMonitorIndexService;
import com.letv.portal.service.IMonitorService;

@Component("monitorProxy")
public class MonitorProxyImpl implements IMonitorProxy{
	private final static Logger logger = LoggerFactory.getLogger(MonitorProxyImpl.class);
	@Autowired
	private IMonitorService monitorService;
	
	@Autowired
	private IBuildTaskService buildService;
	
	@Autowired
	private IContainerService containerService;
	
	@Autowired
	private IMonitorIndexService moniIndexService;
	
	@Override
	public void collectMclusterServiceData() {
		String ipAddr=null;
		Map mapKey = new  HashMap<String,String>();
		mapKey.put("type", "mclusternode");
		int i=0;
		Map map  = new HashMap<String, Object>();
		List<ContainerModel> cModels = this.containerService.selectAllByMap(mapKey);
		if(cModels!=null&&!"".equals(cModels)){
			int cSize = cModels.size();
			try {			
				for(i=0;i<cSize;i++){		
					ipAddr = cModels.get(i).getIpAddr();
					map =  this.buildService.getContainerServiceData(ipAddr);
					intoMclusterServiceDataDb(map);
				}					
			} catch (Exception e) {	
				if(i==cSize-1){
					logger.debug("集群出现错误，ip为"+ipAddr+" "+e.getMessage())	; 
				}else {
					for(int j=i+1;j<cSize;j++){		
						ipAddr = cModels.get(j).getIpAddr();
						try {
							map =  this.buildService.getContainerServiceData(ipAddr);
							intoMclusterServiceDataDb(map);
						} catch (Exception ex) {
							logger.debug("集群又出现错误，ip为"+ipAddr+" "+ex.getMessage()); 
						}			
					}
				}
				
			}
		}
	
	}

	private void intoMclusterServiceDataDb(Map map) throws Exception{
		for(Iterator it =  map.keySet().iterator();it.hasNext();){
			 Object keString = it.next();	
			 List<MonitorDetailModel> list  = (List<MonitorDetailModel>) map.get(keString);
			 for(MonitorDetailModel m:list){
               this.monitorService.insert(m);
			 }
		}	
	}

}
