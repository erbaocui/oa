/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.service.converter.OfficeConverter;
import com.thinkgem.jeesite.common.service.converter.PdfConverter;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.FTPUtil;
import com.thinkgem.jeesite.common.utils.FileUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.contract.constant.ContConstant;
import com.thinkgem.jeesite.modules.contract.entity.ContApply;
import com.thinkgem.jeesite.modules.contract.entity.ContAttach;
import com.thinkgem.jeesite.modules.contract.entity.Contract;
import com.thinkgem.jeesite.modules.contract.service.ContApplyService;
import com.thinkgem.jeesite.modules.contract.service.ContAttachService;
import com.thinkgem.jeesite.modules.contract.service.ContService;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * 合同请款Controller
 * @author cuijp
 * @version 2018-06-07
 */
@Controller
@RequestMapping(value = "${adminPath}/cont/attach")
public class AttachController extends BaseController {

	@Autowired
	private ContAttachService contAttachService;
	@Autowired
	private ContService contService;
	
	@ModelAttribute
	public ContAttach get(@RequestParam(required=false) String id) {
		ContAttach entity = null;
		if (StringUtils.isNotBlank(id)){
			entity =contAttachService.get(id);
		}
		if (entity == null){
			entity = new ContAttach();
		}
		return entity;
	}

	/**
	 * 合同附件上传
	 * @param
	 * @return
	 */

	@RequiresPermissions("cont:base:view")
	@RequestMapping(value = {"list", ""})
	public String list(String  contractId,Boolean readonly, HttpServletRequest request, HttpServletResponse response, Model model) {
		Contract contract=contService.get(contractId);
		ContAttach contAttach=new ContAttach();
		contAttach.setContractId(contractId);
		List<ContAttach> list = contAttachService.findList(contAttach);
		model.addAttribute("contAttachs", list);
		model.addAttribute("contract",contract);
		model.addAttribute("readonly",readonly);
		return "modules/contract/contractAttach";
	}

	/**
	 * 合同附件上传
	 * @param file
	 * @return
	 */

	@RequestMapping(value = "/upload", method=RequestMethod.POST)
	public String upload(String contractId,String remark,boolean readonly, MultipartFile file,RedirectAttributes redirectAttributes)throws Exception {
		try {
			Contract contract = contService.get(contractId);
			Date date = contract.getCreateDate();
			String year = DateUtils.formatDate(date, "yyyy");
			String fileType = file.getOriginalFilename().split("[.]")[1];

			String ftpPath = "/" + ContConstant.CONTRACT_FILE_PATH + "/" + year+"/";
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
			contAttach.setType("1");
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

		return "redirect:"+Global.getAdminPath()+"/cont/attach/list?contractId="+contractId+"&readonly="+readonly;
	}
	/**
	 * 合同附件下载
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/download")
	public ResponseEntity<byte[]>
	download(String id, Model model)throws Exception {
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


	@RequestMapping(value = {"preview", ""})
	public String preview(String id , Model model, HttpServletRequest request) throws Exception{

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


	@RequestMapping(value = "/delete", method=RequestMethod.GET)
	public String delete(String id,String contractId,boolean readonly,RedirectAttributes redirectAttributes)throws Exception {
		try {
			ContAttach contAttach=contAttachService.get(id);
			String path = Global.getUserfilesBaseDir();
			FTPUtil ftpUtil = new FTPUtil();
			ftpUtil.deleteFile(contAttach.getPath(),contAttach.getFile());
			contAttachService.delete(contAttach);
			addMessage( redirectAttributes, "合同附件删除成功");
		}catch (Exception e){
			e.printStackTrace();
			addMessage( redirectAttributes, "合同附件删除失败");
		}
		ContApply contApply = new ContApply();
		contApply.setContractId(contractId);
		return "redirect:"+Global.getAdminPath()+"/cont/attach/list?contractId="+contractId+"&readonly="+readonly;
	}

}