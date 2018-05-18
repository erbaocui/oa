/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.income.entity;

import com.thinkgem.jeesite.modules.contract.entity.Contract;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.List;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 请款功能Entity
 * @author cuijp
 * @version 2018-05-02
 */
public class Apply extends DataEntity<Apply> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 请款名称
	private BigDecimal applyValue;		// 请款金额
	private String firstParty;		// 甲方名称
	private BigDecimal enterValue;		// 到账金额
	private Integer status;		// 1申请开票;2已开票；3全部入账

	private Contract contract;
	private List<Income> incomeList;

	
	public Apply() {
		super();
	}

	public Apply(String id){
		super(id);
	}

	@Length(min=0, max=255, message="请款名称长度必须介于 0 和 255 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

	@Length(min=0, max=255, message="甲方名称长度必须介于 0 和 255 之间")
	public String getFirstParty() {
		return firstParty;
	}

	public void setFirstParty(String firstParty) {
		this.firstParty = firstParty;
	}

	/*public List<Contract> getContractList() {
		return contractList;
	}

	public void setContractList(List<Contract> contractList) {
		this.contractList = contractList;
	}*/

	public BigDecimal getApplyValue() {
		return applyValue;
	}

	public void setApplyValue(BigDecimal applyValue) {
		this.applyValue = applyValue;
	}

	public BigDecimal getEnterValue() {
		return enterValue;
	}

	public void setEnterValue(BigDecimal enterValue) {
		this.enterValue = enterValue;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	public List<Income> getIncomeList() {
		return incomeList;
	}

	public void setIncomeList(List<Income> incomeList) {
		this.incomeList = incomeList;
	}
}