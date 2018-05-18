/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.income.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

import java.math.BigDecimal;

/**
 * 分配规则细则Entity
 * @author cuijp
 * @version 2018-05-04
 */
public class RuleItem extends DataEntity<RuleItem> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 名字
	private BigDecimal percent;		// 分配比例
	private String ruleId;		// 规则名
	private String officeId;
	private Integer isTax;
	private Integer isFilingFee;
	
	public RuleItem() {
		super();
	}

	public RuleItem(String id){
		super(id);
	}

	@Length(min=0, max=255, message="名字长度必须介于 0 和 255 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPercent() {
		return percent;
	}

	public void setPercent(BigDecimal percent) {
		this.percent = percent;
	}

	@Length(min=0, max=255, message="规则名长度必须介于 0 和 255 之间")
	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public Integer getIsTax() {
		return isTax;
	}

	public void setIsTax(Integer isTax) {
		this.isTax = isTax;
	}

	public Integer getIsFilingFee() {
		return isFilingFee;
	}

	public void setIsFilingFee(Integer isFilingFee) {
		this.isFilingFee = isFilingFee;
	}
}