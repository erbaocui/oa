/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.entity;

import com.thinkgem.jeesite.modules.sys.entity.User;
import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 员工详细信息Entity
 * @author cuijp
 * @version 2019-03-18
 */
public class Detail extends DataEntity<Detail> {
	
	private static final long serialVersionUID = 1L;
	private User user;		// 用户编号
	private String idCardNo;		// 身份证号
	private String gender;          // 性别
	private String status;          //状态
	private String usedName;		// 曾用名
	private String political;		// 政治面貌
	private Date partyDate;		// 入党时间
	private String marital;		// 婚姻状况
	private String nation;		// 民族
	private String nativePlace;		// 籍贯
	private Date birthdate;		// 出生日期
	private Date companyDate;		// 进入中怡时间
	private Date workDate;		// 开始工作时间
	private String homeAddress;		// 家庭地址
	private String mailAddress;		// 通讯地址
	private String curPosition;     //当前职位
	private Date positionDate;		// 任职日期
	
	public Detail() {
		super();
	}

	public Detail(String id){
		super(id);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Length(min=0, max=255, message="身份证号长度必须介于 0 和 255 之间")
	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}
	
	@Length(min=0, max=64, message="性别长度必须介于 0 和 64 之间")
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Length(min=0, max=255, message="曾用名长度必须介于 0 和 255 之间")
	public String getUsedName() {
		return usedName;
	}

	public void setUsedName(String usedName) {
		this.usedName = usedName;
	}
	
	@Length(min=0, max=100, message="政治面貌长度必须介于 0 和 100 之间")
	public String getPolitical() {
		return political;
	}

	public void setPolitical(String political) {
		this.political = political;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getPartyDate() {
		return partyDate;
	}

	public void setPartyDate(Date partyDate) {
		this.partyDate = partyDate;
	}
	
	@Length(min=0, max=10, message="婚姻状况长度必须介于 0 和 10 之间")
	public String getMarital() {
		return marital;
	}

	public void setMarital(String marital) {
		this.marital = marital;
	}
	
	@Length(min=0, max=64, message="民族长度必须介于 0 和 64 之间")
	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}
	
	@Length(min=0, max=255, message="籍贯长度必须介于 0 和 255 之间")
	public String getNativePlace() {
		return nativePlace;
	}

	public void setNativePlace(String nativePlace) {
		this.nativePlace = nativePlace;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCompanyDate() {
		return companyDate;
	}

	public void setCompanyDate(Date companyDate) {
		this.companyDate = companyDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getWorkDate() {
		return workDate;
	}

	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}
	
	@Length(min=0, max=255, message="家庭地址长度必须介于 0 和 255 之间")
	public String getHomeAddress() {
		return homeAddress;
	}

	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}
	
	@Length(min=0, max=200, message="通讯地址长度必须介于 0 和 200 之间")
	public String getMailAddress() {
		return mailAddress;
	}

	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	public String getCurPosition() {
		return curPosition;
	}

	public void setCurPosition(String curPosition) {
		this.curPosition = curPosition;
	}

	public Date getPositionDate() {
		return positionDate;
	}

	public void setPositionDate(Date positionDate) {
		this.positionDate = positionDate;
	}
}