package com.letv.portal.service.product;

import java.util.Map;

import com.letv.portal.model.product.Product;
import com.letv.portal.service.IBaseService;

/**Program Name: IBaseElementService <br>
 * Description:  产品<br>
 * @author name: liuhao1 <br>
 * Written Date: 2015年7月30日 <br>
 * Modified By: <br>
 * Modified Date: <br>
 */
public interface IProductService extends IBaseService<Product> {
	Map<String, Object> queryProductDetailById(Long id);
	/**
	  * @Title: getRegionIdByCode
	  * @Description: 地域名称获取地域ID
	  * @param regionCode
	  * @return Long   
	  * @throws 
	  * @author lisuxiao
	  * @date 2015年9月19日 下午4:30:07
	  */
	Long getRegionIdByCode(String regionCode);
	/**
	  * @Title: validateData
	  * @Description: 验证产品信息是否合法
	  * @param id
	  * @param map
	  * @return boolean   
	  * @throws 
	  * @author lisuxiao
	  * @date 2015年9月6日 下午5:00:37
	  */
	boolean validateData(Long id, Map<String,Object> map);
}