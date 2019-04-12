/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 合同类型Entity
 * @author cuijp
 * @version 2019-04-08
 */
public class ContType extends DataEntity<ContType> {
	
	private static final long serialVersionUID = 1L;
	private String typeId;		// type_id
	private String contractId;		// contract_id
	
	public ContType() {
		super();
	}

	public ContType(String id){
		super(id);
	}

	@Length(min=0, max=255, message="type_id长度必须介于 0 和 255 之间")
	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
}