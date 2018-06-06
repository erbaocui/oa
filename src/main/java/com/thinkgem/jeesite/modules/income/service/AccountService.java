/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.income.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.income.entity.Account;
import com.thinkgem.jeesite.modules.income.dao.AccountDao;

/**
 * 账户Service
 * @author cuijp
 * @version 2018-05-22
 */
@Service
@Transactional(readOnly = true)
public class AccountService extends CrudService<AccountDao, Account> {

	public Account get(String id) {
		return super.get(id);
	}
	
	public List<Account> findList(Account account) {
		return super.findList(account);
	}
	
	public Page<Account> findPage(Page<Account> page, Account account) {
		return super.findPage(page, account);
	}
	
	@Transactional(readOnly = false)
	public void save(Account account) {
		super.save(account);
	}
	
	@Transactional(readOnly = false)
	public void delete(Account account) {
		super.delete(account);
	}
	
}