package com.thinkgem.jeesite.modules.income.proc;

import com.thinkgem.jeesite.modules.contract.entity.Contract;
import com.thinkgem.jeesite.modules.contract.service.ContService;
import com.thinkgem.jeesite.modules.income.entity.Income;
import com.thinkgem.jeesite.modules.income.service.IncomeService;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * 员工经理任务分配
 *
 */
@SuppressWarnings("serial")
public class OfficeLeaderTaskHandler implements TaskListener {


	@Override
	public void notify(DelegateTask delegateTask) {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		IncomeService incomeService=(IncomeService)wac.getBean("incomeService");
		SystemService systemService=(SystemService)wac.getBean("systemService");
		ContService contService=(ContService)wac.getBean("contService");
		String incomeId =(String)delegateTask.getVariable("businessId");
		Income income=incomeService.get(incomeId);
		Contract contract=contService.get(income.getContract().getId());
		User creator =systemService.getUser(contract.getCreateBy().getId());
		Office office=creator.getOffice();
		String loginName=office.getPrimaryPerson().getLoginName();
		List<String> userList=new ArrayList<String>();
		userList.add(loginName);
		loginName=office.getDeputyPerson().getLoginName();
		userList.add(loginName);
		delegateTask.addCandidateUsers(userList);
	}

}
