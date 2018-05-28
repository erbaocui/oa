package com.thinkgem.jeesite.modules.income.proc;

import com.thinkgem.jeesite.modules.income.service.IncomeService;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

/**
 * 员工经理任务分配
 *
 */
@SuppressWarnings("serial")
public class OfficeChiefTaskHandler implements TaskListener {
	@Override
	public void notify(DelegateTask delegateTask) {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		IncomeService incomeService=(IncomeService)wac.getBean("incomeService");
		OfficeService officeService=(OfficeService)wac.getBean("officeService");
		String incomeId =(String)delegateTask.getVariable("businessId");
		String officeId=incomeService.get(incomeId).getContract().getId();
		String userId=((Office)officeService.get(officeId)).getPrimaryPerson().getId();
		delegateTask.setAssignee( userId);
	}

}
