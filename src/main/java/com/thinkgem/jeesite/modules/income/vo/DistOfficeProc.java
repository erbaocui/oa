package com.thinkgem.jeesite.modules.income.vo;

import com.thinkgem.jeesite.modules.income.entity.DistOffice;

/**
 * Created by USER on 2018/5/30.
 */
public class DistOfficeProc extends DistOffice {
    private String taskId; 		// 任务编号
    private Integer state;
    private String comment; 	// 任务意见

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
