/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.sys.entity.Performance;
import com.thinkgem.jeesite.modules.sys.dao.PerformanceDao;

/**
 * 员工绩效Service
 * @author cuijp
 * @version 2019-03-13
 */
@Service
@Transactional(readOnly = true)
public class PerformanceService extends CrudService<PerformanceDao, Performance> {

	public Performance get(String id) {
		return super.get(id);
	}
	
	public List<Performance> findList(Performance performance) {
		return super.findList(performance);
	}
	
	public Page<Performance> findPage(Page<Performance> page, Performance performance) {
		return super.findPage(page, performance);
	}
	
	@Transactional(readOnly = false)
	public void save(Performance performance) {
		super.save(performance);
	}
	
	@Transactional(readOnly = false)
	public void delete(Performance performance) {
		super.delete(performance);
	}
	
}