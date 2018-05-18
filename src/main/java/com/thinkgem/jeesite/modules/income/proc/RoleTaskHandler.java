package com.thinkgem.jeesite.modules.income.proc;

import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 员工经理任务分配
 *
 */
@SuppressWarnings("serial")
public class RoleTaskHandler implements TaskListener {
	@Autowired


	@Override
	public void notify(DelegateTask delegateTask) {
		//String id =delegateTask.getVariable("businessId");
		//delegateTask.addCandidateUsers(userIds);
	}

}
