/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.income.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.income.entity.ApplyPayCont;

import java.util.List;

/**
 * 请款合同DAO接口
 * @author cuijp
 * @version 2018-05-02
 */
@MyBatisDao
public interface ApplyPayContDao extends CrudDao<ApplyPayCont> {


    int insertBatch(List<ApplyPayCont> entity);


}