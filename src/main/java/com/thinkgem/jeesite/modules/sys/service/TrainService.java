/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.service;

import java.util.ArrayList;
import java.util.List;

import com.thinkgem.jeesite.modules.sys.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.sys.entity.Train;
import com.thinkgem.jeesite.modules.sys.dao.TrainDao;

/**
 * 培训Service
 * @author cuijp
 * @version 2019-03-13
 */
@Service
@Transactional(readOnly = true)
public class TrainService extends CrudService<TrainDao, Train> {

	public Train get(String id) {
		return super.get(id);
	}
	
	public List<Train> findList(Train train) {
		return super.findList(train);
	}
	
	public Page<Train> findPage(Page<Train> page, Train train) {
		return super.findPage(page, train);
	}
	
	@Transactional(readOnly = false)
	public void save(Train train) {
		super.save(train);
	}
	
	@Transactional(readOnly = false)
	public void delete(Train train) {
		super.delete(train);
	}
	public List<User> findUserByTrainId(Train train){
		return dao.findUserByTrainId(train);
	}

//
//	@Transactional(readOnly = false)
//	public User assignUserToTrain(Train train, User user) {
//		if (user == null){
//			return null;
//		}
//		train
//		List<String> roleIds = dao.get(train)
//		if (roleIds.contains(role.getId())) {
//			return null;
//		}
//		user.getRoleList().add(role);
//		dao.saveTrainUser();
//		return user;
//	}
	@Transactional(readOnly = false)
	public void saveTrainUser(Train train) {
		//dao.deleteTrainUser(train);
		dao.insertTrainUser(train);
	}

	@Transactional(readOnly = false)
	public void outUserInTrain(Train train, User user) {
		List<User> userList= new ArrayList<User>();
		userList.add(user);
		train.setUserList(userList);
		dao.deleteTrainUser(train);


	}
	
}