/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.sys.entity.JobPosition;

/**
 * 设计岗位DAO接口
 * @author cuijp
 * @version 2019-03-15
 */
@MyBatisDao
public interface JobPositionDao extends CrudDao<JobPosition> {
	
}