/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.contract.entity.ContSplit;
import com.thinkgem.jeesite.modules.contract.dao.ContSplitDao;

/**
 * 合同拆分Service
 * @author cuijp
 * @version 2019-05-05
 */
@Service
@Transactional(readOnly = true)
public class ContSplitService extends CrudService<ContSplitDao, ContSplit> {

	public ContSplit get(String id) {
		return super.get(id);
	}
	public ContSplit getOne(ContSplit contSplit) {
		return dao.getOne( contSplit);
	}
	
	public List<ContSplit> findList(ContSplit contSplit) {
		return super.findList(contSplit);
	}
	
	public Page<ContSplit> findPage(Page<ContSplit> page, ContSplit contSplit) {
		return super.findPage(page, contSplit);
	}
	
	@Transactional(readOnly = false)
	public void save(ContSplit contSplit) {
		super.save(contSplit);
	}
	
	@Transactional(readOnly = false)
	public void delete(ContSplit contSplit) {
		super.delete(contSplit);
	}
	
}