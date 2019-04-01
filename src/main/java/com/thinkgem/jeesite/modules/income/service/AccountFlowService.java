/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.income.service;

import java.util.List;

import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.income.entity.AccountFlow;
import com.thinkgem.jeesite.modules.income.dao.AccountFlowDao;

/**
 * 账户流水Service
 * @author cuijp
 * @version 2018-05-22
 */
@Service
@Transactional(readOnly = true)
public class AccountFlowService extends CrudService<AccountFlowDao, AccountFlow> {

	public AccountFlow get(String id) {
		return super.get(id);
	}
	
	public List<AccountFlow> findList(AccountFlow accountFlow){
		accountFlow.getSqlMap().put("dsf",dataScopeFilter(UserUtils.getUser(), "o", "u"));
	    return super.findList(accountFlow);
	}
	
	public Page<AccountFlow> findPage(Page<AccountFlow> page, AccountFlow accountFlow) {
		accountFlow.getSqlMap().put("dsf",dataScopeFilter(UserUtils.getUser(), "o", "u"));
		return super.findPage(page, accountFlow);
	}
	
	@Transactional(readOnly = false)
	public void save(AccountFlow accountFlow) {
		super.save(accountFlow);
	}
	
	@Transactional(readOnly = false)
	public void delete(AccountFlow accountFlow) {
		super.delete(accountFlow);
	}

	@Transactional(readOnly = false)
	public void saveBatch(List<AccountFlow> accountFlowList){dao.insertBatch( accountFlowList);
	}


}