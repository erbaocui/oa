package com.thinkgem.jeesite.modules.contract.vo;

import com.thinkgem.jeesite.modules.contract.entity.Contract;

import java.util.List;

/**
 * Created by USER on 2018/4/18.
 */
public class QueryContract extends Contract {

   private String year;
   private Integer min;
   private Integer max;

   private  String[] noInArray;
   private List<String> typeIds;

    public QueryContract() {
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String[] getNoInArray() {
        return noInArray;
    }

    public void setNoInArray(String[] noInArray) {
        this.noInArray = noInArray;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public List<String> getTypeIds() {
        return typeIds;
    }

    public void setTypeIds(List<String> typeIds) {
        this.typeIds = typeIds;
    }
}
