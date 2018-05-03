package com.thinkgem.jeesite.modules.contract.entity;

import java.util.List;

/**
 * Created by USER on 2018/4/18.
 */
public class QueryContract extends Contract {

   private String year;

   private  String notIn;

    public QueryContract() {
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getNotIn() {
        return notIn;
    }

    public void setNotIn(String notIn) {
        this.notIn = notIn;
    }
}
