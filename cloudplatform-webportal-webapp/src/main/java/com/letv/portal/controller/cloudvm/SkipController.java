package com.letv.portal.controller.cloudvm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.letv.common.exception.ValidateException;
import com.letv.common.result.ResultObject;
import com.letv.common.session.SessionServiceImpl;
import com.letv.portal.model.DbModel;

/**Program Name: SkipController <br>
 * Description:  用于页面跳转       list、detail、form、……<br>
 * @author name: liuhao1 <br>
 * Written Date: 2014年10月8日 <br>
 * Modified By: <br>
 * Modified Date: <br>
 */
@Controller("vmSkip")
public class SkipController {
	
	@Autowired(required=false)
	private SessionServiceImpl sessionService;
	
	@Value("${oauth.auth.http}")
	private String OAUTH_AUTH_HTTP;
	@Value("${webportal.local.http}")
	private String WEBPORTAL_LOCAL_HTTP;
	
	
	/**
	 * Methods Name: dbInfo<br>
	 * Description: 跳转基本信息页面
	 * @author name: yaokuo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value ="/detail/vmBaseInfo/{vmId}",method=RequestMethod.GET)
	public ModelAndView vmBaseInfo(@PathVariable String vmId,String region,ModelAndView mav){
		mav.addObject("vmId",vmId);
		mav.addObject("region",region);
		mav.setViewName("/cloudvm/baseInfo");
		return mav;
	}
	/**Methods Name: vmDetail <br>
	 * Description: 跳转至vm详情<br>
	 * @author name: liuhao1
	 * @param vmId
	 * @param mav
	 * @return
	 */
	@RequestMapping(value ="/detail/vm/{vmId}",method=RequestMethod.GET)
	public ModelAndView vmDetail(@PathVariable String vmId,String region,ModelAndView mav){
		mav.addObject("vmId",vmId);
		mav.addObject("region",region);
		mav.setViewName("/cloudvm/layout");
		return mav;
	}

	/**
	 * Methods Name: vmInfo<br>
	 * Description: 跳转数据库列表
	 * @author name: yaokuo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value ="/list/vm",method=RequestMethod.GET)
	public ModelAndView toVmList(ModelAndView mav){
		mav.setViewName("/cloudvm/vmList");
		return mav;
	}

	/**
	 * Methods Name: vmCreate<br>
	 * Description: 跳转数据库创建页面
	 * @author name: yaokuo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value ="/detail/vmCreate",method=RequestMethod.GET)
	public ModelAndView toVmCreate(ModelAndView mav){
		mav.setViewName("/cloudvm/vmCreate");
		return mav;
	}	
	
	/**
	 * Methods Name: vmCreate<br>
	 * Description: 跳转数据库创建页面
	 * @author name: yaokuo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value ="/detail/diskCreate",method=RequestMethod.GET)
	public ModelAndView toDiskCreate(ModelAndView mav){
		mav.setViewName("/cloudvm/diskCreate");
		return mav;
	}	
	
	@RequestMapping(value ="/list/vm/disk",method=RequestMethod.GET)
	public ModelAndView toVmDiskList(ModelAndView mav){
		mav.setViewName("/cloudvm/diskList");
		return mav;
	}
	
	@RequestMapping(value ="/detail/vmDisks/{vmId}",method=RequestMethod.GET)
	public ModelAndView toVmDisks(@PathVariable String vmId,String region,ModelAndView mav){
		mav.addObject("vmId",vmId);
		mav.addObject("region",region);
		mav.setViewName("/cloudvm/vmDisks");
		return mav;
	}
	
	@RequestMapping(value ="/vnc_auto.html",method=RequestMethod.GET)
	public ModelAndView vncAuto(ModelAndView mav){
		mav.setViewName("/cloudvm/vnc_auto");
		return mav;
	}
}
