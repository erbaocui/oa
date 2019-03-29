/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.income.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.income.entity.DistType;

import java.util.List;

/**
 * 进款分配类型DAO接口
 * @author cuijp
 * @version 2019-03-26
 */
@MyBatisDao
public interface DistTypeDao extends CrudDao<DistType> {
    List<String> getDistOfficeIdList(DistType distType);
	
}