/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.contract.entity.ContSplitDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 合同拆分细化DAO接口
 * @author cuijp
 * @version 2019-05-07
 */
@MyBatisDao
public interface ContSplitDetailDao extends CrudDao<ContSplitDetail> {
    public List<String> findChiefLoginNameList(@Param("detailId")String detailId);
	
}