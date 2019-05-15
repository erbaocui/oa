/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.web;

import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.act.constant.ActConstant;
import com.thinkgem.jeesite.modules.act.entity.BaseReview;
import com.thinkgem.jeesite.modules.act.service.ActTaskService;
import com.thinkgem.jeesite.modules.contract.constant.ContConstant;
import com.thinkgem.jeesite.modules.contract.entity.ContSplitDetail;
import com.thinkgem.jeesite.modules.contract.entity.ContSplitDetailItem;
import com.thinkgem.jeesite.modules.contract.entity.ContSplitDetailOffice;
import com.thinkgem.jeesite.modules.contract.entity.Contract;
import com.thinkgem.jeesite.modules.contract.service.ContService;
import com.thinkgem.jeesite.modules.contract.service.ContSplitDetailItemService;
import com.thinkgem.jeesite.modules.contract.service.ContSplitDetailOfficeService;
import com.thinkgem.jeesite.modules.contract.service.ContSplitDetailService;
import com.thinkgem.jeesite.modules.sys.entity.User;
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
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程Controller
 * @author cuijp
 * @version 2018-05-28
 */
@Controller
@RequestMapping(value = "${adminPath}/cont/split/detail/proc")
public class ContProcSplitDetailController extends BaseController {
	@Autowired
	private ContService contService;
	@Autowired
	private ContSplitDetailService contSplitDetailService;
	@Autowired
	private ContSplitDetailItemService contSplitDetailItemService;
	@Autowired
	private ContSplitDetailOfficeService contSplitDetailOfficeService;

	@Autowired
	private ActTaskService actTaskService;




	/**
	 * 发起分配流程
	 *
	 * @param id //incomeId
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
			List<String> userLoginNameList=contSplitDetailService.findChiefLoginNameList(id);
			variables.put("assigneeList",userLoginNameList);
			BigDecimal rate=new BigDecimal("1").divide(new BigDecimal(userLoginNameList.size()),2,BigDecimal.ROUND_DOWN);
			variables.put("rate",rate);
			variables.put("msg","pass");
			String processInstanceId=actTaskService.startProcess(ContConstant.PROCESS_KEY_CONTRACT_SPLIT_DETAIL , ContConstant.TABLE_NAME_CONT_SPLIT_DETAIL,id,ContConstant.PROCESS_TITLE_CONTRACT_SPLIT_DETAIL,variables);
			actTaskService.completeFirstTask(processInstanceId);
			ContSplitDetail detail =contSplitDetailService.get(id);
			detail.setStatus(ContConstant.SplitDetailStatus.INPROCESS.getValue());
			detail.setProcInsId(processInstanceId);
			contSplitDetailService.save(detail);//
			result.put("result","success");

		} catch (Exception e) {
			e.printStackTrace();
			result.put("result","error");

		} finally {
			return result;
		}


	}
    //部门确认
	@RequestMapping(value = {"office"})
	public String office(BaseReview review, Model model, HttpServletRequest request) throws Exception{
		Task task=actTaskService.getTask(  review.getTaskId());
		String detailId =(String)actTaskService.getTaskVariable(task.getId(),"businessId");
		ContSplitDetail detail=contSplitDetailService.get(detailId);
		ContSplitDetailItem contSplitDetailItem=new ContSplitDetailItem();
		contSplitDetailItem.setDetailId(detailId);
		contSplitDetailItem.setOfficeId(UserUtils.getUser().getOffice().getId());
		List<ContSplitDetailOffice> contSplitDetailOffices=contSplitDetailOfficeService.findListByOfficeId(detailId,UserUtils.getUser().getOffice().getId());
		Contract contract=contService.get( detail.getContractId());
		List<Comment> comments=actTaskService.getTaskHistoryCommentList(review.getTaskId());
		model.addAttribute("taskId", review.getTaskId());
		model.addAttribute("comments",comments);
		model.addAttribute("review", new BaseReview());
		model.addAttribute("detailId",detailId);
		model.addAttribute("contract",contract);
		model.addAttribute("contSplitDetailOffices",contSplitDetailOffices);
		return "modules/contract/proc/splitDetail/office";
	}

	@RequestMapping(value = {"office/submit"},method= RequestMethod.POST)
	public String officeSubmit(BaseReview review, RedirectAttributes redirectAttributes)  throws Exception{

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
			variables.put("role","contract");

		}else if(state==2){
			variables.put("msg", "reject");


		}
		User user=UserUtils.getUser();
		Authentication.setAuthenticatedUserId(  "【部门确认】" +user.getName());// 设置用户id
		actTaskService.complete(taskId,processInstanceId,review.getComment(),variables);
		addMessage(redirectAttributes, "操作成功");
		return "redirect:"+adminPath+ ActConstant.MY_TASK_LIST;

	}


	//部门分配
	@RequestMapping(value = {"dist"})
	public String dist(BaseReview review, Model model, HttpServletRequest request) throws Exception{
		Task task=actTaskService.getTask(  review.getTaskId());
		String detailId =(String)actTaskService.getTaskVariable(task.getId(),"businessId");
		ContSplitDetail contSplitDetail =contSplitDetailService.get(detailId);
		Contract contract=contService.get(contSplitDetail.getContractId());

		ContSplitDetailItem contSplitDetailItem=new ContSplitDetailItem();
		contSplitDetailItem.setDetailId(contSplitDetail.getId());
		List<ContSplitDetailItem> contSplitDetailItems=contSplitDetailItemService.findList(contSplitDetailItem);

		model.addAttribute("contractId", contract.getId());
		model.addAttribute("contract", contract);
		model.addAttribute("contSplitDetail", contSplitDetail);
		model.addAttribute("contSplitDetailItem",new ContSplitDetailItem());
		model.addAttribute("contSplitDetailItems",contSplitDetailItems);

		List<Comment> comments=actTaskService.getTaskHistoryCommentList(review.getTaskId());
		model.addAttribute("taskId", review.getTaskId());
		model.addAttribute("comments",comments);
		model.addAttribute("review", new BaseReview());
		model.addAttribute("detailId",detailId);
		return "modules/contract/proc/splitDetail/dist";
	}

	@RequestMapping(value = {"dist/submit"},method= RequestMethod.POST)
	public String distSubmit( BaseReview review, RedirectAttributes redirectAttributes)  throws Exception{
		Task task=actTaskService.getTask(   review.getTaskId());
		String taskId=task.getId();
		String processInstanceId = task.getProcessInstanceId(); // 获取流程实例id
		Map<String, Object> variables=new HashMap<String,Object>();
		String detailId =(String)actTaskService.getTaskVariable(taskId,"businessId");
		if(review.getComment()==null||review.getComment().equals("")){
			review.setComment("部门分配完成");
		}
		List<String> userLoginNameList=contSplitDetailService.findChiefLoginNameList(detailId);
		variables.put("assigneeList",userLoginNameList);
		BigDecimal rate=new BigDecimal("1").divide(new BigDecimal(userLoginNameList.size()),2,BigDecimal.ROUND_DOWN);
		variables.put("rate",rate);
		variables.put("msg","pass");
		User user=UserUtils.getUser();
		Authentication.setAuthenticatedUserId(  "【部门分配】" +user.getName());// 设置用户id
		actTaskService.complete(taskId,processInstanceId,review.getComment(),variables);
		addMessage(redirectAttributes, "操作成功");
		return "redirect:"+adminPath+ ActConstant.MY_TASK_LIST;

	}

	//合同管理员确认
	@RequestMapping(value = {"administrator"})
	public String administrator(BaseReview review, Model model, HttpServletRequest request) throws Exception{
		Task task=actTaskService.getTask(  review.getTaskId());
		String detailId =(String)actTaskService.getTaskVariable(task.getId(),"businessId");
		ContSplitDetail detail=contSplitDetailService.get(detailId);

		Contract contract=contService.get( detail.getContractId());
		ContSplitDetailItem contSplitDetailItem=new ContSplitDetailItem();
		contSplitDetailItem.setDetailId(detailId);
		contSplitDetailItem.setOfficeId(UserUtils.getUser().getOffice().getId());
		List<ContSplitDetailItem> contSplitDetailItems=contSplitDetailItemService.findList(contSplitDetailItem);

		List<Comment> comments=actTaskService.getTaskHistoryCommentList(review.getTaskId());
		model.addAttribute("taskId", review.getTaskId());
		model.addAttribute("comments",comments);
		model.addAttribute("review", new BaseReview());
		model.addAttribute("detailId",detailId);
		model.addAttribute("contract",contract);
		model.addAttribute("contSplitDetailItems",contSplitDetailItems);
		return "modules/contract/proc/splitDetail/administrator";
	}

	@RequestMapping(value = {"administrator/submit"},method= RequestMethod.POST)
	public String administratorSubmit(BaseReview review, RedirectAttributes redirectAttributes)  throws Exception{
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
			variables.put("role","contractAudit");


		}else if(state==2){
			variables.put("msg", "reject");


		}
		User user=UserUtils.getUser();
		Authentication.setAuthenticatedUserId(  "【合同管理员】" +user.getName());// 设置用户id
		actTaskService.complete(taskId,processInstanceId,review.getComment(),variables);
		addMessage(redirectAttributes, "操作成功");
		return "redirect:"+adminPath+ ActConstant.MY_TASK_LIST;

	}


	//合同管理员确认
	@RequestMapping(value = {"audit"})
	public String audit(BaseReview review, Model model, HttpServletRequest request) throws Exception{
		Task task=actTaskService.getTask(  review.getTaskId());
		String detailId =(String)actTaskService.getTaskVariable(task.getId(),"businessId");
		ContSplitDetail detail=contSplitDetailService.get(detailId);

		Contract contract=contService.get( detail.getContractId());
		ContSplitDetailItem contSplitDetailItem=new ContSplitDetailItem();
		contSplitDetailItem.setDetailId(detailId);
		contSplitDetailItem.setOfficeId(UserUtils.getUser().getOffice().getId());
		List<ContSplitDetailItem> contSplitDetailItems=contSplitDetailItemService.findList(contSplitDetailItem);

		List<Comment> comments=actTaskService.getTaskHistoryCommentList(review.getTaskId());
		model.addAttribute("taskId", review.getTaskId());
		model.addAttribute("comments",comments);
		model.addAttribute("review", new BaseReview());
		model.addAttribute("detailId",detailId);
		model.addAttribute("contract",contract);
		model.addAttribute("contSplitDetailItems",contSplitDetailItems);
		return "modules/contract/proc/splitDetail/audit";
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


		}
		User user=UserUtils.getUser();
		Authentication.setAuthenticatedUserId(  "【合同复核】" +user.getName());// 设置用户id
		actTaskService.complete(taskId,processInstanceId,review.getComment(),variables);
		//
		ContSplitDetail contSplitDetail =contSplitDetailService.get(detailId);
		contSplitDetail.setStatus(ContConstant.SplitDetailStatus.FINISH.getValue());
		contSplitDetailService.save(contSplitDetail);
		addMessage(redirectAttributes, "操作成功");
		return "redirect:"+adminPath+ ActConstant.MY_TASK_LIST;

	}



}