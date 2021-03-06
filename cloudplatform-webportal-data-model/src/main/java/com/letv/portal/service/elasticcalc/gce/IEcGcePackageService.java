/**
 *
 *  Copyright (c) 2016 乐视云计算有限公司（lecloud.com）. All rights reserved
 *
 */
package com.letv.portal.service.elasticcalc.gce;

import java.util.Map;

import com.letv.portal.model.elasticcalc.gce.EcGce;
import com.letv.portal.model.elasticcalc.gce.EcGcePackage;
import com.letv.portal.service.IBaseService;

/**
 * IEcGcePackageService
 * @author linzhanbo .
 * @since 2016年6月29日, 上午9:48:54 .
 * @version 1.0 .
 */
public interface IEcGcePackageService extends IBaseService<EcGcePackage> {
	/**
	 * 保存该GCE应用部署包
	 * @param gce
	 * @param gcePackage
	 * @return
	 * @author linzhanbo .
	 * @since 2016年6月29日, 上午10:01:22 .
	 * @version 1.0 .
	 */
	public Map<String, Object> insertGceAndGcePackage(EcGce gce, EcGcePackage gcePackage);
	/**
	 * 保存GCE集群信息
	 * @param gce
	 * @param gcePackage
	 * @return
	 */
	public Map<String, Object> insertGceCuster(EcGce gce, EcGcePackage gcePackage);

}
