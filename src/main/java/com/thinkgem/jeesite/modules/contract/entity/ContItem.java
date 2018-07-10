/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 合同付款约定Entity
 * @author cuijp
 * @version 2018-07-01
 */
public class ContItem extends DataEntity<ContItem> {
	
	private static final long serialVersionUID = 1L;
	private String content;		// content
	private String contractId;		// contract_id
	
	public ContItem() {
		super();
	}

	public ContItem(String id){
		super(id);
	}

	@Length(min=0, max=255, message="content长度必须介于 0 和 255 之间")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Length(min=0, max=255, message="contract_id长度必须介于 0 和 255 之间")
	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	
}