/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.income.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.income.entity.RuleItem;
import com.thinkgem.jeesite.modules.income.dao.RuleItemDao;

/**
 * 分配规则细则Service
 * @author cuijp
 * @version 2018-05-04
 */
@Service
@Transactional(readOnly = true)
public class RuleItemService extends CrudService<RuleItemDao, RuleItem> {

	public RuleItem get(String id) {
		return super.get(id);
	}
	
	public List<RuleItem> findList(RuleItem ruleItem) {
		return super.findList(ruleItem);
	}
	
	public Page<RuleItem> findPage(Page<RuleItem> page, RuleItem ruleItem) {
		return super.findPage(page, ruleItem);
	}
	
	@Transactional(readOnly = false)
	public void save(RuleItem ruleItem) {
		super.save(ruleItem);
	}
	
	@Transactional(readOnly = false)
	public void delete(RuleItem ruleItem) {
		super.delete(ruleItem);
	}
	
}