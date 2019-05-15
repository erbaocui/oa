/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 拆分分项Entity
 * @author cuijp
 * @version 2019-05-05
 */
public class ContSplitItem extends DataEntity<ContSplitItem> {
	
	private static final long serialVersionUID = 1L;
	private String value;		// 收款值
	private String splitId;		// 拆分ID
	private String plan;		// 0不能保存1可以保存
	private String draw;		// 0不能保存1可以保存
	private Integer type;      // 拆分类型
	private String remark;
	public ContSplitItem() {
		super();
	}

	public ContSplitItem(String id){
		super(id);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@Length(min=0, max=255, message="拆分ID长度必须介于 0 和 255 之间")
	public String getSplitId() {
		return splitId;
	}

	public void setSplitId(String splitId) {
		this.splitId = splitId;
	}
	
	@Length(min=0, max=11, message="0不能保存1可以保存长度必须介于 0 和 11 之间")
	public String getPlan() {
		return plan;
	}

	public void setPlan(String plan) {
		this.plan = plan;
	}
	
	@Length(min=0, max=11, message="0不能保存1可以保存长度必须介于 0 和 11 之间")
	public String getDraw() {
		return draw;
	}

	public void setDraw(String draw) {
		this.draw = draw;
	}
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}