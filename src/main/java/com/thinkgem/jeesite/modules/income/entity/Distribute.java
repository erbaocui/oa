/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.income.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

import java.math.BigDecimal;

/**
 * 分配Entity
 * @author cuijp
 * @version 2018-05-21
 */
public class Distribute extends DataEntity<Distribute> {
	
	private static final long serialVersionUID = 1L;
	private String ruleItemId;		// rule_item_id
	private String des;		// desc
	private BigDecimal value;		// value
	private Integer status;		// 1:未分配；2:已分配
	private String account;		// 账户
	private String incomeId;		// 入款id
	
	public Distribute() {
		super();
	}

	public Distribute(String id){
		super(id);
	}

	@Length(min=0, max=32, message="rule_item_id长度必须介于 0 和 32 之间")
	public String getRuleItemId() {
		return ruleItemId;
	}

	public void setRuleItemId(String ruleItemId) {
		this.ruleItemId = ruleItemId;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Length(min=0, max=255, message="账户长度必须介于 0 和 255 之间")
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
	
	@Length(min=0, max=255, message="入款id长度必须介于 0 和 255 之间")
	public String getIncomeId() {
		return incomeId;
	}

	public void setIncomeId(String incomeId) {
		this.incomeId = incomeId;
	}
	
}