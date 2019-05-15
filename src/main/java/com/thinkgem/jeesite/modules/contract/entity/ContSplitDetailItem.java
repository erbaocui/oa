/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

import java.math.BigDecimal;
import java.util.List;

/**
 * 合同拆分细化分项Entity
 * @author cuijp
 * @version 2019-05-07
 */
public class ContSplitDetailItem extends DataEntity<ContSplitDetailItem> {
	
	private static final long serialVersionUID = 1L;
	private String detailId;		// 合同细化Id
	private BigDecimal value;		// 分配值
	private Integer type;		// 类型
	private String remark;
	private String officeId;
	private List<ContSplitDetailOffice> officeList;
	public ContSplitDetailItem() {
		super();
	}

	public ContSplitDetailItem(String id){
		super(id);
	}

	@Length(min=0, max=255, message="合同细化Id长度必须介于 0 和 255 之间")
	public String getDetailId() {
		return detailId;
	}

	public void setDetailId(String detailId) {
		this.detailId = detailId;
	}
	
	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}
	
	@Length(min=0, max=255, message="类型长度必须介于 0 和 255 之间")
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<ContSplitDetailOffice> getOfficeList() {
		return officeList;
	}

	public void setOfficeList(List<ContSplitDetailOffice> officeList) {
		this.officeList = officeList;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
}