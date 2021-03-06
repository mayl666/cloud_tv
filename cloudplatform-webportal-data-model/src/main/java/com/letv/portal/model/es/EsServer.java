package com.letv.portal.model.es;

import java.util.List;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.letv.common.model.BaseModel;
import com.letv.portal.enumeration.EsStatus;
import com.letv.portal.model.HclusterModel;
import com.letv.portal.model.UserModel;
import com.letv.portal.validation.annotation.IdValid;
import com.letv.portal.validation.annotation.NumberLimit;

public class EsServer extends BaseModel {
	
	private static final long serialVersionUID = -7999485658204466572L;

	/*
	 * es名称
	 */
	private String esName;
	/*
	 * 状态
	 */
	private EsStatus status;
	/*
	 * 描述
	 */
	private String descn;
	/*
	 * 物理机集群id
	 */
	private Long hclusterId;
	private HclusterModel hcluster;
	/*
	 * es集群
	 */
	private EsCluster esCluster;
	/*
	 * es集群id
	 */
	private Long esClusterId;
	/*
	 * 内存大小
	 */
	private Long memorySize;
	/*
	 * 存储大小
	 */
	private Long storageSize;
	/*
	 *节点数量 
	 */
	private Integer nodeCount;
	/*
	 * es节点
	 */
	private List<EsContainer> esContainers;
	private UserModel createUserModel;
	
	public UserModel getCreateUserModel() {
		return createUserModel;
	}
	public void setCreateUserModel(UserModel createUserModel) {
		this.createUserModel = createUserModel;
	}
	public HclusterModel getHcluster() {
		return hcluster;
	}
	public void setHcluster(HclusterModel hcluster) {
		this.hcluster = hcluster;
	}
	@NumberLimit(limits = {1073741824L,2147483648L,4294967296L},message = "内存大小必须在1073741824,2147483648,4294967296之中")
	public Long getMemorySize() {
		return memorySize;
	}
	public void setMemorySize(Long memorySize) {
		this.memorySize = memorySize;
	}
	public Long getEsClusterId() {
		return esClusterId;
	}
	public void setEsClusterId(Long esClusterId) {
		this.esClusterId = esClusterId;
	}
	
	@NotBlank
    @Pattern(regexp = "^[a-zA-Z_][a-zA-Z_0-9]{1,15}$",message = "内容必须以字母开头，允许字母数字下划线，长度在2-16字节内")
	public String getEsName() {
		return esName;
	}
	public void setEsName(String esName) {
		this.esName = esName;
	}
	public EsStatus getStatus() {
		return status;
	}
	public void setStatus(EsStatus status) {
		this.status = status;
	}
	
	@Length(max = 50)
	public String getDescn() {
		return descn;
	}
	public void setDescn(String descn) {
		this.descn = descn;
	}
	
	@IdValid(service = "hclusterService",message = "物理机集群id不合法")
	public Long getHclusterId() {
		return hclusterId;
	}
	public void setHclusterId(Long hclusterId) {
		this.hclusterId = hclusterId;
	}
	public EsCluster getEsCluster() {
		return esCluster;
	}
	public void setEsCluster(EsCluster esCluster) {
		this.esCluster = esCluster;
	}
	public List<EsContainer> getEsContainers() {
		return esContainers;
	}
	public void setEsContainers(List<EsContainer> esContainers) {
		this.esContainers = esContainers;
	}
	public Long getStorageSize() {
		return storageSize;
	}
	public void setStorageSize(Long storageSize) {
		this.storageSize = storageSize;
	}
	@NumberLimit(limits = {1,2,3,4,5},message = "购买数量必须在1,2,3,4,5之中")
	public Integer getNodeCount() {
		return nodeCount;
	}
	public void setNodeCount(Integer nodeCount) {
		this.nodeCount = nodeCount;
	}
	
}
