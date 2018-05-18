/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.income.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.income.entity.Rule;
import com.thinkgem.jeesite.modules.income.entity.RuleGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分配规则DAO接口
 * @author cuijp
 * @version 2018-05-04
 */
@MyBatisDao
public interface RuleDao extends CrudDao<Rule> {

    public List<Rule> findAllListByOfficeId(Rule rule);
	
}