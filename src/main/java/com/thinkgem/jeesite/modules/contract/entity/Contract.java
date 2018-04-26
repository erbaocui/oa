/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.project.entity.Project;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 合同管理Entity
 * @author cuijp
 * @version 2018-04-19
 */
public class Contract extends DataEntity<Contract> {
	
	private static final long serialVersionUID = 1L;
	private String code;		// code
	private Project project;		// project_id
	private String name;		// name
	private Date createTime;		// create_time
	private String creatorId;		// creator_id
	private User manager;		// manager_id
	private BigDecimal value;		// value
	private BigDecimal income;		// income
	private Integer type;		// type
	private Integer status;		// status
	private Date signedTime;		// signed_time
	private Date beginTime;		// begin_time
	private Date endTime;		// end_time
	private String contact;		// contact
	private String contactPhone;		// contact_phone
	private String remark;		// remark
	private Integer isSub;		// is_sub
	private String blueprintNum;		// blueprint_num
	private String payment;		// payment
	private Date sealTime;		// seal_time
	private Date recordTime;		// record_time
	private Date returnTime;		// return_time
	private Date returnFinancialTime;		// return_financial_time
	private String subItem;		// sub_item
	private String timeLimit;		// time_limit
	private BigDecimal price;		// price
	private BigDecimal area;		// area
	private BigDecimal investment;		// investment
	private BigDecimal progress;		// progress
	private Office office;		// office_id
	private String procInsId;
	private Integer  procStatus;

	public Contract() {
		super();
	}

	public Contract(String id){
		super(id);
	}

	@Length(min=0, max=200, message="code长度必须介于 0 和 200 之间")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@Length(min=0, max=200, message="name长度必须介于 0 和 200 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Length(min=0, max=32, message="creator_id长度必须介于 0 和 32 之间")
	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public User getManager() {
		return manager;
	}

	public void setManager(User manager) {
		this.manager = manager;
	}


	

	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getSignedTime() {
		return signedTime;
	}

	public void setSignedTime(Date signedTime) {
		this.signedTime = signedTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	@Length(min=0, max=32, message="contact长度必须介于 0 和 32 之间")
	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}
	
	@Length(min=0, max=32, message="contact_phone长度必须介于 0 和 32 之间")
	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	
	@Length(min=0, max=200, message="remark长度必须介于 0 和 200 之间")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	

	@Length(min=0, max=200, message="blueprint_num长度必须介于 0 和 200 之间")
	public String getBlueprintNum() {
		return blueprintNum;
	}

	public void setBlueprintNum(String blueprintNum) {
		this.blueprintNum = blueprintNum;
	}
	
	@Length(min=0, max=200, message="payment长度必须介于 0 和 200 之间")
	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getSealTime() {
		return sealTime;
	}

	public void setSealTime(Date sealTime) {
		this.sealTime = sealTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getReturnTime() {
		return returnTime;
	}

	public void setReturnTime(Date returnTime) {
		this.returnTime = returnTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getReturnFinancialTime() {
		return returnFinancialTime;
	}

	public void setReturnFinancialTime(Date returnFinancialTime) {
		this.returnFinancialTime = returnFinancialTime;
	}
	
	@Length(min=0, max=200, message="sub_item长度必须介于 0 和 200 之间")
	public String getSubItem() {
		return subItem;
	}

	public void setSubItem(String subItem) {
		this.subItem = subItem;
	}
	
	@Length(min=0, max=200, message="time_limit长度必须介于 0 和 200 之间")
	public String getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(String timeLimit) {
		this.timeLimit = timeLimit;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getArea() {
		return area;
	}

	public void setArea(BigDecimal area) {
		this.area = area;
	}

	public BigDecimal getInvestment() {
		return investment;
	}

	public void setInvestment(BigDecimal investment) {
		this.investment = investment;
	}

	public BigDecimal getProgress() {
		return progress;
	}

	public void setProgress(BigDecimal progress) {
		this.progress = progress;
	}

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public BigDecimal getIncome() {
		return income;
	}

	public void setIncome(BigDecimal income) {
		this.income = income;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getIsSub() {
		return isSub;
	}

	public void setIsSub(Integer isSub) {
		this.isSub = isSub;
	}


}