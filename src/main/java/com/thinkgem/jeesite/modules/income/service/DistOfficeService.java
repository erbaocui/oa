/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.income.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.income.entity.DistOffice;
import com.thinkgem.jeesite.modules.income.dao.DistOfficeDao;

/**
 * 部门分配Service
 * @author cuijp
 * @version 2018-05-14
 */
@Service
@Transactional(readOnly = true)
public class DistOfficeService extends CrudService<DistOfficeDao, DistOffice> {

	public DistOffice get(String id) {
		return super.get(id);
	}
	
	public List<DistOffice> findList(DistOffice distOffice) {
		return super.findList(distOffice);
	}

	
	public Page<DistOffice> findPage(Page<DistOffice> page, DistOffice distributeOffice) {
		return super.findPage(page, distributeOffice);
	}
	
	@Transactional(readOnly = false)
	public void save(DistOffice distributeOffice) {
		super.save(distributeOffice);
	}
	
	@Transactional(readOnly = false)
	public void delete(DistOffice distributeOffice) {
		super.delete(distributeOffice);
	}

	public  String findGroupId(Map paramMap){return dao.findGroupId( paramMap);}

	public  List<String> findChiefLoginNameList(String incomeId){
		return dao.findChiefLoginNameList(incomeId);
	}
}