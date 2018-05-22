/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.income.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.thinkgem.jeesite.modules.income.dao.AccountDao;
import com.thinkgem.jeesite.modules.income.dao.AccountFlowDao;
import com.thinkgem.jeesite.modules.income.entity.Account;
import com.thinkgem.jeesite.modules.income.entity.AccountFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.income.entity.Distribute;
import com.thinkgem.jeesite.modules.income.dao.DistributeDao;

/**
 * 分配Service
 * @author cuijp
 * @version 2018-05-21
 */
@Service
@Transactional(readOnly = true)
public class DistributeService extends CrudService<DistributeDao, Distribute> {
	@Autowired
	private AccountDao accountDao;
	@Autowired
	private AccountFlowDao accountFlowDao;

	public Distribute get(String id) {
		return super.get(id);
	}
	
	public List<Distribute> findList(Distribute distribute) {
		return super.findList(distribute);
	}
	
	public Page<Distribute> findPage(Page<Distribute> page, Distribute distribute) {
		return super.findPage(page, distribute);
	}
	
	@Transactional(readOnly = false)
	public void save(Distribute distribute) {
		super.save(distribute);
	}
	@Transactional(readOnly = false)
	public void addBatch(List<Distribute> distributeList) {
		dao.insertBatch(distributeList);
	}
	
	@Transactional(readOnly = false)
	public void delete(Distribute distribute) {
		super.delete(distribute);
	}

	public  String findGroupId(Map paramMap){return dao.findGroupId( paramMap);}


	@Transactional(readOnly = false)
	public void saveAcount(String incomeId) {
		Distribute distribute=new Distribute();
		distribute.setIncomeId(incomeId);
		List<Distribute>  distributes=dao.findDistAccountSum(distribute);
		List<AccountFlow> accountFlows=new ArrayList<AccountFlow>();
		List<Account> accounts=new ArrayList<Account>();
        for(Distribute d:distributes){
			AccountFlow accountFlow=new AccountFlow();
			accountFlow.preInsert();
			accountFlow.setValue(d.getValue());
			accountFlow.setType(1);
			accountFlow.setAccount(d.getAccount());
			accountFlow.setIncomeId(d.getIncomeId());
			accountFlows.add(accountFlow);

			Account account=new Account();
			account.setAccount(d.getAccount());
			account.setValue(d.getValue());
			accounts.add(account);
		}

		accountFlowDao.insertBatch(accountFlows);
		for(Account a:accounts){
			accountDao.updateAdd( a);
		}
		distribute.setStatus(2);
		dao.update(distribute);
	}



}