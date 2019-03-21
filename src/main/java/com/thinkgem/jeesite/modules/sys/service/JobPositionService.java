/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.sys.entity.JobPosition;
import com.thinkgem.jeesite.modules.sys.dao.JobPositionDao;

/**
 * 设计岗位Service
 * @author cuijp
 * @version 2019-03-15
 */
@Service
@Transactional(readOnly = true)
public class JobPositionService extends CrudService<JobPositionDao, JobPosition> {

	public JobPosition get(String id) {
		return super.get(id);
	}
	
	public List<JobPosition> findList(JobPosition jobPosition) {
		return super.findList(jobPosition);
	}
	
	public Page<JobPosition> findPage(Page<JobPosition> page, JobPosition jobPosition) {
		return super.findPage(page, jobPosition);
	}
	
	@Transactional(readOnly = false)
	public void save(JobPosition jobPosition) {
		super.save(jobPosition);
	}
	
	@Transactional(readOnly = false)
	public void delete(JobPosition jobPosition) {
		super.delete(jobPosition);
	}
	
}