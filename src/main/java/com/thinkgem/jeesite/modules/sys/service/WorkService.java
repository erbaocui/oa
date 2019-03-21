/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.sys.entity.Work;
import com.thinkgem.jeesite.modules.sys.dao.WorkDao;

/**
 * 工作履历Service
 * @author cuijp
 * @version 2019-03-11
 */
@Service
@Transactional(readOnly = true)
public class WorkService extends CrudService<WorkDao, Work> {

	public Work get(String id) {
		return super.get(id);
	}
	
	public List<Work> findList(Work work) {
		return super.findList(work);
	}
	
	public Page<Work> findPage(Page<Work> page, Work work) {
		return super.findPage(page, work);
	}
	
	@Transactional(readOnly = false)
	public void save(Work work) {
		super.save(work);
	}
	
	@Transactional(readOnly = false)
	public void delete(Work work) {
		super.delete(work);
	}
	
}