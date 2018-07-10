/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.modules.contract.entity.Contract;
import com.thinkgem.jeesite.modules.contract.service.ContService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.contract.entity.ContItem;
import com.thinkgem.jeesite.modules.contract.service.ContItemService;

import java.util.List;

/**
 * 合同付款约定Controller
 * @author cuijp
 * @version 2018-07-01
 */
@Controller
@RequestMapping(value = "${adminPath}/cont/contItem")
public class ContItemController extends BaseController {

	@Autowired
	private ContItemService contItemService;
	@Autowired
	private ContService contService;
	
	@ModelAttribute
	public ContItem get(@RequestParam(required=false) String id) {
		ContItem entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = contItemService.get(id);
		}
		if (entity == null){
			entity = new ContItem();
		}
		return entity;
	}
	



	@RequestMapping(value = "list")
	public String form(ContItem contItem,Boolean readonly,Model model) {
        Contract contract=contService.get( contItem.getContractId());
		List<ContItem> list=contItemService.findList(contItem);
		model.addAttribute("contractId", contItem.getContractId());
		model.addAttribute("contItems",list);
		model.addAttribute("readonly", readonly);
		model.addAttribute("contract", contract);
		return "modules/contract/contractPayItem";
	}


	@RequestMapping(value = "save")
	public String save(ContItem contItem,Boolean readonly, RedirectAttributes redirectAttributes) {
		/*if (!beanValidator(model, contItem)){
			return form(contItem, model);
		}*/
		contItemService.save(contItem);
		addMessage(redirectAttributes, "保存付款约定成功");
		return "redirect:"+Global.getAdminPath()+"/cont/contItem/list?contractId="+contItem.getContractId()+"&readonly="+readonly;
	}

	@RequestMapping(value = "delete")
	public String delete(ContItem contItem,Boolean readonly, RedirectAttributes redirectAttributes) {
		contItemService.delete(contItem);
		addMessage(redirectAttributes, "删除付款约定成功");

		return "redirect:"+Global.getAdminPath()+"/cont/contItem/list?contractId="+contItem.getContractId()+"&readonly="+readonly;
	}

}