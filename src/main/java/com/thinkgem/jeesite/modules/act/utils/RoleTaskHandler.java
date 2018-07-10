package com.thinkgem.jeesite.modules.act.utils;

import com.thinkgem.jeesite.modules.income.service.IncomeService;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * 员工经理任务分配
 *
 */
@SuppressWarnings("serial")
public class RoleTaskHandler implements ExecutionListener, TaskListener {
	@Autowired
	private SystemService systemService;


	@Override
	public void notify(DelegateTask delegateTask) {
		String roleEnname =(String )delegateTask.getVariable("role");
		//String roleEnname="";

		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		SystemService systemService=(SystemService)wac.getBean("systemService");
		List<User> userList=systemService.findUserByRoleEnname(roleEnname);
		List<String>userIdList=new ArrayList<String>();
		for(User user:userList){
			userIdList.add( user.getLoginName());
		}
		if(userList!=null&&userList.size()>0) {
			if (userList.size() == 1) {
				delegateTask.addCandidateUser(userIdList.get(0));
			} else {
				delegateTask.addCandidateUsers(userIdList);
			}
		}
	}

	@Override
	public void notify(DelegateExecution delegateExecution) throws Exception {

	}
}
