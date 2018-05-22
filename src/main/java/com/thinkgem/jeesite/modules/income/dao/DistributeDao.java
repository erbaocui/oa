/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.income.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.income.entity.Distribute;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 分配DAO接口
 * @author cuijp
 * @version 2018-05-21
 */
@MyBatisDao
public interface DistributeDao extends CrudDao<Distribute> {
    public int insertBatch(@Param("distributeList")List<Distribute> distributeList);

    public String findGroupId(Map paramMap);
    public List<Distribute> findDistAccountSum(Distribute distribute);
}