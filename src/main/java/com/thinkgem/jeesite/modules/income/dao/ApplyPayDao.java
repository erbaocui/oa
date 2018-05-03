/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.income.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.income.entity.ApplyPay;

/**
 * 请款功能DAO接口
 * @author cuijp
 * @version 2018-05-02
 */
@MyBatisDao
public interface ApplyPayDao extends CrudDao<ApplyPay> {
	
}