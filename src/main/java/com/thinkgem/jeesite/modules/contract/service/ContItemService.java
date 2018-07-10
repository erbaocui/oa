/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.contract.entity.ContItem;
import com.thinkgem.jeesite.modules.contract.dao.ContItemDao;

/**
 * 合同付款约定Service
 * @author cuijp
 * @version 2018-07-01
 */
@Service
@Transactional(readOnly = true)
public class ContItemService extends CrudService<ContItemDao, ContItem> {

	public ContItem get(String id) {
		return super.get(id);
	}
	
	public List<ContItem> findList(ContItem contItem) {
		return super.findList(contItem);
	}
	
	public Page<ContItem> findPage(Page<ContItem> page, ContItem contItem) {
		return super.findPage(page, contItem);
	}
	
	@Transactional(readOnly = false)
	public void save(ContItem contItem) {
		super.save(contItem);
	}
	
	@Transactional(readOnly = false)
	public void delete(ContItem contItem) {
		super.delete(contItem);
	}
	
}