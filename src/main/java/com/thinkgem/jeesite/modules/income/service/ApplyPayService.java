/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.income.service;

import java.util.List;

import com.thinkgem.jeesite.modules.income.dao.ApplyPayContDao;
import com.thinkgem.jeesite.modules.income.entity.ApplyPayCont;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.income.entity.ApplyPay;
import com.thinkgem.jeesite.modules.income.dao.ApplyPayDao;

/**
 * 请款功能Service
 * @author cuijp
 * @version 2018-05-02
 */
@Service
@Transactional(readOnly = true)
public class ApplyPayService extends CrudService<ApplyPayDao, ApplyPay> {
    @Autowired
	ApplyPayContDao applyPayContDao;

	public ApplyPayContDao getApplyPayContDao() {
		return applyPayContDao;
	}

	public void setApplyPayContDao(ApplyPayContDao applyPayContDao) {
		this.applyPayContDao = applyPayContDao;
	}

	public ApplyPay get(String id) {
		return super.get(id);
	}
	
	public List<ApplyPay> findList(ApplyPay applyPay) {
		return super.findList(applyPay);
	}
	
	public Page<ApplyPay> findPage(Page<ApplyPay> page, ApplyPay applyPay) {
		return super.findPage(page, applyPay);
	}
	
	@Transactional(readOnly = false)
	public void save(ApplyPay applyPay) {
		super.save(applyPay);
		//applyPayContDao.delete(applyPay.getId());
		//applyPayContDao.insertBatch(applyPay.getContractList());
	}
	
	@Transactional(readOnly = false)
	public void delete(ApplyPay applyPay) {
		super.delete(applyPay);
	}

	@Transactional(readOnly = false)
	public void deleteContract(ApplyPayCont applyPayCont) {
		applyPayContDao.delete(applyPayCont);
	}

	@Transactional(readOnly = false)
	public void addContract(ApplyPayCont applyPayCont) {
		applyPayCont.preInsert();
		applyPayContDao.insert(applyPayCont);
	}
}