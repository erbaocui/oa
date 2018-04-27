/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.act.constant.ActConstant;
import com.thinkgem.jeesite.modules.act.entity.BaseReview;
import com.thinkgem.jeesite.modules.act.service.ActTaskService;
import com.thinkgem.jeesite.modules.contract.constant.ContConstant;
import com.thinkgem.jeesite.modules.contract.entity.Contract;
import com.thinkgem.jeesite.modules.contract.entity.QueryContract;
import com.thinkgem.jeesite.modules.contract.service.ContService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.identity.Authentication;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 合同管理Controller
 * @author cuijp
 * @version 2018-04-19
 */
@Controller
@RequestMapping(value = "${adminPath}/cont/audit")
public class ContAuditController extends BaseController {

	@Autowired
	private ContService contractService;

	@Autowired
	private ActTaskService actTaskService;



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
	@RequestMapping("/start")
	@ResponseBody
	public Map start(String id) throws Exception {
		Map<String,Object> result=new HashMap<String, Object>();
		try {
			Map<String,Object> variables = null;
			variables = new HashMap<String, Object>();
			variables.put("businessId", id);
			String processInstanceId=actTaskService.startProcess(ActConstant.CONTRACT_REVIEW_PROCESS_KEY, ContConstant.CONTRACT_TABLE_NAME,id,ActConstant.CONTRACT_REVIEW_PROCESS_TITLE,variables);
			actTaskService.completeFirstTask(processInstanceId);
			Contract contract =contractService.get(id);
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
		List<Comment> comments=actTaskService.getTaskHistoryCommentList(taskId);

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
		actTaskService.complete(taskId,processInstanceId,review.getComment(),variables);
		addMessage(redirectAttributes, "操作成功");
		return "redirect:"+adminPath+ ActConstant.MY_TASK_LIST;

	}

	@RequiresPermissions("contract:contract:view")
	@RequestMapping(value = {"risk"})
	public String risk(String id,String taskId, Model model) throws Exception{
		Contract contract=contractService.get(id);
		List<Comment> comments=actTaskService.getTaskHistoryCommentList( taskId);
		model.addAttribute("taskId", taskId);
		model.addAttribute("contract",contract);
		model.addAttribute("comments",comments);
		model.addAttribute("review", new BaseReview());
		return "modules/contract/contractRisk";
	}

	@RequiresPermissions("contract:contract:view")
	@RequestMapping(value = {"riskReview"},method= RequestMethod.POST)
	public String riskReview(BaseReview review,RedirectAttributes redirectAttributes) {
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
		actTaskService.complete(taskId,processInstanceId,review.getComment(),variables);
		addMessage(redirectAttributes, "操作成功");
		return "redirect:"+adminPath+ ActConstant.MY_TASK_LIST;

	}

	@RequiresPermissions("contract:contract:view")
	@RequestMapping(value = {"leader"})
	public String leader(String id,String taskId, Model model) throws Exception{
		Contract contract=contractService.get(id);
		List<Comment> comments=actTaskService.getTaskHistoryCommentList( taskId);
		model.addAttribute("taskId", taskId);
		model.addAttribute("contract",contract);
		model.addAttribute("comments",comments);
		model.addAttribute("review", new BaseReview());
		return "modules/contract/contractLeader";
	}

	@RequiresPermissions("contract:contract:view")
	@RequestMapping(value = {"leaderReview"},method= RequestMethod.POST)
	public String leaderReview(BaseReview review,RedirectAttributes redirectAttributes) {
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
		actTaskService.complete(taskId,processInstanceId,review.getComment(),variables);
		addMessage(redirectAttributes, "操作成功");
		return "redirect:"+adminPath+ ActConstant.MY_TASK_LIST;
	}

	@RequiresPermissions("contract:contract:view")
	@RequestMapping(value = {"improve"})
	public String improve(String id,String taskId, Model model) throws Exception{
		Contract contract=contractService.get(id);
		List<Comment> comments=actTaskService.getTaskHistoryCommentList( taskId);
		model.addAttribute("taskId", taskId);
		model.addAttribute("contract",contract);
		model.addAttribute("comments",comments);
		model.addAttribute("review", new BaseReview());
		return "modules/contract/contractImprove";
	}

	@RequiresPermissions("contract:contract:view")
	@RequestMapping(value = {"improveReview"},method= RequestMethod.POST)
	public String improveReview(BaseReview review,RedirectAttributes redirectAttributes) {
		Task task=actTaskService.getTask( review.getTaskId());
		String taskId=task.getId();
		String processInstanceId = task.getProcessInstanceId(); // 获取流程实例id
		Map<String, Object> variables=new HashMap<String,Object>();
		variables.put("msg", "pass");
		User user=UserUtils.getUser();
		Authentication.setAuthenticatedUserId(  user.getName()+ "【"+UserUtils.getUser().getLoginName()+"】");// 设置用户id
		actTaskService.complete(taskId,processInstanceId,review.getComment(),variables);
		addMessage(redirectAttributes, "操作成功");
		return "redirect:"+adminPath+ ActConstant.MY_TASK_LIST;
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