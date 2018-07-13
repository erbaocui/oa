/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.contract.entity.ContApply;

import java.util.List;

/**
 * 合同请款DAO接口
 * @author cuijp
 * @version 2018-06-07
 */
@MyBatisDao
public interface ContApplyDao extends CrudDao<ContApply> {
    public ContApply getLast(String contractId);
}