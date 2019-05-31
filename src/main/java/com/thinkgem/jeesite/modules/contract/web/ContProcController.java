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
@RequestMapping(value = "${adminPath}/cont/audit/proc")
public class ContProcController extends BaseController {

	@Autowired
	private ContService contractService;

	@Autowired
	private ActTaskService actTaskService;

	@Autowired
	private ContAttachService contAttachService;

	@Autowired
	private ContApplyService contApplyService;
	@Autowired
	private AreaService areaService;



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
	@RequestMapping("/audit/start")
	@ResponseBody
	public Map auditStart(String id) throws Exception {
		Contract contract=get(id);
		Map<String,Object> result=new HashMap<String, Object>();
		try {
			Map<String,Object> variables = null;
			variables = new HashMap<String, Object>();
			variables.put("businessId", id);
			variables.put("role","contract");
			String processInstanceId=actTaskService.startProcess(ActConstant.CONTRACT_REVIEW_PROCESS_KEY, ContConstant.CONTRACT_TABLE_NAME,id,contract.getName()+"合同",variables);
			actTaskService.completeFirstTask(processInstanceId);
			contract =contractService.get(id);
			contract.setStatus(2);
			//改合同状态
			contractService.save(contract);
//			//创建者信息
//			List<Task> tasList=actTaskService.processInstanceTaskList(processInstanceId);
//             if(tasList!=null&&tasList.size()>0){
//             	Task task=tasList.get(0);
//             	//任务拾取
//				 actTaskService.claim(task.getId(),contract.getCreateBy().getLoginName());
//				 User user=UserUtils.getUser();
//				 Authentication.setAuthenticatedUserId(  "【合同创建者】" +user.getName());// 设置用户id
//				 actTaskService.complete(task.getId(),processInstanceId,"合同创建",variables);
//			 }
			result.put("result","success");

		} catch (Exception e) {
			e.printStackTrace();
			result.put("result","error");

		} finally {
			return result;
		}


	}

	@RequestMapping(value = {"/audit/manager"})
	public String auditManagerView(String id,String taskId, Model model) throws Exception{
	    Contract contract=contractService.get(id);
		List<Comment> comments=actTaskService.getTaskHistoryCommentList(taskId);
		ContractReview review=new ContractReview();
		review.setSpecificItem(contract.getSpecificItem());
		ContAttach contAttach=new ContAttach();
		contAttach.setContractId(contract.getId());
		List<ContAttach> list = contAttachService.findList(contAttach);
		model.addAttribute("contAttachs", list);
		model.addAttribute("taskId", taskId);
		model.addAttribute("contract",contract);
		model.addAttribute("comments",comments);
		model.addAttribute("review",review);
		model.addAttribute("readonly",true );
		//model.addAttribute("fileClass","2" );

		Area area=new Area();
		Area parent=new Area();
		parent.setId("1");
		area.setParent(parent);
		List<Area> provinceList =areaService.findList(area);
		model.addAttribute("provinceList",  provinceList );

		area=new Area();
		parent=new Area();
		if(contract!=null&&contract.getProvince()!=null&&(!StringUtils.isEmpty(contract.getProvince().getId()))){

			parent.setId(contract.getProvince().getId());
		}else{
			parent.setId( provinceList.get(0).getId());
		}
		area.setParent(parent);
		List<Area> cityList =areaService.findList(area);
		model.addAttribute("cityList",  cityList );
		return "modules/contract/proc/audit/auditManager";
	}

	@RequestMapping(value = {"/audit/managerSave"},method= RequestMethod.POST)
	public String auditManagerSave(ContractReview review,RedirectAttributes redirectAttributes) throws Exception{
		Task task=actTaskService.getTask( review.getTaskId());
		Map<String, Object> variables=task.getTaskLocalVariables();

		String taskId=task.getId();
		String processId=task.getProcessInstanceId();
		actTaskService.getTaskVariable(taskId,"businessId");
		String processInstanceId = task.getProcessInstanceId(); // 获取流程实例id
		String contractId=(String)actTaskService.getTaskVariable(taskId,"businessId");
		Contract contract=contractService.get(contractId);
		contract.setSpecificItem(review.getSpecificItem());
		contractService.save(contract);
		int state=review.getState();
		if( state==1){
			if(review.getComment()==null||review.getComment().equals("")){
				review.setComment("通过");
			}
			variables.put("msg", "pass");
			variables.put("role","risk");

		}else if(state==2){
			variables.put("msg", "reject");


 		}
		variables.put("value",contract.getValue());
		variables.put("specificItem",review.getSpecificItem());
		User user=UserUtils.getUser();
		Authentication.setAuthenticatedUserId(  "【合同管理员】" +user.getName());// 设置用户id
		actTaskService.complete(taskId,processInstanceId,review.getComment(),variables);
		if( state==1){
			if((review.getSpecificItem().equals("0"))&&(contract.getValue().doubleValue()<3000000)){
				actTaskService.completeTaskByDefKey(processId,"usertask11",variables);
			}
		}
		addMessage(redirectAttributes, "操作成功");
		return "redirect:"+adminPath+ ActConstant.MY_TASK_LIST;

	}


	@RequestMapping(value = {"/audit/risk"})
	public String auditRiskView(String id,String taskId, Model model) throws Exception{
		Contract contract=contractService.get(id);
		List<Comment> comments=actTaskService.getTaskHistoryCommentList( taskId);
		ContAttach contAttach=new ContAttach();
		contAttach.setContractId(contract.getId());
		List<ContAttach> list = contAttachService.findList(contAttach);
		model.addAttribute("contAttachs", list);
		model.addAttribute("taskId", taskId);
		model.addAttribute("contract",contract);
		model.addAttribute("comments",comments);
		model.addAttribute("review", new BaseReview());
		model.addAttribute("readonly",true);
		Area area=new Area();
		Area parent=new Area();
		parent.setId("1");
		area.setParent(parent);
		List<Area> provinceList =areaService.findList(area);
		model.addAttribute("provinceList",  provinceList );

		area=new Area();
		parent=new Area();
		if(contract!=null&&contract.getProvince()!=null&&(!StringUtils.isEmpty(contract.getProvince().getId()))){

			parent.setId(contract.getProvince().getId());
		}else{
			parent.setId( provinceList.get(0).getId());
		}
		area.setParent(parent);
		List<Area> cityList =areaService.findList(area);
		model.addAttribute("cityList",  cityList );
		//model.addAttribute("fileClass","2" );
		return "modules/contract/proc/audit/auditRisk";
	}

	@RequestMapping(value = {"/audit/riskSave"},method= RequestMethod.POST)
	public String auditRiskSave(BaseReview review,RedirectAttributes redirectAttributes)throws Exception{
		Task task=actTaskService.getTask( review.getTaskId());
		String taskId=task.getId();
		String processInstanceId = task.getProcessInstanceId(); // 获取流程实例id
		Map<String, Object> variables=new HashMap<String,Object>();
		int state=review.getState();
		if( state==1){
			if(review.getComment()==null||review.getComment().equals("")){
				review.setComment("通过");
			}
			variables.put("msg", "pass");
			variables.put("role","business");
		}else if(state==2){
			variables.put("msg", "reject");
			actTaskService.completeOtherTask(taskId,variables);
		}
		User user=UserUtils.getUser();
		Authentication.setAuthenticatedUserId( "【风险】"+ user.getName());// 设置用户id
		actTaskService.complete(taskId,processInstanceId,review.getComment(),variables);
		addMessage(redirectAttributes, "操作成功");
		return "redirect:"+adminPath+ ActConstant.MY_TASK_LIST;

	}

	@RequestMapping(value = {"/audit/law"})
	public String auditLawView(String id,String taskId, Model model) throws Exception{
		Contract contract=contractService.get(id);
		List<Comment> comments=actTaskService.getTaskHistoryCommentList( taskId);
		ContAttach contAttach=new ContAttach();
		contAttach.setContractId(contract.getId());
		List<ContAttach> list = contAttachService.findList(contAttach);
		model.addAttribute("contAttachs", list);
		model.addAttribute("taskId", taskId);
		model.addAttribute("contract",contract);
		model.addAttribute("comments",comments);
		model.addAttribute("review", new ContractReview());
		model.addAttribute("readonly",true);
		model.addAttribute("fileClass","2" );
		Area area=new Area();
		Area parent=new Area();
		parent.setId("1");
		area.setParent(parent);
		List<Area> provinceList =areaService.findList(area);
		model.addAttribute("provinceList",  provinceList );

		area=new Area();
		parent=new Area();
		if(contract!=null&&contract.getProvince()!=null&&(!StringUtils.isEmpty(contract.getProvince().getId()))){

			parent.setId(contract.getProvince().getId());
		}else{
			parent.setId( provinceList.get(0).getId());
		}
		area.setParent(parent);
		List<Area> cityList =areaService.findList(area);
		model.addAttribute("cityList",  cityList );
		return "modules/contract/proc/audit/auditLaw";
	}


	@RequestMapping(value = {"/audit/lawSave"},method= RequestMethod.POST)
	public String auditLawSave(ContractReview review,RedirectAttributes redirectAttributes) throws Exception{
		Task task=actTaskService.getTask( review.getTaskId());

		String taskId=task.getId();
		String processInstanceId = task.getProcessInstanceId(); // 获取流程实例id
		Map<String, Object> variables=new HashMap<String,Object>();
		int state=review.getState();
		if( state==1){
			if(review.getComment()==null||review.getComment().equals("")){
				review.setComment("通过");
			}
			variables.put("msg", "pass");
			variables.put("role","business");
		}else if(state==2){
			variables.put("msg", "reject");
			actTaskService.completeOtherTask(taskId,variables);
		}
		User user=UserUtils.getUser();
		Authentication.setAuthenticatedUserId("【法律】"+user.getName());// 设置用户id
		actTaskService.complete(taskId,processInstanceId,review.getComment(),variables);
		addMessage(redirectAttributes, "操作成功");
		return "redirect:"+adminPath+ ActConstant.MY_TASK_LIST;

	}

	@RequestMapping(value = {"/audit/busi"})
	public String  auditBusiView(String id,String taskId, Model model) throws Exception{
		Contract contract=contractService.get(id);
		List<Comment> comments=actTaskService.getTaskHistoryCommentList( taskId);
		ContAttach contAttach=new ContAttach();
		contAttach.setContractId(contract.getId());
		List<ContAttach> list = contAttachService.findList(contAttach);
		model.addAttribute("contAttachs", list);
		model.addAttribute("taskId", taskId);
		model.addAttribute("contract",contract);
		model.addAttribute("comments",comments);
		model.addAttribute("review", new BaseReview());
		model.addAttribute("readonly",true);
		//model.addAttribute("fileClass","2" );
		Area area=new Area();
		Area parent=new Area();
		parent.setId("1");
		area.setParent(parent);
		List<Area> provinceList =areaService.findList(area);
		model.addAttribute("provinceList",  provinceList );

		area=new Area();
		parent=new Area();
		if(contract!=null&&contract.getProvince()!=null&&(!StringUtils.isEmpty(contract.getProvince().getId()))){

			parent.setId(contract.getProvince().getId());
		}else{
			parent.setId( provinceList.get(0).getId());
		}
		area.setParent(parent);
		List<Area> cityList =areaService.findList(area);
		model.addAttribute("cityList",  cityList );
		return "modules/contract/proc/audit/auditBusiness";
	}


	@RequestMapping(value = {"/audit/busiSave"},method= RequestMethod.POST)
	public String auditBusiSave(BaseReview review,RedirectAttributes redirectAttributes) {
		Task task=actTaskService.getTask( review.getTaskId());
		String taskId=task.getId();
		String contractId=(String)actTaskService.getTaskVariable(taskId,	"businessId");
		String processInstanceId = task.getProcessInstanceId(); // 获取流程实例id
		Map<String, Object> variables=new HashMap<String,Object>();
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
		Authentication.setAuthenticatedUserId("【运营】"+user.getName());// 设置用户id
		actTaskService.complete(taskId,processInstanceId,review.getComment(),variables);
		if( state==1){
			Contract contract=contractService.get(contractId);
			contract.setStatus(3);
			contractService.save(contract);

		}
		addMessage(redirectAttributes, "操作成功");
		return "redirect:"+adminPath+ ActConstant.MY_TASK_LIST;
	}


	@RequestMapping(value = {"/audit/improve"})
	public String auditImproveView(String id,String taskId, Model model) throws Exception{
		Contract contract=contractService.get(id);
		List<Comment> comments=actTaskService.getTaskHistoryCommentList( taskId);
		ContAttach contAttach=new ContAttach();
		contAttach.setContractId(contract.getId());
		List<ContAttach> list = contAttachService.findList(contAttach);
		model.addAttribute("contAttachs", list);
		model.addAttribute("taskId", taskId);
		model.addAttribute("contract",contract);
		model.addAttribute("comments",comments);
		model.addAttribute("review", new BaseReview());
		model.addAttribute("readonly",false );
		model.addAttribute("fileClass","1" );
		Area area=new Area();
		Area parent=new Area();
		parent.setId("1");
		area.setParent(parent);
		List<Area> provinceList =areaService.findList(area);
		model.addAttribute("provinceList",  provinceList );

		area=new Area();
		parent=new Area();
		if(contract!=null&&contract.getProvince()!=null&&(!StringUtils.isEmpty(contract.getProvince().getId()))){

			parent.setId(contract.getProvince().getId());
		}else{
			parent.setId( provinceList.get(0).getId());
		}
		area.setParent(parent);
		List<Area> cityList =areaService.findList(area);
		model.addAttribute("cityList",  cityList );
		return "modules/contract/proc/audit/auditImprove";
	}





	@RequestMapping(value = {"/audit/improveSave"},method= RequestMethod.POST)
	public String  auditImproveSave(BaseReview review,RedirectAttributes redirectAttributes) {
		Task task=actTaskService.getTask( review.getTaskId());
		String taskId=task.getId();
		String processInstanceId = task.getProcessInstanceId(); // 获取流程实例id
		Map<String, Object> variables=new HashMap<String,Object>();
		variables.put("msg", "pass");
		if(review.getComment()==null||review.getComment().equals("")){
			review.setComment("通过");
		}
		variables.put("role","contract");
		User user=UserUtils.getUser();
		Authentication.setAuthenticatedUserId("【创建者】"+user.getName());// 设置用户id
		actTaskService.complete(taskId,processInstanceId,review.getComment(),variables);
		addMessage(redirectAttributes, "操作成功");
		return "redirect:"+adminPath+ ActConstant.MY_TASK_LIST;
	}


	/**
	 * 合同附件上传
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/audit/upload", method=RequestMethod.POST)
	public String attachUpload(String contractId, String fileRemark,String fileClassUpload,String taskIdUpload, MultipartFile file, RedirectAttributes redirectAttributes)throws Exception {
		try {
			Contract contract = get(contractId);
			Date date = contract.getCreateDate();
			String year = DateUtils.formatDate(date, "yyyy");
			String fileType = file.getOriginalFilename().split("[.]")[1];

			String ftpPath = "/" + ContConstant.CONTRACT_FILE_PATH + "/" + year + "/";
			String path = Global.getUserfilesBaseDir();
			String fileName = ContConstant.CONTRACT_FILE_PREFIX + DateUtils.getDate("yyyyMMddHHmmssSSS") + "." + fileType;
			File f = new File(path, fileName);
			if (f.isFile()) {
				FileUtils.deleteFile(path + "/" + fileName);
			}

			FileUtils.writeByteArrayToFile(f, file.getBytes());
			FTPUtil ftpUtil = new FTPUtil();
			ftpUtil.uploadFile(ftpPath, fileName, path + "/" + fileName);


			ContAttach contAttach = new ContAttach();
			contAttach.setContractId(contract.getId());
			contAttach.setUrl(ftpPath+fileName);
			contAttach.setPath(ftpPath);

			contAttach.setType(Integer.valueOf(fileClassUpload));
			contAttach.preInsert();
			contAttach.setIsNewRecord(true);
			contAttach.setRemark(fileRemark);
			contAttach.setFile(fileName);
			contAttachService.save(contAttach);
			addMessage(redirectAttributes, "文件上传成功");
		}catch (Exception e){
			e.printStackTrace();
			addMessage(redirectAttributes, "文件上传失败");
		}
	/*	UserContract contract=contractService.get(contractId);
		List<Comment> comments=actTaskService.getTaskHistoryCommentList( taskIdUpload);
		ContAttach contAttach=new ContAttach();
		contAttach.setContractId(contract.getId());
		List<ContAttach> list = contAttachService.findList(contAttach);
		model.addAttribute("contAttachs", list);
		model.addAttribute("taskId",  taskIdUpload);
		model.addAttribute("contract",contract);
		model.addAttribute("comments",comments);
		model.addAttribute("review", new BaseReview());
		model.addAttribute("readonly",false );
		model.addAttribute("type",type );
		if(type.equals("1")) {
			return "modules/contract/auditImprove";
		}else{
			return "modules/contract/auditLaw";
		}*/
		if(fileClassUpload.equals("1")) {
			return "redirect:" + Global.getAdminPath() + "/cont/proc/audit/improve?taskId=" + taskIdUpload + "&id=" + contractId;
		}else{
			return "redirect:" + Global.getAdminPath() + "/cont/proc/audit/law?taskId=" + taskIdUpload + "&id=" +contractId;
		}

	}

	@RequestMapping(value = "/audit/delete")
	public String delete(String id,String taskId,String fileClass, RedirectAttributes redirectAttributes)throws Exception {
		ContAttach contAttach=contAttachService.get(id);
		try {

			String path = Global.getUserfilesBaseDir();
			FTPUtil ftpUtil = new FTPUtil();
			ftpUtil.deleteFile(contAttach.getPath(),contAttach.getFile());
			contAttachService.delete(contAttach);
			addMessage( redirectAttributes, "附件删除成功");
		}catch (Exception e){
			e.printStackTrace();
			addMessage( redirectAttributes, "附件删除失败");
		}
		if(fileClass.equals("1")) {
			return "redirect:" + Global.getAdminPath() + "/cont/proc/audit/improve?taskId=" + taskId + "&id=" + contAttach.getContractId();
		}else{
			return "redirect:" + Global.getAdminPath() + "/cont/proc/audit/law?taskId=" + taskId + "&id=" + contAttach.getContractId();
		}
	}


	/**
	 * 合同信息保存
	 *
	 * @return
	 */


	@RequestMapping(value = "/audit/save")
	public String save(Contract contract,String taskIdContract,RedirectAttributes redirectAttributes)throws Exception {

		contractService.save(contract);
		addMessage(redirectAttributes, "合同保存成功");
		return "redirect:"+Global.getAdminPath()+"/cont/proc/audit/improve?taskId="+taskIdContract+"&id="+contract.getId();
	}

//
//	/**
//	 * 发起请款审批
//	 *
//	 * @param id //合同id
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping("/apply/start")
//	@ResponseBody
//	public Map applyStart(String id) throws Exception {
//		ContApply contApply=contApplyService.get(id);
//		Map<String,Object> result=new HashMap<String, Object>();
//		try {
//			Map<String,Object> variables = null;
//			variables = new HashMap<String, Object>();
//			variables.put("businessId", id);
//			variables.put("role","finance");
//			String processInstanceId=actTaskService.startProcess(ActConstant.APPLY_PAY_PROCESS_KEY, ContConstant.CONT_APPYLY_TABLE_NAME,id, contApply.getReceiptName(),variables);
//			actTaskService.completeFirstTask(processInstanceId);
//			contApply=contApplyService.get(id);
//			contApply.setStatus(2);
//			//改合同状态
//			contApplyService.save(contApply);
//			result.put("result","success");
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			result.put("result","error");
//
//		} finally {
//			return result;
//		}
//
//
//	}
//
//	@RequestMapping(value = {"/apply/finance"})
//	public String   applyFinanceView(String id,String taskId, Model model) throws Exception{
//		ContApply contApply=contApplyService.get(id);
//
//		FinanceVO finance=new FinanceVO();
//		finance.setName(contApply.getReceiptName());
//		finance.setTaxId(contApply.getTaxId());
//		finance.setAddressPhone(contApply.getReceiptAddress()+" "+contApply.getReceiptPhone());
//		finance.setBankAccount(contApply.getReceiptBank()+" "+contApply.getReceiptAccount());
//		finance.setContent(contApply.getReceiptContent());
//		finance.setValue(contApply.getReceiptValue().doubleValue());
//		finance.setReceiptRemark(contApply.getReceiptRemark());
//		//
//		finance.setRemark(contApply.getRemark());
//		finance.setReceiptDate(contApply.getReceiptDate());
//		model.addAttribute("finance", finance);
//		List<Comment> comments=actTaskService.getTaskHistoryCommentList( taskId);
//		model.addAttribute("taskId", taskId);
//		model.addAttribute("contApply",contApply);
//		model.addAttribute("comments",comments);
//		model.addAttribute("review", new BaseReview());
//		model.addAttribute("readonly",true);
//		return "modules/contract/proc/applyPay/applyPayFinance";
//	}
//
//	@RequestMapping(value = {"/apply/financeSave"},method= RequestMethod.POST)
//	public String  applyFinanceSave(BaseReview review,RedirectAttributes redirectAttributes) {
//		Task task=actTaskService.getTask( review.getTaskId());
//		String taskId=task.getId();
//		String contApplyId=(String)actTaskService.getTaskVariable(taskId,"businessId");
//		String processInstanceId = task.getProcessInstanceId(); // 获取流程实例id
//		Map<String, Object> variables=new HashMap<String,Object>();
//		variables.put("msg", "pass");
//		int state=review.getState();
//		if( state==1){
//			if(review.getComment()==null||review.getComment().equals("")){
//				review.setComment("通过");
//			}
//
//			variables.put("msg", "pass");
//		}else if(state==2){
//			variables.put("msg", "reject");
//		}
//		User user=UserUtils.getUser();
//		Authentication.setAuthenticatedUserId("【财务】"+user.getName());// 设置用户id
//		actTaskService.complete(taskId,processInstanceId,review.getComment(),variables);
//		if( state==1) {
//			ContApply contApply = contApplyService.get(contApplyId);
//			contApply.setStatus(3);
//			contApplyService.save(contApply);
//		}
//		addMessage(redirectAttributes, "操作成功");
//		return "redirect:"+adminPath+ ActConstant.MY_TASK_LIST;
//	}
//
//	@RequestMapping(value = {"/apply/improve"})
//	public String   applyImproveView(String id,String taskId, Model model) throws Exception{
//		ContApply contApply=contApplyService.get(id);
//		List<Comment> comments=actTaskService.getTaskHistoryCommentList( taskId);
//		model.addAttribute("taskId", taskId);
//		model.addAttribute("contApply",contApply);
//		model.addAttribute("comments",comments);
//		model.addAttribute("review", new BaseReview());
//		model.addAttribute("readonly",false);
//		return "modules/contract/proc/applyPay/applyPayImprove";
//	}
//
//	@RequestMapping(value = {"/apply/improveSave"},method= RequestMethod.POST)
//	public String  applyImproveSave(BaseReview review,RedirectAttributes redirectAttributes) {
//		Task task=actTaskService.getTask( review.getTaskId());
//		String taskId=task.getId();
//		String processInstanceId = task.getProcessInstanceId(); // 获取流程实例id
//		Map<String, Object> variables=new HashMap<String,Object>();
//		variables.put("msg", "pass");
//		int state=review.getState();
//		User user=UserUtils.getUser();
//		Authentication.setAuthenticatedUserId("【创建者】"+user.getName());// 设置用户id
//		actTaskService.complete(taskId,processInstanceId,review.getComment(),variables);
//		addMessage(redirectAttributes, "操作成功");
//		return "redirect:"+adminPath+ ActConstant.MY_TASK_LIST;
//	}
//
//
//	@RequestMapping(value = "/apply/save")
//	public String applyPaysave(ContApply contApply,String taskIdApplyPay, Model model, RedirectAttributes redirectAttributes) {
//		try {
//            ContApply saveContApply=contApplyService.get(contApply.getId());
//            saveContApply.setRemark(contApply.getRemark());
//            saveContApply.setReceiptName(contApply.getReceiptName());
//            saveContApply.setReceiptValue(contApply.getReceiptValue());
//            saveContApply.setReceiptAccount(contApply.getReceiptAccount());
//            saveContApply.setReceiptAddress(contApply.getReceiptAddress());
//            saveContApply.setReceiptBank(contApply.getReceiptBank());
//            saveContApply.setReceiptDate(contApply.getReceiptDate());
//            saveContApply.setReceiptContent(contApply.getReceiptContent());
//            saveContApply.setReceiptPhone(contApply.getReceiptPhone());
//            saveContApply.setReceiptRemark(contApply.getReceiptRemark());
//            saveContApply.setTaxId(contApply.getTaxId());
//            contApplyService.save(saveContApply);
//			addMessage(redirectAttributes, "保存请款信息成功");
//		}catch (Exception e){
//			e.printStackTrace();
//			addMessage(redirectAttributes, "保存请款信息失败");
//		}
//
//
//		return "redirect:"+Global.getAdminPath()+"/cont/proc/apply/improve?taskId="+taskIdApplyPay+"&id="+contApply.getId();
//	}





}