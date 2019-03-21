/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.sys.entity.Detail;
import com.thinkgem.jeesite.modules.sys.dao.DetailDao;

/**
 * 员工详细信息Service
 * @author cuijp
 * @version 2019-03-18
 */
@Service
@Transactional(readOnly = true)
public class DetailService extends CrudService<DetailDao, Detail> {

	public Detail get(String id) {
		return super.get(id);
	}
	
	public List<Detail> findList(Detail detail) {
		return super.findList(detail);
	}
	
	public Page<Detail> findPage(Page<Detail> page, Detail detail) {
		return super.findPage(page, detail);
	}
	
	@Transactional(readOnly = false)
	public void save(Detail detail) {
		super.save(detail);
	}
	
	@Transactional(readOnly = false)
	public void delete(Detail detail) {
		super.delete(detail);
	}

	public Detail getByUserId(String userId) {
		return dao.getByUserId(userId);
	}
	
}