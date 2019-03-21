/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.entity;

import com.thinkgem.jeesite.modules.sys.entity.User;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 学习履历Entity
 * @author cuijp
 * @version 2019-02-27
 */
public class Study extends DataEntity<Study> {
	
	private static final long serialVersionUID = 1L;
	private User user;		// 用户编码
	private Date beginDate;		// 开始时间
	private Date endDate;		// 结束时间
	private String school;		// 学校
	private String major;		// 专业
	private String graduate;		// 毕业情况
	private String education;		// 学历
	private String degree;		// 学位
	private String first;		// 第一学历
	private String highest;		// 最高学历
	private String year;		// 学制（年）
	
	public Study() {
		super();
	}

	public Study(String id){
		super(id);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	@Length(min=0, max=200, message="学校长度必须介于 0 和 200 之间")
	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}
	
	@Length(min=0, max=255, message="专业长度必须介于 0 和 255 之间")
	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}
	
	@Length(min=0, max=255, message="毕业情况长度必须介于 0 和 255 之间")
	public String getGraduate() {
		return graduate;
	}

	public void setGraduate(String graduate) {
		this.graduate = graduate;
	}
	
	@Length(min=0, max=255, message="学历长度必须介于 0 和 255 之间")
	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}
	
	@Length(min=0, max=255, message="学位长度必须介于 0 和 255 之间")
	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}
	
	@Length(min=0, max=64, message="第一学历长度必须介于 0 和 64 之间")
	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = first;
	}
	
	@Length(min=0, max=64, message="最高学历长度必须介于 0 和 64 之间")
	public String getHighest() {
		return highest;
	}

	public void setHighest(String highest) {
		this.highest = highest;
	}
	
	@Length(min=0, max=11, message="学制（年）长度必须介于 0 和 11 之间")
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}
	
}