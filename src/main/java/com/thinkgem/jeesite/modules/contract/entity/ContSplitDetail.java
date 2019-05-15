/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

import java.math.BigDecimal;

/**
 * 合同拆分细化Entity
 * @author cuijp
 * @version 2019-05-07
 */
public class ContSplitDetail extends DataEntity<ContSplitDetail> {
	
	private static final long serialVersionUID = 1L;
	private String remark;		// 备注
	private Integer status;		// 状态
	private String procInsId;		// 流程实例id
	private String contractId;		// 合同编号
	private BigDecimal total;		// 费用合计
	
	public ContSplitDetail() {
		super();
	}

	public ContSplitDetail(String id){
		super(id);
	}

	@Length(min=0, max=255, message="备注长度必须介于 0 和 255 之间")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}
}