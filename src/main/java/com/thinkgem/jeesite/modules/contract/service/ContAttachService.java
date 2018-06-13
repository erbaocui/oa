/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.contract.dao.ContAttachDao;
import com.thinkgem.jeesite.modules.contract.entity.ContAttach;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 合同请款Service
 * @author cuijp
 * @version 2018-06-07
 */
@Service
@Transactional(readOnly = true)
public class ContAttachService extends CrudService<ContAttachDao, ContAttach> {

	public ContAttach get(String id) {
		return super.get(id);
	}
	
	public List<ContAttach> findList(ContAttach contAttach) {
		return super.findList(contAttach);
	}
	
	public Page<ContAttach> findPage(Page<ContAttach> page, ContAttach contAttach) {
		return super.findPage(page, contAttach);
	}
	
	@Transactional(readOnly = false)
	public void save(ContAttach contAttach) {
		super.save(contAttach);
	}
	
	@Transactional(readOnly = false)
	public void delete(ContAttach contAttach) {
		super.delete(contAttach);
	}
	
}