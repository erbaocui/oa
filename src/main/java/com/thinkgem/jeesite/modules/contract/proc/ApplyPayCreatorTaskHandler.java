package com.thinkgem.jeesite.modules.contract.proc;

import com.thinkgem.jeesite.modules.contract.entity.ContApply;
import com.thinkgem.jeesite.modules.contract.entity.Contract;
import com.thinkgem.jeesite.modules.contract.service.ContApplyService;
import com.thinkgem.jeesite.modules.contract.service.ContService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

/**
 * 合同创建者任务分配
 *
 */
@SuppressWarnings("serial")
public class ApplyPayCreatorTaskHandler implements TaskListener {


	@Override
	public void notify(DelegateTask delegateTask) {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		ContApplyService contApplyService =(ContApplyService )wac.getBean("contApplyService");
		String contApplyId =(String)delegateTask.getVariable("businessId");
		ContApply contApply=contApplyService.get(contApplyId);
		delegateTask.addCandidateUser(contApply.getCreateBy().getLoginName());
	}

}
