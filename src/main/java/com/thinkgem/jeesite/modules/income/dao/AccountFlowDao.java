/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.income.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.income.entity.AccountFlow;
import com.thinkgem.jeesite.modules.income.entity.Distribute;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 账户流水DAO接口
 * @author cuijp
 * @version 2018-05-22
 */
@MyBatisDao
public interface AccountFlowDao extends CrudDao<AccountFlow> {
    public int insertBatch(@Param("accountFlowList")List<AccountFlow> accountFlowList);
}