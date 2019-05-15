package com.thinkgem.jeesite.modules.contract.proc;

import com.thinkgem.jeesite.modules.contract.entity.ContSplitDetail;
import com.thinkgem.jeesite.modules.contract.entity.Contract;
import com.thinkgem.jeesite.modules.contract.service.ContService;
import com.thinkgem.jeesite.modules.contract.service.ContSplitDetailService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

/**
 * 合同创建者任务分配
 *
 */
@SuppressWarnings("serial")
public class SplitDetailCreatorTaskHandler implements TaskListener {


	@Override
	public void notify(DelegateTask delegateTask) {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		String detailId =(String)delegateTask.getVariable("businessId");
		ContSplitDetailService contSplitDetailService=(ContSplitDetailService)wac.getBean("contSplitDetailService");		String contractId =(String)delegateTask.getVariable("businessId");
		ContSplitDetail detail=contSplitDetailService.get(detailId);
		delegateTask.addCandidateUser(detail.getCreateBy().getLoginName());
	}

}
