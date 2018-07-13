/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.contract.entity.ContApply;
import com.thinkgem.jeesite.modules.contract.dao.ContApplyDao;

/**
 * 合同请款Service
 * @author cuijp
 * @version 2018-06-07
 */
@Service
@Transactional(readOnly = true)
public class ContApplyService extends CrudService<ContApplyDao, ContApply> {

	public ContApply get(String id) {
		return super.get(id);
	}

	public ContApply getLast(String contractId) {
		return  dao.getLast(contractId);
	}
	
	public List<ContApply> findList(ContApply contApply) {
		return super.findList(contApply);
	}
	
	public Page<ContApply> findPage(Page<ContApply> page, ContApply contApply) {
		return super.findPage(page, contApply);
	}
	
	@Transactional(readOnly = false)
	public void save(ContApply contApply) {
		super.save(contApply);
	}
	
	@Transactional(readOnly = false)
	public void delete(ContApply contApply) {
		super.delete(contApply);
	}
	
}