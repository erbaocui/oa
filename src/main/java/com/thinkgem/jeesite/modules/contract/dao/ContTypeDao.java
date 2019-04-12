/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.contract.entity.ContType;

/**
 * 合同类型DAO接口
 * @author cuijp
 * @version 2019-04-08
 */
@MyBatisDao
public interface ContTypeDao extends CrudDao<ContType> {
	
}