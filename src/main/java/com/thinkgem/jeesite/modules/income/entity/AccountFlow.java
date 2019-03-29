/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.income.entity;

import com.thinkgem.jeesite.modules.contract.entity.Contract;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 账户流水Entity
 * @author cuijp
 * @version 2018-05-22
 */
public class AccountFlow extends DataEntity<AccountFlow> {
	
	private static final long serialVersionUID = 1L;
	private BigDecimal value;		// value
	private Integer type;		// 1:入账；2:出账
	private String incomeId;		// income_id
	private Account account;
	protected Date beginDate;	// 创建日期
	protected Date endDate;	// 创建日期
	private Contract contract;


	public AccountFlow() {
		super();
	}

	public AccountFlow(String id){
		super(id);
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}


	@Length(min=0, max=255, message="income_id长度必须介于 0 和 255 之间")
	public String getIncomeId() {
		return incomeId;
	}

	public void setIncomeId(String incomeId) {
		this.incomeId = incomeId;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

}