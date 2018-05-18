/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.income.vo;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.income.entity.Rule;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.List;

/**
 * 部门分配Entity
 * @author cuijp
 * @version 2018-05-14
 */
public class DistOfficeVo {

	private String id;
	private String incomeId;		// income_id
	private String officeId;
	private String officeName;// office_id
	private String  value;		// value
	private String  ruleGroupId;
	private List<RuleGroupVo> ruleGroups;
	private List<RuleVo> rules;
	private Integer rowspan;

	
	public DistOfficeVo() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIncomeId() {
		return incomeId;
	}

	public void setIncomeId(String incomeId) {
		this.incomeId = incomeId;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getRuleGroupId() {
		return ruleGroupId;
	}

	public void setRuleGroupId(String ruleGroupId) {
		this.ruleGroupId = ruleGroupId;
	}

	public List<RuleGroupVo> getRuleGroups() {
		return ruleGroups;
	}

	public void setRuleGroups(List<RuleGroupVo> ruleGroups) {
		this.ruleGroups = ruleGroups;
	}

	public List<RuleVo> getRules() {
		return rules;
	}

	public void setRules(List<RuleVo> rules) {
		this.rules = rules;
	}

	public Integer getRowspan() {
		return rowspan;
	}

	public void setRowspan(Integer rowspan) {
		this.rowspan = rowspan;
	}
}