/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.contract.entity.ContSplitDetail;
import com.thinkgem.jeesite.modules.contract.dao.ContSplitDetailDao;

/**
 * 合同拆分细化Service
 * @author cuijp
 * @version 2019-05-07
 */
@Service
@Transactional(readOnly = true)
public class ContSplitDetailService extends CrudService<ContSplitDetailDao, ContSplitDetail> {

	public ContSplitDetail get(String id) {
		return super.get(id);
	}
	
	public List<ContSplitDetail> findList(ContSplitDetail contSplitDetail) {
		return super.findList(contSplitDetail);
	}
	
	public Page<ContSplitDetail> findPage(Page<ContSplitDetail> page, ContSplitDetail contSplitDetail) {
		return super.findPage(page, contSplitDetail);
	}
	
	@Transactional(readOnly = false)
	public void save(ContSplitDetail contSplitDetail) {
		super.save(contSplitDetail);
	}
	
	@Transactional(readOnly = false)
	public void delete(ContSplitDetail contSplitDetail) {
		super.delete(contSplitDetail);
	}

	public  List<String> findChiefLoginNameList(String detailId){
		return dao.findChiefLoginNameList(detailId);
	}


}