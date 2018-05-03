/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.income.entity;

import com.thinkgem.jeesite.modules.contract.entity.Contract;
import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 请款合同Entity
 * @author cuijp
 * @version 2018-05-02
 */
public class ApplyPayCont extends DataEntity<ApplyPayCont> {
	
	private static final long serialVersionUID = 1L;
	private String  applyPayId;		// apply_pay_id
	private String contractId;		// contract_id
	
	public ApplyPayCont() {
		super();
	}

	public String getApplyPayId() {
		return applyPayId;
	}

	public void setApplyPayId(String applyPayId) {
		this.applyPayId = applyPayId;
	}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
}