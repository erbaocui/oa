/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.income.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.income.entity.RuleItem;

/**
 * 分配规则细则DAO接口
 * @author cuijp
 * @version 2018-05-04
 */
@MyBatisDao
public interface RuleItemDao extends CrudDao<RuleItem> {
	
}