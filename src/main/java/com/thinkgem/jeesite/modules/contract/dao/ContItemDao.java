/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.contract.entity.ContItem;

/**
 * 合同付款约定DAO接口
 * @author cuijp
 * @version 2018-07-01
 */
@MyBatisDao
public interface ContItemDao extends CrudDao<ContItem> {
	
}