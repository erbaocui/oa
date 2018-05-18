/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.income.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.income.entity.Income;
import com.thinkgem.jeesite.modules.income.dao.IncomeDao;

/**
 * 收款Service
 * @author cuijp
 * @version 2018-05-11
 */
@Service
@Transactional(readOnly = true)
public class IncomeService extends CrudService<IncomeDao, Income> {

	public Income get(String id) {
		return super.get(id);
	}
	
	public List<Income> findList(Income income) {
		return super.findList(income);
	}
	
	public Page<Income> findPage(Page<Income> page, Income income) {
		return super.findPage(page, income);
	}
	
	@Transactional(readOnly = false)
	public void save(Income income) {
		super.save(income);
	}
	
	@Transactional(readOnly = false)
	public void delete(Income income) {
		super.delete(income);
	}
	
}