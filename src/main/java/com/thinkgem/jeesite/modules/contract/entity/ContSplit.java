/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 合同拆分Entity
 * @author cuijp
 * @version 2019-05-05
 */
public class ContSplit extends DataEntity<ContSplit> {
	
	private static final long serialVersionUID = 1L;
	private Integer status;		// 状态
	private String procInsId;		// 流程实例id
	private String contractId;		// 合同编号
	private Integer plan;		// 0不能保存1可以保存
	private Integer draw;		// 0不能保存1可以保存
	private String delFlag;
	
	public ContSplit() {
		super();
	}

	public ContSplit(String id){
		super(id);
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Length(min=0, max=255, message="流程实例id长度必须介于 0 和 255 之间")
	public String getProcInsId() {
		return procInsId;
	}

	public void setProcInsId(String procInsId) {
		this.procInsId = procInsId;
	}
	
	@Length(min=0, max=32, message="合同编号长度必须介于 0 和 32 之间")
	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public Integer getPlan() {
		return plan;
	}

	public void setPlan(Integer plan) {
		this.plan = plan;
	}

	public Integer getDraw() {
		return draw;
	}

	public void setDraw(Integer draw) {
		this.draw = draw;
	}

	@Override
	public String getDelFlag() {
		return delFlag;
	}

	@Override
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
}