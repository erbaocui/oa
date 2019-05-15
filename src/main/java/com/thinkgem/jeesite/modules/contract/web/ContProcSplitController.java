/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.web;

import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.act.constant.ActConstant;
import com.thinkgem.jeesite.modules.act.entity.BaseReview;
import com.thinkgem.jeesite.modules.act.service.ActTaskService;
import com.thinkgem.jeesite.modules.contract.constant.ContConstant;
import com.thinkgem.jeesite.modules.contract.entity.*;
import com.thinkgem.jeesite.modules.contract.service.ContService;
import com.thinkgem.jeesite.modules.contract.service.ContSplitItemService;
import com.thinkgem.jeesite.modules.contract.service.ContSplitService;
import com.thinkgem.jeesite.modules.income.constant.IncomeConstant;
import com.thinkgem.jeesite.modules.income.entity.DistType;
import com.thinkgem.jeesite.modules.income.entity.Income;
import com.thinkgem.jeesite.modules.income.service.*;
import com.thinkgem.jeesite.modules.income.vo.DistOfficeProc;
import com.thinkgem.jeesite.modules.income.vo.DistOfficeVo;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 进款分配流程Controller
 * @author cuijp
 * @version 2018-05-28
 */
@Controller
@RequestMapping(value = "${adminPath}/cont/split/proc")
public class ContProcSplitController extends BaseController {
	@Autowired
	private ContService contService;

	@Autowired
	private ContSplitService contSplitService;
	@Autowired
	private ContSplitItemService contSplitItemService;

	@Autowired
	private ActTaskService actTaskService;





	/**
	 * 发起chief流程
	 *
	 * @param id //拆分id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/start")
	@ResponseBody
	public Map start(String id) throws Exception {
		Map<String,Object> result=new HashMap<String, Object>();
		try {
			Map<String,Object> variables = new HashMap<String, Object>();
			variables.put("businessId", id);
			variables.put("role","contractAudit");
			String processInstanceId=actTaskService.startProcess(ContConstant.PROCESS_KEY_CONTRACT_SPLIT , ContConstant.TABLE_NAME_CONT_SPLIT,id,ContConstant.PROCESS_TITLE_CONTRACT_SPLIT,variables);
			actTaskService.completeFirstTask(processInstanceId);
			ContSplit split =contSplitService.get(id);
			split.setStatus(ContConstant.SplitStatus.INPROCESS.getValue());
			split.setProcInsId(processInstanceId);
			contSplitService.save(split);//
			result.put("result","success");

		} catch (Exception e) {
			e.printStackTrace();
			result.put("result","error");

		} finally {
			return result;
		}


	}
	//合同审核
	@RequestMapping(value = {"audit"})
	public String audit(BaseReview review, Model model, HttpServletRequest request) throws Exception{
		Task task=actTaskService.getTask(  review.getTaskId());
		String splitId =(String)actTaskService.getTaskVariable(task.getId(),"businessId");
		ContSplit split=contSplitService.get(splitId);

		Contract contract=contService.get( split.getContractId());
		ContSplitItem contSplitItem=new ContSplitItem();
		contSplitItem.setSplitId(splitId);

		List<ContSplitItem> contSplitDetailItems=contSplitItemService.findList(contSplitItem);

		List<Comment> comments=actTaskService.getTaskHistoryCommentList(review.getTaskId());
		model.addAttribute("taskId", review.getTaskId());
		model.addAttribute("comments",comments);
		model.addAttribute("review", new BaseReview());
		model.addAttribute("splitId",splitId);
		model.addAttribute("contSplit",split);
		model.addAttribute("contract",contract);
		model.addAttribute("contSplitItems",contSplitDetailItems);
		return "modules/contract/proc/split/audit";
	}

	@RequestMapping(value = {"audit/submit"},method= RequestMethod.POST)
	public String auditSubmit(BaseReview review, RedirectAttributes redirectAttributes)  throws Exception{
		Task task=actTaskService.getTask(  review.getTaskId());
		String taskId=task.getId();
		String processInstanceId = task.getProcessInstanceId(); // 获取流程实例id
		Map<String, Object> variables=new HashMap<String,Object>();
		String detailId =(String)actTaskService.getTaskVariable(taskId,"businessId");
		if(review.getComment()==null||review.getComment().equals("")){
			review.setComment("部门确认完成");
		}
		int state=review.getState();
		if( state==1){
			if(review.getComment()==null||review.getComment().equals("")){
				review.setComment("通过");
			}
			variables.put("msg", "pass");



		}else if(state==2){
			variables.put("msg", "reject");
			variables.put("role","contract");


		}
		User user=UserUtils.getUser();
		Authentication.setAuthenticatedUserId(  "【合同复核】" +user.getName());// 设置用户id
		actTaskService.complete(taskId,processInstanceId,review.getComment(),variables);
		if( state==1) {
			ContSplit contSplit = contSplitService.get(detailId);
			contSplit.setStatus(ContConstant.SplitStatus.FINISH.getValue());
			contSplitService.save(contSplit);
		}
		addMessage(redirectAttributes, "操作成功");
		return "redirect:"+adminPath+ ActConstant.MY_TASK_LIST;

	}


	//部门分配
	@RequestMapping(value = {"administrator"})
	public String administrator(BaseReview review, Model model, HttpServletRequest request) throws Exception{
		Task task=actTaskService.getTask(  review.getTaskId());
		String splitId =(String)actTaskService.getTaskVariable(task.getId(),"businessId");
		ContSplit split=contSplitService.get(splitId);

		Contract contract=contService.get( split.getContractId());
		ContSplitItem contSplitItem=new ContSplitItem();
		contSplitItem.setSplitId(splitId);

		List<ContSplitItem> contSplitDetailItems=contSplitItemService.findList(contSplitItem);

		List<Comment> comments=actTaskService.getTaskHistoryCommentList(review.getTaskId());
		model.addAttribute("taskId", review.getTaskId());
		model.addAttribute("comments",comments);
		model.addAttribute("review", new BaseReview());
		model.addAttribute("splitId",splitId);
		model.addAttribute("contSplit",split);
		model.addAttribute("contSplitItem", contSplitItem);
		model.addAttribute("contract",contract);
		model.addAttribute("contSplitItems",contSplitDetailItems);
		return "modules/contract/proc/split/administrator";

	}

	@RequestMapping(value = {"administrator/submit"},method= RequestMethod.POST)
	public String administratorSubmit( BaseReview review, RedirectAttributes redirectAttributes)  throws Exception{
		Task task=actTaskService.getTask(   review.getTaskId());
		String taskId=task.getId();
		String processInstanceId = task.getProcessInstanceId(); // 获取流程实例id
		Map<String, Object> variables=new HashMap<String,Object>();
		if(review.getComment()==null||review.getComment().equals("")){
			review.setComment("合同拆解完成");
		}
		variables.put("role","contractAudit");
		User user=UserUtils.getUser();
		Authentication.setAuthenticatedUserId(  "【合同管理员】" +user.getName());// 设置用户id
		actTaskService.complete(taskId,processInstanceId,review.getComment(),variables);
		addMessage(redirectAttributes, "操作成功");
		return "redirect:"+adminPath+ ActConstant.MY_TASK_LIST;

	}

}