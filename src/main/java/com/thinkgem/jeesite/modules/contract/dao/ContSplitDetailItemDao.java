/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.contract.entity.ContSplitDetailItem;

/**
 * 合同拆分细化分项DAO接口
 * @author cuijp
 * @version 2019-05-07
 */
@MyBatisDao
public interface ContSplitDetailItemDao extends CrudDao<ContSplitDetailItem> {
    public ContSplitDetailItem getOne(ContSplitDetailItem contSplitDetailItem);


}