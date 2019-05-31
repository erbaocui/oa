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
import com.thinkgem.jeesite.modules.contract.entity.ContAttach;
import com.thinkgem.jeesite.modules.contract.entity.Contract;
import com.thinkgem.jeesite.modules.contract.proc.ContractReview;
import com.thinkgem.jeesite.modules.contract.service.ContAttachService;
import com.thinkgem.jeesite.modules.contract.service.ContService;
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
public class ContProcAuditController extends BaseController {

	@Autowired
	private ContService contractService;

	@Autowired
	private ActTaskService actTaskService;

	@Autowired
	private ContAttachService contAttachService;


	@Autowired
	private AreaService areaService;




	/**
	 * 发起合同审批
	 *
	 * @param id //合同id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("start")
	@ResponseBody
	public Map start(String id) throws Exception {
		Contract contract=contractService.get(id);
		Map<String,Object> result=new HashMap<String, Object>();
		try {
			Map<String,Object> variables = null;
			variables = new HashMap<String, Object>();
			variables.put("businessId", id);
			variables.put("role","contract");
			String processInstanceId=actTaskService.startProcess( ContConstant.PROCESS_KEY_CONTRACT_AUDIT, ContConstant.CONTRACT_TABLE_NAME,id,ContConstant.PROCESS_TITLE_AUDIT,variables);
			actTaskService.completeFirstTask(processInstanceId);
			contract =contractService.get(id);
			contract.setStatus(2);
			//改合同状态
			contractService.save(contract);
			result.put("result","success");

		} catch (Exception e) {
			e.printStackTrace();
			result.put("result","error");

		} finally {
			return result;
		}


	}

	@RequestMapping(value = {"administrator"})
	public String administrator(String id,String taskId, Model model) throws Exception{
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
		return "modules/contract/proc/audit/administrator";
	}

	@RequestMapping(value = {"administrator/submit"},method= RequestMethod.POST)
	public String administratorSubmit(ContractReview review,RedirectAttributes redirectAttributes) throws Exception{
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


	@RequestMapping(value = {"risk"})
	public String risk(String id,String taskId, Model model) throws Exception{
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
		return "modules/contract/proc/audit/risk";
	}

	@RequestMapping(value = {"risk/submit"},method= RequestMethod.POST)
	public String riskSubmit(BaseReview review,RedirectAttributes redirectAttributes)throws Exception{
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

	@RequestMapping(value = {"law"})
	public String law(String id,String taskId, Model model) throws Exception{
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
		return "modules/contract/proc/audit/law";
	}


	@RequestMapping(value = {"law/submit"},method= RequestMethod.POST)
	public String lawSubmit(ContractReview review,RedirectAttributes redirectAttributes) throws Exception{
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

	@RequestMapping(value = {"business"})
	public String  business(String id,String taskId, Model model) throws Exception{
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
		return "modules/contract/proc/audit/business";
	}


	@RequestMapping(value = {"business/submit"},method= RequestMethod.POST)
	public String businessSubmit(BaseReview review,RedirectAttributes redirectAttributes) {
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


	@RequestMapping(value = {"improve"})
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
		return "modules/contract/proc/audit/improve";
	}





	@RequestMapping(value = {"improve/submit"},method= RequestMethod.POST)
	public String  improveSubmit(BaseReview review,RedirectAttributes redirectAttributes) {
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
	@RequestMapping(value = "attach/upload", method=RequestMethod.POST)
	public String attachUpload(String contractId, String fileRemark,String fileClassUpload,String taskIdUpload, MultipartFile file, RedirectAttributes redirectAttributes)throws Exception {
		try {
			Contract contract = contractService.get(contractId);
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

			contAttach.setType(fileClassUpload);
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

		if(fileClassUpload.equals("1")) {
			return "redirect:" + Global.getAdminPath() + "/cont/audit/proc/improve?taskId=" + taskIdUpload + "&id=" + contractId;
		}else{
			return "redirect:" + Global.getAdminPath() + "/cont/audit/proc/law?taskId=" + taskIdUpload + "&id=" +contractId;
		}

	}

	@RequestMapping(value = "attach/delete")
	public String attachDelete(String id,String taskId,String fileClass, RedirectAttributes redirectAttributes)throws Exception {
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
			return "redirect:" + Global.getAdminPath() + "/cont/audit/proc/improve?taskId=" + taskId + "&id=" + contAttach.getContractId();
		}else{
			return "redirect:" + Global.getAdminPath() + "//cont/audit/proc/law?taskId=" + taskId + "&id=" + contAttach.getContractId();
		}
	}


	/**
	 * 合同信息保存
	 *
	 * @return
	 */


	@RequestMapping(value = "save")
	public String save(Contract contract,String taskIdContract,RedirectAttributes redirectAttributes)throws Exception {

		contractService.save(contract);
		addMessage(redirectAttributes, "合同保存成功");
		return "redirect:"+Global.getAdminPath()+"/cont/audit/proc/improve?taskId="+taskIdContract+"&id="+contract.getId();
	}







}