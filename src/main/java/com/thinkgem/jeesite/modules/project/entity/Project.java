package com.thinkgem.jeesite.modules.project.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.sys.entity.Office;

import java.math.BigDecimal;
import java.util.Date;

public class Project extends DataEntity<Project> {
    private Office office;
    private String code;
    private String name;

    public Project() {

    }

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}