/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.act.entity.BaseReview;
import com.thinkgem.jeesite.modules.act.service.ActTaskService;
import com.thinkgem.jeesite.modules.contract.constant.ContractConstant;
import com.thinkgem.jeesite.modules.contract.entity.Contract;
import com.thinkgem.jeesite.modules.contract.entity.QueryContract;
import com.thinkgem.jeesite.modules.contract.service.ContractService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 合同管理Controller
 * @author cuijp
 * @version 2018-04-19
 */
@Controller
@RequestMapping(value = "${adminPath}/contract/contract")
public class ContractController extends BaseController {

	@Autowired
	private ContractService contractService;

	@Autowired
	private ActTaskService actTaskService;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;


	@ModelAttribute
	public Contract get(@RequestParam(required=false) String id) {
		Contract entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = contractService.get(id);
		}
		if (entity == null){
			entity = new Contract();
		}
		return entity;
	}

	/**
	 * 发起合同审批
	 *
	 * @param id //合同id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/startReview")
	@ResponseBody
	public Map startApply(String id) throws Exception {
		Map<String,Object> result=new HashMap<String, Object>();
		try {
			Map<String,Object> variables = null;
			variables = new HashMap<String, Object>();
			variables.put("contactId", id);
			ProcessInstance pi =runtimeService.startProcessInstanceByKey(ContractConstant.CONTRACT_REVIEW_PROCESS_KEY,variables);// 启动流程
			// 根据流程实例id查询任务
			Task task = taskService.createTaskQuery().processInstanceId(pi.getProcessInstanceId()).singleResult();
			taskService.complete(task.getId()); //完成任务
			Contract contract =contractService.get(id);
			//contract.setProcessStatus();
			contract.setProcessInsId(pi.getProcessInstanceId());
			contract.setStatus(2);
			contractService.save(contract);// 修改请假单状态*/
			result.put("result","success");

		} catch (Exception e) {
			result.put("result","error");

		} finally {
			return result;
		}


	}

	@RequiresPermissions("contract:contract:view")
	@RequestMapping(value = {"auditor"})
	public String auditor(String id,String taskId, Model model) throws Exception{
		Contract contract=contractService.get(id);
		List<Comment> comments=actTaskService.getTaskHistoryCommentList( taskId);
		model.addAttribute("taskId", taskId);
		model.addAttribute("contract",contract);
		model.addAttribute("comments",comments);


		model.addAttribute("review", new BaseReview());
		return "modules/contract/contractAuditor";
	}

	@RequiresPermissions("contract:contract:view")
	@RequestMapping(value = {"auditorReview"},method= RequestMethod.POST)
	public String auditorReview(BaseReview review,RedirectAttributes redirectAttributes) {
	    Task task=actTaskService.getTask( review.getTaskId());
	    String taskId=task.getId();
		String processInstanceId = task.getProcessInstanceId(); // 获取流程实例id
		Map<String, Object> variables=new HashMap<String,Object>();
        int state=review.getState();
		if( state==1){
			variables.put("msg", "pass");
		}else if(state==2){
			variables.put("msg", "reject");
		}
		User user=UserUtils.getUser();
		Authentication.setAuthenticatedUserId(  user.getName()+ "【"+UserUtils.getUser().getLoginName()+"】");// 设置用户id
		//actTaskService.addTaskComment( taskId, processInstanceId, review.getComment());
		//actTaskService.complete(taskId,processInstanceId,variables); // 完成任务*/
		actTaskService.complete(taskId,processInstanceId,review.getComment(),variables);
		addMessage(redirectAttributes, "操作成功");
		return "redirect:"+adminPath+"/act/task/todo?repage";
		//return "redirect:"+adminPath+"/act/task/todo/";
	}
	
	@RequiresPermissions("contract:contract:view")
	@RequestMapping(value = {"list", ""})
	public String list(QueryContract queryContract, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Contract> page = contractService.findPage(new Page<Contract>(request, response), queryContract);
		model.addAttribute("page", page);
		model.addAttribute("queryContract", queryContract);
		return "modules/contract/contractList";
	}

	@RequiresPermissions("contract:contract:view")
	@RequestMapping(value = "form")
	public String form(Contract contract, Model model) {
		model.addAttribute("contract", contract);
		return "modules/contract/contractForm";
	}

	@RequiresPermissions("contract:contract:edit")
	@RequestMapping(value = "save")
	public String save(Contract contract, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, contract)){
			return form(contract, model);
		}
		contractService.save(contract);
		addMessage(redirectAttributes, "合同保存成功");
		return "redirect:"+adminPath+"/contract/contract/?repage";
	}
	
	@RequiresPermissions("contract:contract:edit")
	@RequestMapping(value = "delete")
	public String delete(Contract contract, RedirectAttributes redirectAttributes) {
		contractService.delete(contract);
		addMessage(redirectAttributes, "删除保存合同成功成功");
		return "redirect:"+Global.getAdminPath()+"/contract/contract/?repage";
	}

}