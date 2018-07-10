package com.thinkgem.jeesite.modules.contract.proc;

import com.thinkgem.jeesite.modules.contract.entity.Contract;
import com.thinkgem.jeesite.modules.contract.service.ContService;
import com.thinkgem.jeesite.modules.income.entity.Income;
import com.thinkgem.jeesite.modules.income.service.IncomeService;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * 合同创建者任务分配
 *
 */
@SuppressWarnings("serial")
public class CreatorTaskHandler implements TaskListener {


	@Override
	public void notify(DelegateTask delegateTask) {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		ContService contService=(ContService)wac.getBean("contService");
		String contractId =(String)delegateTask.getVariable("businessId");
		Contract contract=contService.get(contractId);
		delegateTask.addCandidateUser(contract.getCreateBy().getLoginName());
	}

}
