/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.contract.entity.BmTest;
import com.thinkgem.jeesite.modules.contract.dao.BmTestDao;

/**
 * 测试Service
 * @author 测试
 * @version 2018-06-28
 */
@Service
@Transactional(readOnly = true)
public class BmTestService extends CrudService<BmTestDao, BmTest> {

	public BmTest get(String id) {
		return super.get(id);
	}
	
	public List<BmTest> findList(BmTest bmTest) {
		return super.findList(bmTest);
	}
	
	public Page<BmTest> findPage(Page<BmTest> page, BmTest bmTest) {
		return super.findPage(page, bmTest);
	}
	
	@Transactional(readOnly = false)
	public void save(BmTest bmTest) {
		super.save(bmTest);
	}
	
	@Transactional(readOnly = false)
	public void delete(BmTest bmTest) {
		super.delete(bmTest);
	}
	
}