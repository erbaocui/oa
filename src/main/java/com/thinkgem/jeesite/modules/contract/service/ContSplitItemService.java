/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.service;

import java.util.List;

import com.thinkgem.jeesite.modules.contract.constant.ContConstant;
import com.thinkgem.jeesite.modules.contract.entity.ContSplit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.contract.entity.ContSplitItem;
import com.thinkgem.jeesite.modules.contract.dao.ContSplitItemDao;

/**
 * 拆分分项Service
 * @author cuijp
 * @version 2019-05-05
 */
@Service
@Transactional(readOnly = true)
public class ContSplitItemService extends CrudService<ContSplitItemDao, ContSplitItem> {

	@Autowired
	 ContSplitService contSplitService;

	public ContSplitItem get(String id) {
		return super.get(id);
	}
	
	public List<ContSplitItem> findList(ContSplitItem contSplitItem) {
		return super.findList(contSplitItem);
	}
	
	public Page<ContSplitItem> findPage(Page<ContSplitItem> page, ContSplitItem contSplitItem) {
		return super.findPage(page, contSplitItem);
	}
	
	@Transactional(readOnly = false)
	public void save(ContSplitItem contSplitItem) {
		super.save(contSplitItem);
		ContSplit contSplit=contSplitService.get(contSplitItem.getSplitId());
		if(contSplitItem.getType().equals(ContConstant.SplitType.DRAW.getValue())){
			contSplit.setDraw(1);
			contSplitService.save(contSplit);
		}
		if(contSplitItem.getType().equals(ContConstant.SplitType.PLAN.getValue())){
			contSplit.setPlan(1);
			contSplitService.save(contSplit);
		}
		contSplitService.save(contSplit);

	}
	
	@Transactional(readOnly = false)
	public void delete(ContSplitItem contSplitItem) {
		contSplitItem=this.get(contSplitItem.getId());
		ContSplit contSplit=contSplitService.get(contSplitItem.getSplitId());
		super.delete(contSplitItem);
		if(contSplitItem.getType().equals(ContConstant.SplitType.DRAW.getValue())){
			contSplit.setDraw(0);
			contSplitService.save(contSplit);
		}
		if(contSplitItem.getType().equals(ContConstant.SplitType.PLAN.getValue())){
			contSplit.setPlan(0);
			contSplitService.save(contSplit);
		}
		contSplitService.save(contSplit);
	}
	
}