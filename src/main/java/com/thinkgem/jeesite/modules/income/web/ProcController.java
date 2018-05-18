/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.income.web;

import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.act.constant.ActConstant;
import com.thinkgem.jeesite.modules.act.entity.BaseReview;
import com.thinkgem.jeesite.modules.act.service.ActTaskService;
import com.thinkgem.jeesite.modules.contract.constant.ContConstant;
import com.thinkgem.jeesite.modules.contract.entity.Contract;
import com.thinkgem.jeesite.modules.contract.service.ContService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 合同管理Controller
 * @author cuijp
 * @version 2018-04-19
 */
@Controller
@RequestMapping(value = "${adminPath}/income/proc")
public class ProcController extends BaseController {


	@Autowired
	private ActTaskService actTaskService;




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
			//Contract contract =contractService.get(id);
			//contract.setStatus(2);
			//contractService.save(contract);// 修改请假单状态*/
			result.put("result","success");

		} catch (Exception e) {
			result.put("result","error");

		} finally {
			return result;
		}


	}

	@RequestMapping(value = {"auditor"})
	public String auditorView(String id,String taskId, Model model) throws Exception{
	   // Contract contract=contractService.get(id);
		List<Comment> comments=actTaskService.getTaskHistoryCommentList(taskId);

		model.addAttribute("taskId", taskId);
		//model.addAttribute("contract",contract);
		model.addAttribute("comments",comments);
		model.addAttribute("review", new BaseReview());
		return "modules/contract/auditAuditor";
	}

	@RequestMapping(value = {"auditorSubmit"},method= RequestMethod.POST)
	public String auditorSubmit(BaseReview review,RedirectAttributes redirectAttributes) {
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


	@RequestMapping(value = {"risk"})
	public String riskView(String id,String taskId, Model model) throws Exception{
		//Contract contract=contractService.get(id);
		List<Comment> comments=actTaskService.getTaskHistoryCommentList( taskId);
		model.addAttribute("taskId", taskId);
		//model.addAttribute("contract",contract);
		model.addAttribute("comments",comments);
		model.addAttribute("review", new BaseReview());
		return "modules/contract/auditRisk";
	}

	@RequestMapping(value = {"riskSubmit"},method= RequestMethod.POST)
	public String riskSubmit(BaseReview review,RedirectAttributes redirectAttributes) {
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

	@RequestMapping(value = {"leader"})
	public String leaderView(String id,String taskId, Model model) throws Exception{
		//Contract contract=contractService.get(id);
		List<Comment> comments=actTaskService.getTaskHistoryCommentList( taskId);
		model.addAttribute("taskId", taskId);
		//model.addAttribute("contract",contract);
		model.addAttribute("comments",comments);
		model.addAttribute("review", new BaseReview());
		return "modules/contract/auditLeader";
	}


	@RequestMapping(value = {"leaderSubmit"},method= RequestMethod.POST)
	public String leaderSubmit(BaseReview review,RedirectAttributes redirectAttributes) {
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


	@RequestMapping(value = {"improve"})
	public String improveView(String id,String taskId, Model model) throws Exception{
		//Contract contract=contractService.get(id);
		List<Comment> comments=actTaskService.getTaskHistoryCommentList( taskId);
		model.addAttribute("taskId", taskId);
		//model.addAttribute("contract",contract);
		model.addAttribute("comments",comments);
		model.addAttribute("review", new BaseReview());
		return "modules/contract/auditImprove";
	}


	@RequestMapping(value = {"improveSubmit"},method= RequestMethod.POST)
	public String improveSubmit(BaseReview review,RedirectAttributes redirectAttributes) {
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

}