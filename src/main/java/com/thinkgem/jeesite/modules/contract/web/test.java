package com.thinkgem.jeesite.modules.contract.web;

public class test {
    public static void main(String[] args) {
       String abc= "AND (o.id = 'DPT110921000032' OR o.parent_ids LIKE '0,1000,DPT110921000032,%' OR c.id = 'TJZ0023')";
       abc=abc.substring(0,abc.lastIndexOf("OR"))+")";
        System.out.println(abc);
    }
}
