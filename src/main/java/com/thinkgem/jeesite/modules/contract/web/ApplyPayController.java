/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.common.service.converter.OfficeConverter;
import com.thinkgem.jeesite.common.service.converter.PdfConverter;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.FTPUtil;
import com.thinkgem.jeesite.common.utils.FileUtils;
import com.thinkgem.jeesite.modules.act.service.ActTaskService;
import com.thinkgem.jeesite.modules.contract.constant.ContConstant;
import com.thinkgem.jeesite.modules.contract.entity.Contract;
import com.thinkgem.jeesite.modules.contract.service.ContService;
import org.activiti.engine.task.Comment;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.contract.entity.ContApply;
import com.thinkgem.jeesite.modules.contract.service.ContApplyService;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * 合同请款Controller
 * @author cuijp
 * @version 2018-06-07
 */
@Controller
@RequestMapping(value = "${adminPath}/cont/applyPay")
public class ApplyPayController extends BaseController {

	@Autowired
	private ContApplyService contApplyService;
	@Autowired
	private ContService contService;
	@Autowired
	private ActTaskService actTaskService;
	
	@ModelAttribute
	public ContApply get(@RequestParam(required=false) String id) {
		ContApply entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = contApplyService.get(id);
		}
		if (entity == null){
			entity = new ContApply();
		}
		return entity;
	}

	@RequestMapping(value = {"list", ""})
	public String applyPay(String contractId,String readonly,String single,HttpServletRequest request, HttpServletResponse response, Model model) {
		ContApply contApply=new ContApply();
		Contract contract=contService.get( contractId);
		contApply.setContract(contract);
		List<ContApply> list = contApplyService.findList(contApply);
		model.addAttribute("contApplys", list);
		model.addAttribute("contract",contract);
		model.addAttribute("readonly",readonly);
		model.addAttribute("single",single);
		return "modules/contract/contractApplyPay";
	}


	@RequestMapping(value = "form")
	public String form(ContApply contApply,String contractId,String readonly,String single, Model model) throws Exception{
		if(StringUtils.isEmpty(contApply.getId())){
			contApply=contApplyService.getLast(contractId);
			if(contApply!=null){
				contApply.setId(null);
				contApply.setFileName(null);
				contApply.setPath(null);
				contApply.setRemark(null);
				contApply.setReceiptDate(null);
				contApply.setUrl(null);
				contApply.setReceiptValue(null);
			}else{
				Contract contract=contService.get(contractId);
				contApply=new ContApply();
				contApply.setReceiptName(contract.getFirstParty());
			}
		}
		if( contApply.getProcInsId()!=null){
			if((contApply.getStatus()!=1)&&(contApply.getStatus()!=2)){
				List<Comment> comments=actTaskService.getProcessInstanceCommentList(contApply.getProcInsId());
				model.addAttribute("comments",comments);
			}
		}
		model.addAttribute("contApply", contApply);
		model.addAttribute("contractId", contractId);
		model.addAttribute("readonly",readonly);
		model.addAttribute("single",single);
		return "modules/contract/contractApplyPayForm";
	}


	@RequestMapping(value = "save")
	public String save(ContApply contApply,String contractId,Boolean readonly, MultipartFile file, Model model, RedirectAttributes redirectAttributes) {
		try {
			Contract contract = contService.get( contractId);
			if(file.getSize()>0) {
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


				//ContApply contApply = new ContApply();
				//contApply.setContract(contract);
				contApply.setUrl(ftpPath + "/" + fileName);
				contApply.setPath(ftpPath);
				contApply.setFileName(fileName);

			}
			if(StringUtils.isEmpty(contApply.getId())){
				contApply.setStatus(1);
			}
			contApply.setContract(contract);
			contApplyService.save(contApply);
			addMessage(redirectAttributes, "保存合同请款成功");
		}catch (Exception e){
			e.printStackTrace();
			addMessage(redirectAttributes, "保存合同请款失败");
		}


		return "redirect:"+Global.getAdminPath()+"/cont/applyPay/list?contractId="+contractId+"&readonly="+readonly;
	}
	
	/*@RequiresPermissions("cont:base:edit")
	@RequestMapping(value = "delete")
	public String delete(ContApply contApply, RedirectAttributes redirectAttributes) {
		contApplyService.delete(contApply);
		addMessage(redirectAttributes, "删除合同请款成功");
		return "redirect:"+Global.getAdminPath()+"/contract/contApply/?repage";
	}*/

	/**
	 * 请款附件上传保存
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/upload", method= RequestMethod.POST)
	public String applyPayUpload(String contractId, String remark,Boolean readonly, MultipartFile file, RedirectAttributes redirectAttributes)throws Exception {
		try {
			Contract contract = contService.get(contractId);
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
			contApply.setContract(contract);
			contApply.setUrl(ftpPath+"/"+fileName);
			contApply.setPath(ftpPath);
			contApply.setFileName(fileName);
			contApply.preInsert();
			contApply.setIsNewRecord(true);
			contApply.setRemark(remark);
			contApplyService.save(contApply);
			addMessage( redirectAttributes, "合同附件上传成功");
		}catch (Exception e){
			e.printStackTrace();
			addMessage( redirectAttributes, "合同附件上传失败");
		}


		return "redirect:"+Global.getAdminPath()+"/cont/applyPay/form?contractId="+contractId+"&readonly="+readonly;
	}

	@RequestMapping(value="/download")
	public ResponseEntity<byte[]>
	download(HttpServletRequest request,String id, Model model)throws Exception {
		//下载文件路径
		ContApply contApply=contApplyService.get(id);
		String path = Global.getUserfilesBaseDir();
		FTPUtil ftpUtil = new FTPUtil();
		ftpUtil.downloadFile(contApply.getPath(),contApply.getFileName(),path);
		File file = new File(path + "/" + contApply.getFileName());
		HttpHeaders headers = new HttpHeaders();
		//下载显示的文件名，解决中文名称乱码问题
		String downloadFielName = new String(contApply.getFileName().getBytes("UTF-8"),"iso-8859-1");
		//通知浏览器以attachment（下载方式）打开图片
		headers.setContentDispositionFormData("attachment", downloadFielName);
		//application/octet-stream ： 二进制流数据（最常见的文件下载）。
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = {"preview", ""})
	public String preview(String id , Model model, HttpServletRequest request) throws Exception{

		ContApply contApply=contApplyService.get(id);
		String path = Global.getUserfilesBaseDir();
		FTPUtil ftpUtil = new FTPUtil();
		ftpUtil.downloadFile(contApply.getPath(),contApply.getFileName(),path);
		String pdfFileName = contApply.getFileName().split("[.]")[0]+".pdf";
		if((contApply.getFileName().indexOf("doc")>-1)||(contApply.getFileName().indexOf("docx")>-1)){
			OfficeConverter officeConverter=new OfficeConverter();

			officeConverter.convert2PDF(path + "/" + contApply.getFileName(),path + "/"+pdfFileName);
		}
		Thread.sleep(3000);
		PdfConverter pdfConverter=new PdfConverter();


		pdfConverter.File2Swf(path +pdfFileName,contApply.getFileName().split("[.]")[0]);
		System.out.println(path + "/"+pdfFileName);
		System.out.println(contApply.getFileName().split("[.]")[0]);
		String swfFileName = contApply.getFileName().split("[.]")[0]+".swf";
		swfFileName ="/upload/"+swfFileName;
		model.addAttribute("file",  swfFileName);
		return "modules/contract/preview";
	}

	@RequestMapping(value = "/delete", method=RequestMethod.GET)
	public String delete(String id,String contractId,boolean readonly,RedirectAttributes redirectAttributes)throws Exception {
		try {
			ContApply contApply=contApplyService.get(id);
			String path = Global.getUserfilesBaseDir();
			FTPUtil ftpUtil = new FTPUtil();
			ftpUtil.deleteFile(contApply.getPath(),contApply.getFileName());
			contApplyService.delete(contApply);
			addMessage( redirectAttributes, "请款附件删除成功");
		}catch (Exception e){
			e.printStackTrace();
			addMessage( redirectAttributes, "请款附件删除失败");
		}
		/*ContApply contApply = new ContApply();
		contApply.setContractId(contractId);*/
		return "redirect:"+Global.getAdminPath()+"/cont/applyPay/list?contractId="+contractId+"&readonly="+readonly;
	}


}