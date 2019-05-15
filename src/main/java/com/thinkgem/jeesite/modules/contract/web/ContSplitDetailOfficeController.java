/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.modules.sys.entity.Office;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.contract.entity.ContSplitDetailOffice;
import com.thinkgem.jeesite.modules.contract.service.ContSplitDetailOfficeService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 合同拆分细化部门Controller
 * @author cuijp
 * @version 2019-05-07
 */
@Controller
@RequestMapping(value = "${adminPath}/cont/split/detail/office")
public class ContSplitDetailOfficeController extends BaseController {

	@Autowired
	private ContSplitDetailOfficeService contSplitDetailOfficeService;
	
	@ModelAttribute
	public ContSplitDetailOffice get(@RequestParam(required=false) String id) {
		ContSplitDetailOffice entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = contSplitDetailOfficeService.get(id);
		}
		if (entity == null){
			entity = new ContSplitDetailOffice();
		}
		return entity;
	}
	
	@RequiresPermissions("contract:contSplitDetailOffice:view")
	@RequestMapping(value = {"list", ""})
	public String list(ContSplitDetailOffice contSplitDetailOffice, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ContSplitDetailOffice> page = contSplitDetailOfficeService.findPage(new Page<ContSplitDetailOffice>(request, response), contSplitDetailOffice); 
		model.addAttribute("page", page);
		return "modules/contract/contSplitDetailOfficeList";
	}

	@RequiresPermissions("contract:contSplitDetailOffice:view")
	@RequestMapping(value = "form")
	public String form(ContSplitDetailOffice contSplitDetailOffice, Model model) {
		model.addAttribute("contSplitDetailOffice", contSplitDetailOffice);
		return "modules/contract/contSplitDetailOfficeForm";
	}

	//@RequiresPermissions("contract:contSplitDetailOffice:edit")
	@RequestMapping(value = "save")
	@ResponseBody
	public String save(ContSplitDetailOffice contSplitDetailOffice,String officeId, Model model, RedirectAttributes redirectAttributes) {

		// BigDecimal value,String itemId,String remark,
		//ContSplitDetailOffice contSplitDetailOffice=new ContSplitDetailOffice();
		//contSplitDetailOffice.setItemId(itemId);
		//contSplitDetailOffice.setValue(value);
		//contSplitDetailOffice.setRemark(remark);
		Office office=new Office();
		office.setId(officeId);
		contSplitDetailOffice.setOffice(office);

		contSplitDetailOfficeService.save(contSplitDetailOffice);
		return "success";
	}
	
	//@RequiresPermissions("contract:contSplitDetailOffice:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public Map delete(ContSplitDetailOffice contSplitDetailOffice,String detailId,String contractId,Boolean readonly,RedirectAttributes redirectAttributes) {
		contSplitDetailOfficeService.delete(contSplitDetailOffice);
		//addMessage(redirectAttributes, "删除合同拆分细化部门成功");
		//return "redirect:"+Global.getAdminPath()+"/cont/split/detail/list?id="+detailId+"&contractId="+contractId+"&readonly="+readonly+"&repage";
		Map<String,Object> result=new HashMap<String, Object>();
		result.put("result","success");
		result.put("msg","成功");
		return  result;
	}

	@RequestMapping(value = "check")
	@ResponseBody
	public Map check(String itemId, String officeId, RedirectAttributes redirectAttributes) {
		ContSplitDetailOffice contSplitDetailOffice=new ContSplitDetailOffice();
		Office office =new Office();
		office.setId(officeId);
		contSplitDetailOffice.setOffice(office);
		contSplitDetailOffice.setItemId(itemId);
		ContSplitDetailOffice detailOffice=contSplitDetailOfficeService.getOne(contSplitDetailOffice);
		Map<String,Object> result=new HashMap<String, Object>();
		if(detailOffice==null){
			result.put("result","success");
			result.put("msg","成功");
		}else{
			result.put("result","error");
			result.put("msg","已存在");
		}
		return  result;
	}


}