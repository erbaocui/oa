/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.contract.entity.ContSplitDetailOffice;
import com.thinkgem.jeesite.modules.contract.dao.ContSplitDetailOfficeDao;

/**
 * 合同拆分细化部门Service
 * @author cuijp
 * @version 2019-05-07
 */
@Service
@Transactional(readOnly = true)
public class ContSplitDetailOfficeService extends CrudService<ContSplitDetailOfficeDao, ContSplitDetailOffice> {

	public ContSplitDetailOffice get(String id) {
		return super.get(id);
	}
	
	public List<ContSplitDetailOffice> findList(ContSplitDetailOffice contSplitDetailOffice) {
		return super.findList(contSplitDetailOffice);
	}
	
	public Page<ContSplitDetailOffice> findPage(Page<ContSplitDetailOffice> page, ContSplitDetailOffice contSplitDetailOffice) {
		return super.findPage(page, contSplitDetailOffice);
	}

	public ContSplitDetailOffice getOne(ContSplitDetailOffice contSplitDetailOffice) {
		return dao.getOne(contSplitDetailOffice);
	}

	public BigDecimal detailSum(String detailId) {
		return dao.detailSum(detailId);
	}


	@Transactional(readOnly = false)
	public void save(ContSplitDetailOffice contSplitDetailOffice) {
		super.save(contSplitDetailOffice);
	}
	
	@Transactional(readOnly = false)
	public void delete(ContSplitDetailOffice contSplitDetailOffice) {
		super.delete(contSplitDetailOffice);
	}
	public List<ContSplitDetailOffice> findListByOfficeId(String detailId,String officeId){
		Map<String,String> param=new HashMap<String, String>();
		param.put("detailId",detailId);
		param.put("officeId",officeId);
		return dao.findListByOfficeId(param);
	}




}