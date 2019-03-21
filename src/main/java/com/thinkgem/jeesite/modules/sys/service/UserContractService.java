/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.service;

import java.util.List;

import com.thinkgem.jeesite.modules.sys.entity.UserContract;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.sys.dao.UserContractDao;

/**
 * 员工合同Service
 * @author cuijp
 * @version 2019-03-13
 */
@Service
@Transactional(readOnly = true)
public class UserContractService extends CrudService<UserContractDao, UserContract> {

	public UserContract get(String id) {
		return super.get(id);
	}
	
	public List<UserContract> findList(UserContract userContract) {
		return super.findList(userContract);
	}
	
	public Page<UserContract> findPage(Page<UserContract> page, UserContract userContract) {
		return super.findPage(page, userContract);
	}
	
	@Transactional(readOnly = false)
	public void save(UserContract userContract) {
		super.save(userContract);
	}
	
	@Transactional(readOnly = false)
	public void delete(UserContract userContract) {
		super.delete(userContract);
	}
	
}