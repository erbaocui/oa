/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.income.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

import java.math.BigDecimal;

/**
 * 进款分配类型Entity
 * @author cuijp
 * @version 2019-03-26
 */
public class DistType extends DataEntity<DistType> {

	private static final long serialVersionUID = 1L;
	private String incomeId;		// 进款id
	private String type;		// 分配类型
	private BigDecimal value;		// 分配金额

	public DistType() {
		super();
	}

	public DistType(String id){
		super(id);
	}

	@Length(min=0, max=32, message="进款id长度必须介于 0 和 32 之间")
	public String getIncomeId() {
		return incomeId;
	}

	public void setIncomeId(String incomeId) {
		this.incomeId = incomeId;
	}
	
	@Length(min=0, max=32, message="分配类型长度必须介于 0 和 32 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}
	
}