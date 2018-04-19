/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.contract.entity.Contract;
import com.thinkgem.jeesite.modules.contract.entity.QueryContract;
import com.thinkgem.jeesite.modules.contract.service.ContractService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 合同管理Controller
 * @author cuijp
 * @version 2018-04-19
 */
@Controller
@RequestMapping(value = "${adminPath}/contract/contract")
public class ContractController extends BaseController {

	@Autowired
	private ContractService contractService;
	
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
	
	//@RequiresPermissions("contract:contract:view")
	@RequestMapping(value = {"list", ""})
	public String list(QueryContract queryContract, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Contract> page = contractService.findPage(new Page<Contract>(request, response), queryContract);
		model.addAttribute("page", page);

		model.addAttribute("queryContract", queryContract);
		return "modules/contract/contractList";
	}

	//@RequiresPermissions("contract:contract:view")
	@RequestMapping(value = "form")
	public String form(Contract contract, Model model) {
		model.addAttribute("contract", contract);
		return "modules/contract/contractForm";
	}

	//@RequiresPermissions("contract:contract:edit")
	@RequestMapping(value = "save")
	public String save(Contract contract, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, contract)){
			return form(contract, model);
		}
		contractService.save(contract);
		addMessage(redirectAttributes, "保存保存合同成功成功");
		return "redirect:"+Global.getAdminPath()+"/contract/contract/?repage";
	}
	
	@RequiresPermissions("contract:contract:edit")
	@RequestMapping(value = "delete")
	public String delete(Contract contract, RedirectAttributes redirectAttributes) {
		contractService.delete(contract);
		addMessage(redirectAttributes, "删除保存合同成功成功");
		return "redirect:"+Global.getAdminPath()+"/contract/contract/?repage";
	}

}