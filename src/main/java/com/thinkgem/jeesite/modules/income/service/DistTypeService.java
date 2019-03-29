/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.income.service;

import java.util.List;

import com.thinkgem.jeesite.modules.income.dao.DistOfficeDao;
import com.thinkgem.jeesite.modules.income.dao.DistributeDao;
import com.thinkgem.jeesite.modules.income.dao.IncomeDao;
import com.thinkgem.jeesite.modules.income.entity.DistOffice;
import com.thinkgem.jeesite.modules.income.entity.Distribute;
import com.thinkgem.jeesite.modules.income.entity.Income;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.income.entity.DistType;
import com.thinkgem.jeesite.modules.income.dao.DistTypeDao;

/**
 * 进款分配类型Service
 * @author cuijp
 * @version 2019-03-26
 */
@Service
@Transactional(readOnly = true)
public class DistTypeService extends CrudService<DistTypeDao, DistType> {

	@Autowired
	DistributeDao distributeDao;
	@Autowired
	DistOfficeDao distOfficeDao;
	@Autowired
	IncomeService incomeService;
	public DistType get(String id) {
		return super.get(id);
	}
	
	public List<DistType> findList(DistType distType) {
		return super.findList(distType);
	}
	
	public Page<DistType> findPage(Page<DistType> page, DistType distType) {
		return super.findPage(page, distType);
	}
	
	@Transactional(readOnly = false)
	public void save(DistType distType) {
		super.save(distType);
	}
	
	@Transactional(readOnly = false)
	public void delete(DistType distType) {
		super.delete(distType);
		DistOffice distOffice=new DistOffice();
		distOffice.setTypeId(distType.getId());
		distOfficeDao.delete(distOffice);
		Distribute distribute=new Distribute();
		distribute.setTypeId(distType.getId());
		distributeDao.delete(distribute);
		if(distType.getType().equals("1")){
			Income income=incomeService.get(distType.getIncomeId());
			income.setDraw(0);
			incomeService.save(income);
		}
		if(distType.getType().equals("2")){
			Income income=incomeService.get(distType.getIncomeId());
			income.setPlan(0);
			incomeService.save(income);
		}
	}

	public List<String> getDistOfficeIdList(DistType distType) {
		return dao.getDistOfficeIdList(distType);
	}


}