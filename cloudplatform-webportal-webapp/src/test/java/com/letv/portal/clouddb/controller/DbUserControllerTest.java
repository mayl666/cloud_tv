package com.letv.portal.clouddb.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;

import com.letv.common.result.ResultObject;
import com.letv.portal.junitBase.AbstractTest;
import com.letv.portal.model.DbUserModel;
import com.letv.portal.service.IDbUserService;

public class DbUserControllerTest extends AbstractTest{
	
	@Resource
	private IDbUserService dbUserService;
	
	/**
	 * Methods Name: list <br>
	 * Description:/{dbId}
	 * @author name: wujun
	 * @param dbId
	 * @param request
	 * @return
	 */
	@Test
	public void list() {
		Long dbId = 1L;
		ResultObject obj = new ResultObject();
		obj.setData(this.dbUserService.selectByDbId(dbId));
	    System.out.println(obj);
	}
	@Test
	public void InsertDbUser(){
		DbUserModel dbUserModel = new DbUserModel();
		dbUserModel.setAcceptIp("%");
		dbUserModel.setUsername("admin");
		dbUserModel.setPassword("admin");
		dbUserModel.setMaxConcurrency(1000);
	    this.dbUserService.insert(dbUserModel);
	    System.out.println(dbUserModel.getId());
	}
	/**Methods Name: save <br>
	 * Description: 保存创建信息 
	 * @author name: wujun
	 * @param dbApplyStandardModel
	 * @param request
	 * @return
	 */
	@Test
	public void save() {
			DbUserModel dbUserModel = new DbUserModel();
			String acceptIp = "192.168.1.4,192.168.1.5";
			dbUserModel.setMaxConcurrency(0);
			dbUserModel.setAcceptIp(acceptIp);		
			this.dbUserService.insertDbUserAndAcceptIp(dbUserModel);
	}
	
	/**
	 * Methods Name: validate <br>
	 * Description: /validate/{dbId}/{username}/{acceptIp}
	 * @author name: wujun
	 * @param dbUserModel
	 * @param request
	 * @return
	 */
	@Test
	public void validate() {
		DbUserModel dbUserModel = new DbUserModel();
		dbUserModel.setAcceptIp("192.168.1.2");
		dbUserModel.setUsername("test1");
		dbUserModel.setDbId(1L);
		Map<String,Object> map = new HashMap<String,Object>();
		List<DbUserModel> list = this.dbUserService.selectByIpAndUsername(dbUserModel);
		map.put("valid", list.size()>0?false:true);
		System.out.println(map.get("valid"));
	}
	/**
	 * Methods Name: updateDbUser <br>
	 * Description: 修改dbUser信息
	 * @author name: wujun
	 */
	@Test
    public  void updateDbUser(){
		try {
			DbUserModel dbUserModel = new DbUserModel();
			dbUserModel.setId(1L);
			dbUserModel.setUsername("wujunTest1");
			this.dbUserService.update(dbUserModel);
		} catch (Exception e) {
		System.out.print(e.getMessage());
		}

    }
	/**
	 * Methods Name: deleteDbUserById <br>
	 * Description: 删除dbUser信息
	 * @author name: wujun
	 */
	@Test
    public  void deleteDbUserById(){
		DbUserModel dbUserModel = new DbUserModel();
		dbUserModel.setId(1L);
		this.dbUserService.delete(dbUserModel);
    }
	
}
