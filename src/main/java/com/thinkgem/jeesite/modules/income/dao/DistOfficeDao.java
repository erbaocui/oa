/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.income.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.income.entity.DistOffice;

import java.util.Map;

/**
 * 部门分配DAO接口
 * @author cuijp
 * @version 2018-05-14
 */
@MyBatisDao
public interface DistOfficeDao extends CrudDao<DistOffice> {
    public String findGroupId(Map paramMap);
	
}