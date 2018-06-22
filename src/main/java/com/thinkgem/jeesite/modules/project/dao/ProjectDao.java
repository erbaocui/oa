/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.project.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.project.entity.Project;

/**
 * 项目DAO接口
 * @author cuijp
 * @version 2018-06-21
 */
@MyBatisDao
public interface ProjectDao extends CrudDao<Project> {
	
}