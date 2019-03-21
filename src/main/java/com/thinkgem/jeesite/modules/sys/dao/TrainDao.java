/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.sys.entity.Train;
import com.thinkgem.jeesite.modules.sys.entity.User;

import java.util.List;

/**
 * 培训DAO接口
 * @author cuijp
 * @version 2019-03-13
 */
@MyBatisDao
public interface TrainDao extends CrudDao<Train> {
    /**
     * 通过TrainId获取用户列表，仅返回用户id和name（树查询用户时用）
     * @param train
     * @return
     */
    public List<User> findUserByTrainId(Train train);

    /**
     * 删除培训用户信息
     * @param train
     * @return
     */
    public void deleteTrainUser(Train train);
    /**
     * 插入培训用户信息
     * @param train
     * @return
     */
    public void insertTrainUser(Train train);
	
}