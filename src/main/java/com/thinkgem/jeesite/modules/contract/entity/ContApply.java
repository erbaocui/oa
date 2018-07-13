/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.entity;

import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 合同请款Entity
 * @author cuijp
 * @version 2018-07-11
 */
public class ContApply extends DataEntity<ContApply> {
	
	private static final long serialVersionUID = 1L;
	private String remark;		// remark
	private String url;		// url
	private Contract Contract;		// contract_id
	private String path;		// path
	private String fileName;		// file
	private String firstParty;		// first_party
	private Integer status;		// status
	private BigDecimal receiptValue;		// receipt_value
	private String receiptName;		// receipt_name
	private String receiptAddress;		// receipt_address
	private String receiptPhone;		// receipt_phone
	private Date receiptDate;		// receipt_date
	private String receiptBank;		// receipt_bank
	private String receiptAccount;		// receipt_account
	private String taxId;		// tax_id
	private String receiptContent;		// receipt_content
	private String receiptRemark;
	private String procInsId;

	
	public ContApply() {
		super();
	}

	public ContApply(String id){
		super(id);
	}

	@Length(min=0, max=255, message="remark长度必须介于 0 和 255 之间")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Length(min=0, max=255, message="url长度必须介于 0 和 255 之间")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public com.thinkgem.jeesite.modules.contract.entity.Contract getContract() {
		return Contract;
	}

	public void setContract(com.thinkgem.jeesite.modules.contract.entity.Contract contract) {
		Contract = contract;
	}

	@Length(min=0, max=255, message="path长度必须介于 0 和 255 之间")
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Length(min=0, max=255, message="first_party长度必须介于 0 和 255 之间")
	public String getFirstParty() {
		return firstParty;
	}

	public void setFirstParty(String firstParty) {
		this.firstParty = firstParty;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public BigDecimal getReceiptValue() {
		return receiptValue;
	}

	public void setReceiptValue(BigDecimal receiptValue) {
		this.receiptValue = receiptValue;
	}

	@Length(min=0, max=255, message="receipt_name长度必须介于 0 和 255 之间")
	public String getReceiptName() {
		return receiptName;
	}

	public void setReceiptName(String receiptName) {
		this.receiptName = receiptName;
	}
	
	@Length(min=1, max=255, message="receipt_address长度必须介于 1 和 255 之间")
	public String getReceiptAddress() {
		return receiptAddress;
	}

	public void setReceiptAddress(String receiptAddress) {
		this.receiptAddress = receiptAddress;
	}
	
	@Length(min=0, max=255, message="receipt_phone长度必须介于 0 和 255 之间")
	public String getReceiptPhone() {
		return receiptPhone;
	}

	public void setReceiptPhone(String receiptPhone) {
		this.receiptPhone = receiptPhone;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getReceiptDate() {
		return receiptDate;
	}

	public void setReceiptDate(Date receiptDate) {
		this.receiptDate = receiptDate;
	}
	
	@Length(min=0, max=255, message="receipt_bank长度必须介于 0 和 255 之间")
	public String getReceiptBank() {
		return receiptBank;
	}

	public void setReceiptBank(String receiptBank) {
		this.receiptBank = receiptBank;
	}
	
	@Length(min=0, max=255, message="receipt_account长度必须介于 0 和 255 之间")
	public String getReceiptAccount() {
		return receiptAccount;
	}

	public void setReceiptAccount(String receiptAccount) {
		this.receiptAccount = receiptAccount;
	}
	
	@Length(min=0, max=255, message="tax_id长度必须介于 0 和 255 之间")
	public String getTaxId() {
		return taxId;
	}

	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}
	
	@Length(min=0, max=255, message="receipt_content长度必须介于 0 和 255 之间")
	public String getReceiptContent() {
		return receiptContent;
	}

	public void setReceiptContent(String receiptContent) {
		this.receiptContent = receiptContent;
	}

	public String getReceiptRemark() {
		return receiptRemark;
	}

	public void setReceiptRemark(String receiptRemark) {
		this.receiptRemark = receiptRemark;
	}

	public String getProcInsId() {
		return procInsId;
	}

	public void setProcInsId(String procInsId) {
		this.procInsId = procInsId;
	}
}