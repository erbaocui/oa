/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.web;

import com.sun.corba.se.pept.transport.ContactInfo;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.converter.OfficeConverter;
import com.thinkgem.jeesite.common.service.converter.PdfConverter;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.FTPUtil;
import com.thinkgem.jeesite.common.utils.FileUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.act.service.ActTaskService;
import com.thinkgem.jeesite.modules.contract.constant.ContConstant;
import com.thinkgem.jeesite.modules.contract.entity.ContApply;
import com.thinkgem.jeesite.modules.contract.entity.ContAttach;
import com.thinkgem.jeesite.modules.contract.entity.Contract;
import com.thinkgem.jeesite.modules.contract.entity.QueryContract;
import com.thinkgem.jeesite.modules.contract.service.ContApplyService;
import com.thinkgem.jeesite.modules.contract.service.ContAttachService;
import com.thinkgem.jeesite.modules.contract.service.ContService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 合同管理Controller
 * @author cuijp
 * @version 2018-04-19
 */
@Controller
@RequestMapping(value = "${adminPath}/cont/base")
public class ContController extends BaseController {

	@Autowired
	private ContService contService;

	@Autowired
	private ContApplyService contApplyService;
	@Autowired
	private ContAttachService contAttachService;




	@ModelAttribute
	public Contract get(@RequestParam(required=false) String id) {
		Contract entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = contService.get(id);
		}
		if (entity == null){
			entity = new Contract();
		}
		return entity;
	}

	/**
	 * 验证名是否有效
	 * @param oldCode
	 * @param code
	 * @return
	 */
	@ResponseBody

	@RequestMapping(value = "checkCode")
	public String checkCode(String oldCode, String code) {
		if ( code !=null &&  code.equals(oldCode)) {
			return "true";
		} else if (code!=null) {
			Contract contract=new Contract();
			contract.setCode(code);
			Contract cont=contService.get(contract);
			if(cont==null){
				return "true";
			}

		}
		return "false";
	}


	@RequiresPermissions("cont:base:view")
	@RequestMapping(value = {"list", ""})
	public String list(QueryContract queryContract, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Contract> page = contService.findPage(new Page<Contract>(request, response), queryContract);
		model.addAttribute("page", page);
		model.addAttribute("queryContract", queryContract);
		return "modules/contract/contractList";
	}

	@RequiresPermissions("cont:base:view")
	@RequestMapping(value = {"briefList", ""})
	public String briefList(QueryContract queryContract,String notContractIds, HttpServletRequest request, HttpServletResponse response, Model model) {
		System.out.println(notContractIds);
		if(!StringUtils.isEmpty(notContractIds.trim())){
			queryContract.setNoInArray(notContractIds.trim().split(","));
		}
		Page<Contract> page=new Page<Contract>(request, response);
		page.setPageSize(10);
		page = contService.findPage(page, queryContract);
		model.addAttribute("page", page);
		model.addAttribute("queryContract", queryContract);
		return "modules/contract/contractBriefList";
	}

	@RequiresPermissions("cont:base:view")
	@RequestMapping(value = "form")
	public String form(Contract contract, Model model) {
		model.addAttribute("contract", contract);
		return "modules/contract/contractForm";
	}

	@RequiresPermissions("cont:base:edit")
	@RequestMapping(value = "save")
	public String save(Contract contract, MultipartFile file,MultipartFile ultimateFile,Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, contract)){
			return form(contract, model);
		}
		contService.save(contract);
		addMessage(redirectAttributes, "合同保存成功");
		return "redirect:"+adminPath+"/cont/base/list/?repage";
	}
	
	@RequiresPermissions("cont:base:edit")
	@RequestMapping(value = "delete")
	public String delete(Contract contract, RedirectAttributes redirectAttributes) {
		contService.delete(contract);
		addMessage(redirectAttributes, "删除合同成功");
		return "redirect:"+Global.getAdminPath()+"/cont/base/list/?repage";
	}
	/**
	 * 请款附件上传
	 * @param
	 * @return
	 */

	@RequiresPermissions("cont:base:view")
	@RequestMapping(value = {"applyPay", ""})
	public String applyPay(Contract contract, HttpServletRequest request, HttpServletResponse response, Model model) {
		ContApply contApply=new ContApply();
		contApply.setContractId(contract.getId());
		List<ContApply> list = contApplyService.findList(contApply);
		model.addAttribute("contApplys", list);
		model.addAttribute("contract",contract);
		return "modules/contract/contractApplyPay";
	}

	/**
	 * 请款附件上传
	 * @param file
	 * @return
	 */
	@RequiresPermissions("cont:base:edit")
	@RequestMapping(value = "/applyPayUpload", method=RequestMethod.POST)
	public String applyPayUpload(String contractId,String remark, MultipartFile file,RedirectAttributes redirectAttributes)throws Exception {
		try {
			Contract contract = get(contractId);
			Date date = contract.getCreateDate();
			String year = DateUtils.formatDate(date, "yyyy");
			String fileType = file.getOriginalFilename().split("[.]")[1];

			String ftpPath = "/" + ContConstant.APPLY_PAY_FILE_PATH + "/" + year + "/";
			String path = Global.getUserfilesBaseDir();
			String fileName = ContConstant.APPLY_PAY_FILE_PREFIX + DateUtils.getDate("yyyyMMddHHmmssSSS") + "." + fileType;
			File f = new File(path, fileName);
			if (f.isFile()) {
				FileUtils.deleteFile(path + "/" + fileName);
			}

			FileUtils.writeByteArrayToFile(f, file.getBytes());
			FTPUtil ftpUtil = new FTPUtil();
			ftpUtil.uploadFile(ftpPath, fileName, path + "/" + fileName);


			ContApply contApply = new ContApply();
			contApply.setContractId(contract.getId());
			contApply.setUrl(ftpPath+"/"+fileName);
			contApply.setPath(ftpPath);
			contApply.setFile(fileName);
			contApply.preInsert();
			contApply.setIsNewRecord(true);
			contApply.setRemark(remark);
			contApplyService.save(contApply);
			addMessage( redirectAttributes, "合同附件上传成功");
		}catch (Exception e){
			e.printStackTrace();
			addMessage( redirectAttributes, "合同附件上传失败");
		}


		return "redirect:"+Global.getAdminPath()+"/cont/base/applyPay/?id="+contractId;
	}

	@RequestMapping(value="/applyPayDownload")
	public ResponseEntity<byte[]>
	applyPayDownload(HttpServletRequest request,String id, Model model)throws Exception {
		//下载文件路径
		ContApply contApply=contApplyService.get(id);
		String path = Global.getUserfilesBaseDir();
		FTPUtil ftpUtil = new FTPUtil();
		ftpUtil.downloadFile(contApply.getPath(),contApply.getFile(),path);
		File file = new File(path + "/" + contApply.getFile());
		HttpHeaders headers = new HttpHeaders();
		//下载显示的文件名，解决中文名称乱码问题
		String downloadFielName = new String(contApply.getFile().getBytes("UTF-8"),"iso-8859-1");
		//通知浏览器以attachment（下载方式）打开图片
		headers.setContentDispositionFormData("attachment", downloadFielName);
		//application/octet-stream ： 二进制流数据（最常见的文件下载）。
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),headers, HttpStatus.CREATED);
	}

	@RequiresPermissions("cont:base:view")
	@RequestMapping(value = {"applyPayPreview", ""})
	public String applyPayPreview(String id , Model model, HttpServletRequest request) throws Exception{

		ContApply contApply=contApplyService.get(id);
		String path = Global.getUserfilesBaseDir();
		FTPUtil ftpUtil = new FTPUtil();
		ftpUtil.downloadFile(contApply.getPath(),contApply.getFile(),path);
		String pdfFileName = contApply.getFile().split("[.]")[0]+".pdf";
		if((contApply.getFile().indexOf("doc")>-1)||(contApply.getFile().indexOf("docx")>-1)){
			OfficeConverter officeConverter=new OfficeConverter();

			officeConverter.convert2PDF(path + "/" + contApply.getFile(),path + "/"+pdfFileName);
		}
		Thread.sleep(3000);
		PdfConverter pdfConverter=new PdfConverter();


		pdfConverter.File2Swf(path +pdfFileName,contApply.getFile().split("[.]")[0]);
		System.out.println(path + "/"+pdfFileName);
		System.out.println(contApply.getFile().split("[.]")[0]);
		String swfFileName = contApply.getFile().split("[.]")[0]+".swf";
		swfFileName ="/upload/"+swfFileName;
		model.addAttribute("file",  swfFileName);
		return "modules/contract/preview";
	}

	@RequiresPermissions("cont:base:edit")
	@RequestMapping(value = "/applyPayDelete", method=RequestMethod.GET)
	public String applyPayDelete(String id,String contractId,RedirectAttributes redirectAttributes)throws Exception {
		try {
			ContApply contApply=contApplyService.get(id);
			String path = Global.getUserfilesBaseDir();
			FTPUtil ftpUtil = new FTPUtil();
			ftpUtil.deleteFile(contApply.getPath(),contApply.getFile());
			contApplyService.delete(contApply);
			addMessage( redirectAttributes, "请款附件删除成功");
		}catch (Exception e){
			e.printStackTrace();
			addMessage( redirectAttributes, "请款附件删除失败");
		}
		ContApply contApply = new ContApply();
		contApply.setContractId(contractId);
		return "redirect:"+Global.getAdminPath()+"/cont/base/applyPay/?id="+contractId;
	}

	/**
	 * 合同附件上传
	 * @param
	 * @return
	 */

	@RequiresPermissions("cont:base:view")
	@RequestMapping(value = {"attach", ""})
	public String attach(Contract contract, HttpServletRequest request, HttpServletResponse response, Model model) {
		ContAttach contAttach=new ContAttach();
		contAttach.setContractId(contract.getId());
		List<ContAttach> list = contAttachService.findList(contAttach);
		model.addAttribute("contAttachs", list);
		model.addAttribute("contract",contract);
		return "modules/contract/contractAttach";
	}

	/**
	 * 合同附件上传
	 * @param file
	 * @return
	 */
	@RequiresPermissions("cont:base:edit")
	@RequestMapping(value = "/attachUpload", method=RequestMethod.POST)
	public String attachUpload(String contractId,String remark, MultipartFile file,RedirectAttributes redirectAttributes)throws Exception {
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
			contAttach.setUrl(ftpPath+"/"+fileName);
			contAttach.setPath(ftpPath);
			contAttach.preInsert();
			contAttach.setIsNewRecord(true);
			contAttach.setRemark(remark);
			contAttach.setFile(fileName);
			contAttachService.save(contAttach);
			addMessage( redirectAttributes, "合同附件上传成功");
		}catch (Exception e){
			e.printStackTrace();
			addMessage( redirectAttributes, "合同附件上传失败");
		}

		return "redirect:"+Global.getAdminPath()+"/cont/base/attach/?id="+contractId;
	}
	/**
	 * 合同附件下载
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/attachDownload")
	public ResponseEntity<byte[]>
	attachDownload(String id, Model model)throws Exception {
		//下载文件路径
		ContAttach contAttach=contAttachService.get(id);
		String path = Global.getUserfilesBaseDir();
		FTPUtil ftpUtil = new FTPUtil();
		ftpUtil.downloadFile(contAttach.getPath(),contAttach.getFile(),path);
		File file = new File(path + "/" + contAttach.getFile());
		HttpHeaders headers = new HttpHeaders();
		//下载显示的文件名，解决中文名称乱码问题
		String downloadFielName = new String(contAttach.getFile().getBytes("UTF-8"),"iso-8859-1");
		//通知浏览器以attachment（下载方式）打开图片
		headers.setContentDispositionFormData("attachment", downloadFielName);
		//application/octet-stream ： 二进制流数据（最常见的文件下载）。
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),headers, HttpStatus.CREATED);
	}

	@RequiresPermissions("cont:base:view")
	@RequestMapping(value = {"attachPreview", ""})
	public String attachPreview(String id , Model model, HttpServletRequest request) throws Exception{

		ContAttach contAttach=contAttachService.get(id);
		String path = Global.getUserfilesBaseDir();
		FTPUtil ftpUtil = new FTPUtil();
		ftpUtil.downloadFile( contAttach.getPath(), contAttach.getFile(),path);
		String pdfFileName =  contAttach.getFile().split("[.]")[0]+".pdf";
		if(( contAttach.getFile().indexOf("doc")>-1)||( contAttach.getFile().indexOf("docx")>-1)){
			OfficeConverter officeConverter=new OfficeConverter();

			officeConverter.convert2PDF(path + "/" +  contAttach.getFile(),path + "/"+pdfFileName);
		}
		Thread.sleep(3000);
		PdfConverter pdfConverter=new PdfConverter();


		pdfConverter.File2Swf(path +pdfFileName,contAttach.getFile().split("[.]")[0]);

		String swfFileName = contAttach.getFile().split("[.]")[0]+".swf";
		swfFileName ="/upload/"+swfFileName;
		model.addAttribute("file",  swfFileName);
		return "modules/contract/preview";
	}

	@RequiresPermissions("cont:base:edit")
	@RequestMapping(value = "/attachDelete", method=RequestMethod.GET)
	public String attachDelete(String id,String contractId,RedirectAttributes redirectAttributes)throws Exception {
		try {
			ContAttach contAttach=contAttachService.get(id);
			String path = Global.getUserfilesBaseDir();
			FTPUtil ftpUtil = new FTPUtil();
			ftpUtil.deleteFile(contAttach.getPath(),contAttach.getFile());
			contAttachService.delete(contAttach);
			addMessage( redirectAttributes, "请款附件删除成功");
		}catch (Exception e){
			e.printStackTrace();
			addMessage( redirectAttributes, "请款附件删除失败");
		}
		ContApply contApply = new ContApply();
		contApply.setContractId(contractId);
		return "redirect:"+Global.getAdminPath()+"/cont/base/attach/?id="+contractId;
	}


}