/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.income.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.income.entity.RuleGroup;
import com.thinkgem.jeesite.modules.income.dao.RuleGroupDao;

/**
 * 规则分组Service
 * @author cuijp
 * @version 2018-05-17
 */
@Service
@Transactional(readOnly = true)
public class RuleGroupService extends CrudService<RuleGroupDao, RuleGroup> {

	public RuleGroup get(String id) {
		return super.get(id);
	}
	
	public List<RuleGroup> findList(RuleGroup ruleGroup) {
		return super.findList(ruleGroup);
	}

	public List<RuleGroup> findListByOfficeId(String officeId,String type) {
		 Map<String,String> paramMap=new HashMap<String,String>();
		paramMap.put("officeId",officeId);
		if(type!=null&&!type.equals("")) {
			paramMap.put("type", type);
		}
		return dao.findListByOfficeId(paramMap);
	}
//	public List<RuleGroup> findListByOfficeId(String officeId) {
//		Map<String,String> paramMap=new HashMap<String,String>();
//		paramMap.put("officeId",officeId);
//		return dao.findListByOfficeId(paramMap);
//	}
	public Page<RuleGroup> findPage(Page<RuleGroup> page, RuleGroup ruleGroup) {
		return super.findPage(page, ruleGroup);
	}
	
	@Transactional(readOnly = false)
	public void save(RuleGroup ruleGroup) {
		super.save(ruleGroup);
	}
	
	@Transactional(readOnly = false)
	public void delete(RuleGroup ruleGroup) {
		super.delete(ruleGroup);
	}
	
}