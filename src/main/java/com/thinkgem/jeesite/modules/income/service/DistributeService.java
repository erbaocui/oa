/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.income.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.thinkgem.jeesite.common.utils.NumberOperateUtils;
import com.thinkgem.jeesite.modules.contract.dao.ContApplyDao;
import com.thinkgem.jeesite.modules.contract.entity.ContApply;
import com.thinkgem.jeesite.modules.contract.service.ContApplyService;
import com.thinkgem.jeesite.modules.income.constant.IncomeConstant;
import com.thinkgem.jeesite.modules.income.dao.AccountDao;
import com.thinkgem.jeesite.modules.income.dao.AccountFlowDao;
import com.thinkgem.jeesite.modules.income.dao.IncomeDao;
import com.thinkgem.jeesite.modules.income.entity.*;
import com.thinkgem.jeesite.modules.income.vo.RuleItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.income.dao.DistributeDao;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

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
	@Autowired
	private IncomeService incomeService;
	@Autowired
	private ContApplyService contApplyService;

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
	public void saveAcount(String incomeId,String typeId) {
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
			Account a=new Account();
			a.setAccount(d.getAccount());
			accountFlow.setAccount(a);
			//accountFlow.setIncomeId(d.getIncomeId());
			distribute.setTypeId(typeId);
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

		Income income=incomeService.get(incomeId);
		income.setStatus(3);
		incomeService.save(income);
		ContApply contApply=contApplyService.get(income.getApplyId());
		Double incomeValue=0.0;
		Double applyIncomeValue=0.0;
		if(income.getValue()!=null){
			incomeValue=income.getValue().doubleValue();
		}
		if(contApply.getIncome()!=null){
			applyIncomeValue=contApply.getIncome().doubleValue();
		}
		Double incomeSum= NumberOperateUtils.add(incomeValue,applyIncomeValue);
		contApply.setIncome(new BigDecimal(incomeSum));
		if(incomeSum>=contApply.getReceiptValue().doubleValue()){
			contApply.setStatus(4);
		}
		contApplyService.save(contApply);
	}





}