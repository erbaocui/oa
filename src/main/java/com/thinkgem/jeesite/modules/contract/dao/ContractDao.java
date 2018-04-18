/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.contract.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.contract.entity.Contract;
import com.thinkgem.jeesite.modules.contract.entity.QueryContract;
import com.thinkgem.jeesite.modules.project.entity.Project;

import java.util.List;

@MyBatisDao
public interface ContractDao extends CrudDao<Contract> {

	/**
	 *
	 * @param project
	 * @return  List<Project>
	 */
	public List<Contract> findListByQueryObject(QueryContract queryContract );
	
}
