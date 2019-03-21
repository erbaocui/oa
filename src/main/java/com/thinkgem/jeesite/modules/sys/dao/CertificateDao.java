/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.sys.entity.Certificate;

/**
 * 员工证书DAO接口
 * @author cuijp
 * @version 2019-03-14
 */
@MyBatisDao
public interface CertificateDao extends CrudDao<Certificate> {
	
}