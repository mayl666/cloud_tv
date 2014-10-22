package com.letv.portal.zabbixPush;

import com.letv.portal.model.ContainerModel;
import com.letv.portal.model.ZabbixPushModel;

 
public interface IZabbixPushService{
	
	/**
	 * Methods Name: createMultiContainerPushZabbixInfo <br>
	 * Description:创建多个container<br>
	 * @author name: wujun
	 * @param containerModels
	 */
	public void createMultiContainerPushZabbixInfo(ContainerModel[] containerModels);
	
	/**
	 * Methods Name: MultiContainerPushZabbixInfo <br>
	 * Description:  删除单个container<br>
	 * @author name: wujun
	 * @param containerModels
	 */
	public void deleteSingleContainerPushZabbixInfo(ContainerModel containerModel);
	/**
	 * Methods Name: loginZabbix <br>
	 * Description:登陆zabbix系统<br>
	 * @author name: wujun
	 * @param zabbixPushModel
	 * @return
	 */
	public String loginZabbix(); 
	/**
	 * Methods Name: createContainerPushZabbixInfo <br>
	 * Description: 创建container时向zabbix系统推送信息<br>
	 * @author name: wujun
	 * @param zabbixPushModel
	 * @return
	 */
	public Boolean pushZabbixInfo(ZabbixPushModel zabbixPushModel); 
	
	/**
	 * Methods Name: sendFixedInfo <br>
	 * Description: 向zabbix系统发送固资信息<br>
	 * @author name: wujun
	 * @throws Exception 
	 */
	public String sendZabbixInfo(Object object) throws Exception;
	/**
	 * Methods Name: receviceFixedInfo <br>
	 * Description: 接受zabbix系统的固资信息<br>
	 * @author name: wujun
	 */
	public String receviceZabbixInfo(ZabbixPushModel zabbixPushModel)throws Exception;
}
