/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.FTPUtil;
import com.thinkgem.jeesite.common.utils.FileUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.act.constant.ActConstant;
import com.thinkgem.jeesite.modules.act.entity.BaseReview;
import com.thinkgem.jeesite.modules.act.service.ActTaskService;
import com.thinkgem.jeesite.modules.contract.constant.ContConstant;
import com.thinkgem.jeesite.modules.contract.entity.ContApply;
import com.thinkgem.jeesite.modules.contract.entity.ContAttach;
import com.thinkgem.jeesite.modules.contract.entity.Contract;
import com.thinkgem.jeesite.modules.contract.proc.ContractReview;
import com.thinkgem.jeesite.modules.contract.service.ContApplyService;
import com.thinkgem.jeesite.modules.contract.service.ContAttachService;
import com.thinkgem.jeesite.modules.contract.service.ContService;
import com.thinkgem.jeesite.modules.contract.vo.FinanceVO;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.AreaService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 合同管理Controller
 * @author cuijp
 * @version 2018-04-19
 */
@Controller
@RequestMapping(value = "${adminPath}/cont/apply/pay/proc")
public class ContProcApplyPayController extends BaseController {


	@Autowired
	private ActTaskService actTaskService;


	@Autowired
	private ContApplyService contApplyService;








	/**
	 * 发起请款审批
	 *
	 * @param id //合同id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("start")
	@ResponseBody
	public Map start(String id) throws Exception {
		ContApply contApply=contApplyService.get(id);
		Map<String,Object> result=new HashMap<String, Object>();
		try {
			Map<String,Object> variables = null;
			variables = new HashMap<String, Object>();
			variables.put("businessId", id);
			variables.put("role","cashier");
			String processInstanceId=actTaskService.startProcess(ContConstant.PROCESS_KEY_CONTRACT_APPLY_PAY, ContConstant.TABLE_NAME_CONT_APPYLY,id, contApply.getReceiptName(),variables);
			actTaskService.completeFirstTask(processInstanceId);
			contApply=contApplyService.get(id);
			contApply.setStatus(2);
			//改合同状态
			contApplyService.save(contApply);
			result.put("result","success");

		} catch (Exception e) {
			e.printStackTrace();
			result.put("result","error");

		} finally {
			return result;
		}


	}

	@RequestMapping(value = {"finance"})
	public String   finance(String id,String taskId, Model model) throws Exception{
		ContApply contApply=contApplyService.get(id);

		FinanceVO finance=new FinanceVO();
		finance.setName(contApply.getReceiptName());
		finance.setTaxId(contApply.getTaxId());
		finance.setAddressPhone(contApply.getReceiptAddress()+" "+contApply.getReceiptPhone());
		finance.setBankAccount(contApply.getReceiptBank()+" "+contApply.getReceiptAccount());
		finance.setContent(contApply.getReceiptContent());
		finance.setValue(contApply.getReceiptValue().doubleValue());
		finance.setReceiptRemark(contApply.getReceiptRemark());
		//
		finance.setRemark(contApply.getRemark());
		finance.setReceiptDate(contApply.getReceiptDate());
		model.addAttribute("finance", finance);
		List<Comment> comments=actTaskService.getTaskHistoryCommentList( taskId);
		model.addAttribute("taskId", taskId);
		model.addAttribute("contApply",contApply);
		model.addAttribute("comments",comments);
		model.addAttribute("review", new BaseReview());
		model.addAttribute("readonly",true);
		return "modules/contract/proc/applyPay/finance";
	}

	@RequestMapping(value = {"finance/submit"},method= RequestMethod.POST)
	public String  financeSubmit(BaseReview review,RedirectAttributes redirectAttributes) {
		Task task=actTaskService.getTask( review.getTaskId());
		String taskId=task.getId();
		String contApplyId=(String)actTaskService.getTaskVariable(taskId,"businessId");
		String processInstanceId = task.getProcessInstanceId(); // 获取流程实例id
		Map<String, Object> variables=new HashMap<String,Object>();
		variables.put("msg", "pass");
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
		Authentication.setAuthenticatedUserId("【财务】"+user.getName());// 设置用户id
		actTaskService.complete(taskId,processInstanceId,review.getComment(),variables);
		if( state==1) {
			ContApply contApply = contApplyService.get(contApplyId);
			contApply.setStatus(3);
			contApplyService.save(contApply);
		}
		addMessage(redirectAttributes, "操作成功");
		return "redirect:"+adminPath+ ActConstant.MY_TASK_LIST;
	}

	@RequestMapping(value = {"improve"})
	public String   improve(String id,String taskId, Model model) throws Exception{
		ContApply contApply=contApplyService.get(id);
		List<Comment> comments=actTaskService.getTaskHistoryCommentList( taskId);
		model.addAttribute("taskId", taskId);
		model.addAttribute("contApply",contApply);
		model.addAttribute("comments",comments);
		model.addAttribute("review", new BaseReview());
		model.addAttribute("readonly",false);
		return "modules/contract/proc/applyPay/improve";
	}

	@RequestMapping(value = {"/improve/submit"},method= RequestMethod.POST)
	public String  improveSubmit(BaseReview review,RedirectAttributes redirectAttributes) {
		Task task=actTaskService.getTask( review.getTaskId());
		String taskId=task.getId();
		String processInstanceId = task.getProcessInstanceId(); // 获取流程实例id
		Map<String, Object> variables=new HashMap<String,Object>();
		variables.put("msg", "pass");
		int state=review.getState();
		User user=UserUtils.getUser();
		Authentication.setAuthenticatedUserId("【创建者】"+user.getName());// 设置用户id
		actTaskService.complete(taskId,processInstanceId,review.getComment(),variables);
		addMessage(redirectAttributes, "操作成功");
		return "redirect:"+adminPath+ ActConstant.MY_TASK_LIST;
	}


	@RequestMapping(value = "save")
	public String save(ContApply contApply,String taskIdApplyPay, Model model, RedirectAttributes redirectAttributes) {
		try {
            ContApply saveContApply=contApplyService.get(contApply.getId());
            saveContApply.setRemark(contApply.getRemark());
            saveContApply.setReceiptName(contApply.getReceiptName());
            saveContApply.setReceiptValue(contApply.getReceiptValue());
            saveContApply.setReceiptAccount(contApply.getReceiptAccount());
            saveContApply.setReceiptAddress(contApply.getReceiptAddress());
            saveContApply.setReceiptBank(contApply.getReceiptBank());
            saveContApply.setReceiptDate(contApply.getReceiptDate());
            saveContApply.setReceiptContent(contApply.getReceiptContent());
            saveContApply.setReceiptPhone(contApply.getReceiptPhone());
            saveContApply.setReceiptRemark(contApply.getReceiptRemark());
            saveContApply.setTaxId(contApply.getTaxId());
            contApplyService.save(saveContApply);
			addMessage(redirectAttributes, "保存请款信息成功");
		}catch (Exception e){
			e.printStackTrace();
			addMessage(redirectAttributes, "保存请款信息失败");
		}


		return "redirect:"+Global.getAdminPath()+"/cont/apply/pay/proc/improve?taskId="+taskIdApplyPay+"&id="+contApply.getId();
	}





}