/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.income.entity;

import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 收款Entity
 * @author cuijp
 * @version 2018-05-09
 */
public class Receive extends DataEntity<Receive> {
	
	private static final long serialVersionUID = 1L;
	private String applyId;		// 请款编码
	private BigDecimal value;		// 收款值
	
	public Receive() {
		super();
	}

	public Receive(String id){
		super(id);
	}

	@Length(min=0, max=255, message="请款编码长度必须介于 0 和 255 之间")
	public String getApplyId() {
		return applyId;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}
}