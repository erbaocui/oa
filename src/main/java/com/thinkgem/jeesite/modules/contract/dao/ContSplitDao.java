/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.contract.entity.ContSplit;
import com.thinkgem.jeesite.modules.contract.entity.ContSplitDetailOffice;

/**
 * 合同拆分DAO接口
 * @author cuijp
 * @version 2019-05-05
 */
@MyBatisDao
public interface ContSplitDao extends CrudDao<ContSplit> {
    public ContSplit getOne(ContSplit contSplit);
	
}