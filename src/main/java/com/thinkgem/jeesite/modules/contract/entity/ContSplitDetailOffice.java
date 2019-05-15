/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.entity;

import com.thinkgem.jeesite.modules.sys.entity.Office;
import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

import java.math.BigDecimal;

/**
 * 合同拆分细化部门Entity
 * @author cuijp
 * @version 2019-05-07
 */
public class ContSplitDetailOffice extends DataEntity<ContSplitDetailOffice> {
	
	private static final long serialVersionUID = 1L;
	private Office office;		// 部门id
	private String itemId;		// 分项id
	private String itemName;
	private BigDecimal value;		// 分配金额
	private String remark;


	
	public ContSplitDetailOffice() {
		super();
	}

	public ContSplitDetailOffice(String id){
		super(id);
	}

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
	@Length(min=0, max=255, message="分项id长度必须介于 0 和 255 之间")
	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	
	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
}