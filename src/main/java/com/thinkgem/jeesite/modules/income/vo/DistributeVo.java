/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.income.vo;

import com.thinkgem.jeesite.modules.income.entity.Rule;

import java.util.List;

/**
 * 部门分配Entity
 * @author cuijp
 * @version 2018-05-14
 */
public class DistributeVo {

	private String name;

    private String value;

	public DistributeVo() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}