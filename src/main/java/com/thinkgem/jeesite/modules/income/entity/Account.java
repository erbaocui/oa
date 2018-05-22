/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.income.entity;

import org.hibernate.validator.constraints.Length;
import com.thinkgem.jeesite.modules.sys.entity.Office;

import com.thinkgem.jeesite.common.persistence.DataEntity;

import java.math.BigDecimal;

/**
 * 账户Entity
 * @author cuijp
 * @version 2018-05-22
 */
public class Account extends DataEntity<Account> {
	
	private static final long serialVersionUID = 1L;
	private String account;		// account
	private String name;		// name
	private BigDecimal value;		// value
	private Office office;		// office_id
	
	public Account() {
		super();
	}

	public Account(String id){
		super(id);
	}

	@Length(min=0, max=255, message="account长度必须介于 0 和 255 之间")
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
	
	@Length(min=0, max=255, message="name长度必须介于 0 和 255 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
}