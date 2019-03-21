/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.entity;

import com.thinkgem.jeesite.modules.sys.entity.User;
import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 设计岗位Entity
 * @author cuijp
 * @version 2019-03-15
 */
public class JobPosition extends DataEntity<JobPosition> {
	
	private static final long serialVersionUID = 1L;
	private User user;		// 用户编号
	private String professional;		// 专业
	private String responsible="0";		// 专业负责人
	private String approval="0";		// 专业审定人
	private String audit="0";		// 审核
	private String proof="0";		// 校对
	private String design="0";		// 设计
	private String draw="0";		// 绘制
	private String remark;		// 备注
	
	public JobPosition() {
		super();
	}

	public JobPosition(String id){
		super(id);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Length(min=0, max=255, message="专业长度必须介于 0 和 255 之间")
	public String getProfessional() {
		return professional;
	}

	public void setProfessional(String professional) {
		this.professional = professional;
	}
	
	@Length(min=0, max=200, message="专业负责人长度必须介于 0 和 200 之间")
	public String getResponsible() {
		return responsible;
	}

	public void setResponsible(String responsible) {
		this.responsible = responsible;
	}
	
	@Length(min=0, max=255, message="专业审定人长度必须介于 0 和 255 之间")
	public String getApproval() {
		return approval;
	}

	public void setApproval(String approval) {
		this.approval = approval;
	}
	
	@Length(min=0, max=255, message="审核长度必须介于 0 和 255 之间")
	public String getAudit() {
		return audit;
	}

	public void setAudit(String audit) {
		this.audit = audit;
	}

	public String getProof() {
		return proof;
	}

	public void setProof(String proof) {
		this.proof = proof;
	}

	@Length(min=0, max=255, message="设计长度必须介于 0 和 255 之间")
	public String getDesign() {
		return design;
	}

	public void setDesign(String design) {
		this.design = design;
	}
	
	@Length(min=0, max=255, message="绘制长度必须介于 0 和 255 之间")
	public String getDraw() {
		return draw;
	}

	public void setDraw(String draw) {
		this.draw = draw;
	}
	
	@Length(min=0, max=255, message="备注长度必须介于 0 和 255 之间")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}