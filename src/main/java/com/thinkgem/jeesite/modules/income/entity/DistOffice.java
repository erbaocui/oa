/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.income.entity;

import org.hibernate.validator.constraints.Length;
import com.thinkgem.jeesite.modules.sys.entity.Office;

import com.thinkgem.jeesite.common.persistence.DataEntity;

import java.math.BigDecimal;
import java.util.List;

/**
 * 部门分配Entity
 * @author cuijp
 * @version 2018-05-14
 */
public class DistOffice extends DataEntity<DistOffice> {
	
	private static final long serialVersionUID = 1L;
	private String incomeId;		// income_id
	private Office office;		// office_id
	private BigDecimal value;		// value
	private String ruleId;		// rule_id
	private String groupId;
	private List<Rule> ruleList;
	
	public DistOffice() {
		super();
	}

	public DistOffice(String id){
		super(id);
	}

	@Length(min=0, max=32, message="income_id长度必须介于 0 和 32 之间")
	public String getIncomeId() {
		return incomeId;
	}

	public void setIncomeId(String incomeId) {
		this.incomeId = incomeId;
	}
	
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	@Length(min=0, max=32, message="rule_id长度必须介于 0 和 32 之间")
	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
}