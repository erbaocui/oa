package com.thinkgem.jeesite.modules.income.vo;

import java.util.List;

/**
 * Created by USER on 2018/5/17.
 */
public class RuleItemVo {
    private String name;
    private String value;
    private Integer rowspan;
    private List<DistributeVo> distributes;

    public RuleItemVo() {
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

    public List<DistributeVo> getDistributes() {
        return distributes;
    }

    public void setDistributes(List<DistributeVo> distributes) {
        this.distributes = distributes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
