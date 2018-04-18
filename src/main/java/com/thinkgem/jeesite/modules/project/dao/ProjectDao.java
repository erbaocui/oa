/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.project.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.oa.entity.Leave;
import com.thinkgem.jeesite.modules.project.entity.Project;
import com.thinkgem.jeesite.modules.sys.entity.Office;

import java.util.List;

@MyBatisDao
public interface ProjectDao extends CrudDao<Project> {
	

	/**
	 * 根据机构查找项目列表
	 * @param project
	 * @return  List<Project>
	 */
	public List<Project> findProjectByOfficeId(Project project);
	
}
