/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.sys.entity.Reward;
import com.thinkgem.jeesite.modules.sys.dao.RewardDao;

/**
 * 奖惩Service
 * @author cuijp
 * @version 2019-03-12
 */
@Service
@Transactional(readOnly = true)
public class RewardService extends CrudService<RewardDao, Reward> {

	public Reward get(String id) {
		return super.get(id);
	}
	
	public List<Reward> findList(Reward reward) {
		return super.findList(reward);
	}
	
	public Page<Reward> findPage(Page<Reward> page, Reward reward) {
		return super.findPage(page, reward);
	}
	
	@Transactional(readOnly = false)
	public void save(Reward reward) {
		super.save(reward);
	}
	
	@Transactional(readOnly = false)
	public void delete(Reward reward) {
		super.delete(reward);
	}
	
}