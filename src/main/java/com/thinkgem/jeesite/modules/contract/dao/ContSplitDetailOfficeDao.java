/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.contract.entity.ContSplitDetailOffice;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 合同拆分细化部门DAO接口
 * @author cuijp
 * @version 2019-05-07
 */
@MyBatisDao
public interface ContSplitDetailOfficeDao extends CrudDao<ContSplitDetailOffice> {
    public ContSplitDetailOffice getOne(ContSplitDetailOffice contSplitDetailOffice);
    public BigDecimal detailSum(@Param("detailId")String detailId);
    public List<ContSplitDetailOffice> findListByOfficeId(Map param);
}