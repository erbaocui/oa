/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.project.entity;

import org.hibernate.validator.constraints.Length;
import com.thinkgem.jeesite.modules.sys.entity.Office;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 项目Entity
 * @author cuijp
 * @version 2018-06-21
 */
public class Project extends DataEntity<Project> {
	
	private static final long serialVersionUID = 1L;
	private String code;		// code
	private String name;		// name
	private Office office;		// office_id
	private String manager;		// manager
	private String custom;		// custom
	
	public Project() {
		super();
	}

	public Project(String id){
		super(id);
	}

	@Length(min=0, max=255, message="code长度必须介于 0 和 255 之间")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@Length(min=0, max=255, message="name长度必须介于 0 和 255 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
	@Length(min=0, max=255, message="manager长度必须介于 0 和 255 之间")
	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}
	
	@Length(min=0, max=255, message="custom长度必须介于 0 和 255 之间")
	public String getCustom() {
		return custom;
	}

	public void setCustom(String custom) {
		this.custom = custom;
	}
	
}