/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.sys.entity.Performance;

/**
 * 员工绩效DAO接口
 * @author cuijp
 * @version 2019-03-13
 */
@MyBatisDao
public interface PerformanceDao extends CrudDao<Performance> {
	
}