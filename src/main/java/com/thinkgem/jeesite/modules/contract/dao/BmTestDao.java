/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.contract.entity.BmTest;

/**
 * 测试DAO接口
 * @author 测试
 * @version 2018-06-28
 */
@MyBatisDao
public interface BmTestDao extends CrudDao<BmTest> {
	
}