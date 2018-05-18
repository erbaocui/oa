/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.income.entity;

import com.thinkgem.jeesite.modules.contract.entity.Contract;
import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

import java.math.BigDecimal;
import java.util.List;

/**
 * 收款Entity
 * @author cuijp
 * @version 2018-05-11
 */
public class Income extends DataEntity<Income> {
	
	private static final long serialVersionUID = 1L;
	private String applyId;		// 请款编码
	private Contract contract;
	private Integer status;		// 1:未分配；2：分配流程审核中；3：分配完成
	private BigDecimal value;		// 收款值
	private String procInsId;		// 流程实例id

	private List<DistOffice> distOfficeList;
	
	public Income() {
		super();
	}

	public Income(String id){
		super(id);
	}

	@Length(min=0, max=255, message="请款编码长度必须介于 0 和 255 之间")
	public String getApplyId() {
		return applyId;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}


	@Length(min=0, max=255, message="流程实例id长度必须介于 0 和 255 之间")
	public String getProcInsId() {
		return procInsId;
	}

	public void setProcInsId(String procInsId) {
		this.procInsId = procInsId;
	}

	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	public List<DistOffice> getDistOfficeList() {
		return distOfficeList;
	}

	public void setDistOfficeList(List<DistOffice> distOfficeList) {
		this.distOfficeList = distOfficeList;
	}
}