/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.contract.dao.ContDao;
import com.thinkgem.jeesite.modules.contract.dao.ContTypeDao;
import com.thinkgem.jeesite.modules.contract.entity.ContType;
import com.thinkgem.jeesite.modules.contract.entity.Contract;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 合同管理Service
 * @author cuijp
 * @version 2018-04-19
 */
@Service
@Transactional(readOnly = true)
public class ContService extends CrudService<ContDao, Contract> {
	@Autowired
	ContTypeDao contTypeDao;

	public Contract get(String id) {
		Contract contract=new Contract();
		contract.setId(id);
		return super.get(contract);
	}
	
	public List<Contract> findList(Contract contract) {
		return super.findList(contract);
	}
	
	public Page<Contract> findPage(Page<Contract> page, Contract contract) {
		System.out.println(dataScopeFilter(UserUtils.getUser(), "o", "c"));
		contract.getSqlMap().put("dsf",dataScopeFilter(UserUtils.getUser(), "o", "c"));
		return super.findPage(page, contract);
	}
	
	@Transactional(readOnly = false)
	public void save(Contract contract) {
		super.save(contract);
		List<String> list=contract.getTypeIdList();
		ContType contType=new ContType();
		contType.setContractId(contract.getId());
		contTypeDao.delete(contType);
		for(String typeId:list){
			contType=new ContType();
			contType.preInsert();
			contType.setContractId(contract.getId());
			contType.setTypeId( typeId);
			contTypeDao.insert(contType);
		}

	}
	
	@Transactional(readOnly = false)
	public void delete(Contract contract) {
		super.delete(contract);
	}
	
}