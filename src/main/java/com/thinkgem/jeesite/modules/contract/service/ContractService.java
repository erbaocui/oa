/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.modules.contract.dao.ContractDao;
import com.thinkgem.jeesite.modules.contract.entity.Contract;
import com.thinkgem.jeesite.modules.contract.entity.QueryContract;
import com.thinkgem.jeesite.modules.project.dao.ProjectDao;
import com.thinkgem.jeesite.modules.project.entity.Project;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ContractService extends BaseService implements InitializingBean {
	


	@Autowired
	private ContractDao contractDao;
	/**
	 * 通过部门ID获取项目列表，仅返回用户id和name（树查询用户时用）
	 * @param officeId
	 * @return
	 */


	public Page<Contract> findContract(Page<Contract> page, QueryContract queryContract ) {
/*		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		user.getSqlMap().put("dsf", dataScopeFilter(user.getCurrentUser(), "o", "a"));*/
		// 设置分页参数
		queryContract.setPage(page);
		// 执行分页查询
		page.setList(contractDao.findListByQueryObject(queryContract));
		return page;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}
}
