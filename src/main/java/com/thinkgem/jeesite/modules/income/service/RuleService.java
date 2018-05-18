/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.income.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.income.entity.Rule;
import com.thinkgem.jeesite.modules.income.dao.RuleDao;

/**
 * 分配规则Service
 * @author cuijp
 * @version 2018-05-04
 */
@Service
@Transactional(readOnly = true)
public class RuleService extends CrudService<RuleDao, Rule> {

	public Rule get(String id) {
		return super.get(id);
	}
	
	public List<Rule> findList(Rule rule) {
		return super.findList(rule);
	}

	public List<Rule> findListByOfficeId(Rule rule){
		return dao.findAllListByOfficeId(rule);
	}
	
	public Page<Rule> findPage(Page<Rule> page, Rule rule) {
		return super.findPage(page, rule);
	}
	
	@Transactional(readOnly = false)
	public void save(Rule rule) {
		super.save(rule);
	}
	
	@Transactional(readOnly = false)
	public void delete(Rule rule) {
		super.delete(rule);
	}
	
}