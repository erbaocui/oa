/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.sys.entity.Position;
import com.thinkgem.jeesite.modules.sys.dao.PositionDao;

/**
 * 任职历史Service
 * @author cuijp
 * @version 2019-03-13
 */
@Service
@Transactional(readOnly = true)
public class PositionService extends CrudService<PositionDao, Position> {

	public Position get(String id) {
		return super.get(id);
	}
	
	public List<Position> findList(Position position) {
		return super.findList(position);
	}
	
	public Page<Position> findPage(Page<Position> page, Position position) {
		return super.findPage(page, position);
	}
	
	@Transactional(readOnly = false)
	public void save(Position position) {
		super.save(position);
	}
	
	@Transactional(readOnly = false)
	public void delete(Position position) {
		super.delete(position);
	}
	
}