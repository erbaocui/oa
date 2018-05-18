/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.income.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

import java.util.List;

/**
 * 分配规则Entity
 * @author cuijp
 * @version 2018-05-04
 */
public class Rule extends DataEntity<Rule> {
	
	private static final long serialVersionUID = 1L;
	private String groupId;
	private String name;		// 名字
	private String status;
	private String officeId;
	private List<RuleItem> itemList;

	
	public Rule() {
		super();
	}

	public Rule(String id){
		super(id);
	}

	@Length(min=0, max=255, message="名字长度必须介于 0 和 255 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<RuleItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<RuleItem> itemList) {
		this.itemList = itemList;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
}