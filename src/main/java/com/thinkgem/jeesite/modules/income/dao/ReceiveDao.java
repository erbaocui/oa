/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.income.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.income.entity.Receive;

/**
 * 收款DAO接口
 * @author cuijp
 * @version 2018-05-09
 */
@MyBatisDao
public interface ReceiveDao extends CrudDao<Receive> {
	
}