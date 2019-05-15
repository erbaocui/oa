/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.service;

import java.util.List;

import com.thinkgem.jeesite.modules.contract.entity.ContSplitDetailOffice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.contract.entity.ContSplitDetailItem;
import com.thinkgem.jeesite.modules.contract.dao.ContSplitDetailItemDao;

/**
 * 合同拆分细化分项Service
 * @author cuijp
 * @version 2019-05-07
 */
@Service
@Transactional(readOnly = true)
public class ContSplitDetailItemService extends CrudService<ContSplitDetailItemDao, ContSplitDetailItem> {
   @Autowired
	ContSplitDetailOfficeService contSplitDetailOfficeService;
	public ContSplitDetailItem get(String id) {
		return super.get(id);
	}

	public List<ContSplitDetailItem> findList(ContSplitDetailItem contSplitDetailItem) {
		return super.findList(contSplitDetailItem);
	}

	public Page<ContSplitDetailItem> findPage(Page<ContSplitDetailItem> page, ContSplitDetailItem contSplitDetailItem) {
		return super.findPage(page, contSplitDetailItem);
	}

	@Transactional(readOnly = false)
	public void save(ContSplitDetailItem contSplitDetailItem) {
		super.save(contSplitDetailItem);
	}

	@Transactional(readOnly = false)
	public void delete(ContSplitDetailItem contSplitDetailItem) {
		ContSplitDetailOffice office=new ContSplitDetailOffice();
		office.setItemId(contSplitDetailItem.getId());
		contSplitDetailOfficeService.delete(office);
		super.delete(contSplitDetailItem);

	}

	public ContSplitDetailItem getOne(ContSplitDetailItem contSplitDetailItem) {
		return dao.getOne(contSplitDetailItem);
	}
}