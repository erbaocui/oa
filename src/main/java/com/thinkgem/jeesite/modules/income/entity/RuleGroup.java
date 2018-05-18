/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.income.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 规则分组Entity
 * @author cuijp
 * @version 2018-05-17
 */
public class RuleGroup extends DataEntity<RuleGroup> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// name
	
	public RuleGroup() {
		super();
	}

	public RuleGroup(String id){
		super(id);
	}

	@Length(min=0, max=255, message="name长度必须介于 0 和 255 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}