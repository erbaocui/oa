package com.thinkgem.jeesite.modules.contract.proc;

import com.thinkgem.jeesite.modules.contract.service.ContSplitDetailService;
import com.thinkgem.jeesite.modules.income.entity.DistType;
import com.thinkgem.jeesite.modules.income.service.DistTypeService;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * 员工经理任务分配
 *
 */
@SuppressWarnings("serial")
public class OfficeChiefTaskHandler implements TaskListener {
	@Override
	public void notify(DelegateTask delegateTask) {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		ContSplitDetailService contSplitDetailService=(ContSplitDetailService)wac.getBean("contSplitDetailService");

		String detailId =(String)delegateTask.getVariable("businessId");

		List<String> userLoginNameList=contSplitDetailService.findChiefLoginNameList(detailId);
		if(userLoginNameList.size()==1){
			delegateTask.setAssignee(userLoginNameList.get(0));
		}else{
			delegateTask.addCandidateUsers(userLoginNameList);
		}



	}

}
