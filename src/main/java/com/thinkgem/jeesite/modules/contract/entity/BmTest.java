/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.entity;

import com.thinkgem.jeesite.modules.sys.entity.Area;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 测试Entity
 * @author 测试
 * @version 2018-06-28
 */
public class BmTest extends DataEntity<BmTest> {
	
	private static final long serialVersionUID = 1L;
	private Area area;		// area_id
	
	public BmTest() {
		super();
	}

	public BmTest(String id){
		super(id);
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}
	
}