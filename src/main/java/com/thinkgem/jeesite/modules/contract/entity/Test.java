/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.act.entity.Act;
import com.thinkgem.jeesite.modules.project.entity.Project;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 合同管理Entity
 * @author cuijp
 * @version 2018-04-19
 */
public class Test  {
	String id;
	Act act;

	public Test() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}