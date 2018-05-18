package com.thinkgem.jeesite.modules.income.vo;

import java.util.List;

/**
 * Created by USER on 2018/5/17.
 */
public class RuleVo {
    private String value;
    private Integer rowspan;
    private List<RuleItemVo> roleItems;

    public RuleVo() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getRowspan() {
        return rowspan;
    }

    public void setRowspan(Integer rowspan) {
        this.rowspan = rowspan;
    }

    public List<RuleItemVo> getRoleItems() {
        return roleItems;
    }

    public void setRoleItems(List<RuleItemVo> roleItems) {
        this.roleItems = roleItems;
    }
}
