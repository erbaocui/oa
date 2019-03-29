package com.thinkgem.jeesite.modules.income.proc;

import com.thinkgem.jeesite.modules.income.entity.DistOffice;
import com.thinkgem.jeesite.modules.income.entity.DistType;
import com.thinkgem.jeesite.modules.income.entity.Income;
import com.thinkgem.jeesite.modules.income.service.DistOfficeService;
import com.thinkgem.jeesite.modules.income.service.DistTypeService;
import com.thinkgem.jeesite.modules.income.service.IncomeService;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 员工经理任务分配
 *
 */
@SuppressWarnings("serial")
public class OfficeChiefTaskHandler implements TaskListener {
	@Override
	public void notify(DelegateTask delegateTask) {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		DistTypeService distTypeService=(DistTypeService)wac.getBean("distTypeService");
		OfficeService officeService=(OfficeService)wac.getBean("officeService");

		String incomeId =(String)delegateTask.getVariable("businessId");
		DistType distType=new DistType();
		distType.setIncomeId(incomeId);
		List<String> officeIds=distTypeService.getDistOfficeIdList(distType);
		List<String> userLoginNameList=new ArrayList<String>();
		for( String  officeId:officeIds){
			Office office=(Office)officeService.get(officeId);
			String loginName=office.getPrimaryPerson().getLoginName();
			userLoginNameList.add(loginName);
		}
	/*	Map<String,Object> variables = null;
		variables = new HashMap<String, Object>();
		variables.put("nrOfInstances",userLoginNameList.size());
		delegateTask.setVariables(variables);*/

	    if(userLoginNameList.size()==1){
			delegateTask.setAssignee(userLoginNameList.get(0));
		}else{
			delegateTask.addCandidateUsers(userLoginNameList);
		}



	}

}
