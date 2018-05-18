/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.income.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.income.entity.RuleGroup;

import java.util.List;

/**
 * 规则分组DAO接口
 * @author cuijp
 * @version 2018-05-17
 */
@MyBatisDao
public interface RuleGroupDao extends CrudDao<RuleGroup> {
    public List<RuleGroup> findListByOfficeId(String officeId);
}