/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.income.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.thinkgem.jeesite.modules.income.entity.AccountFlow;
import com.thinkgem.jeesite.modules.income.service.AccountFlowService;

/**
 * 账户流水Controller
 * @author cuijp
 * @version 2018-05-22
 */
@Controller
@RequestMapping(value = "${adminPath}/income/accountFlow")
public class AccountFlowController extends BaseController {

	@Autowired
	private AccountFlowService accountFlowService;
	
	@ModelAttribute
	public AccountFlow get(@RequestParam(required=false) String id) {
		AccountFlow entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = accountFlowService.get(id);
		}
		if (entity == null){
			entity = new AccountFlow();
		}
		return entity;
	}
	
	@RequiresPermissions("income:accountFlow:view")
	@RequestMapping(value = {"list", ""})
	public String list(AccountFlow accountFlow, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<AccountFlow> page = accountFlowService.findPage(new Page<AccountFlow>(request, response), accountFlow); 
		model.addAttribute("page", page);
		return "modules/income/account/accountFlowList";
	}

	@RequiresPermissions("income:accountFlow:view")
	@RequestMapping(value = "form")
	public String form(AccountFlow accountFlow, Model model) {
		model.addAttribute("accountFlow", accountFlow);
		return "modules/income/accountFlowForm";
	}

	@RequiresPermissions("income:accountFlow:edit")
	@RequestMapping(value = "save")
	public String save(AccountFlow accountFlow, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, accountFlow)){
			return form(accountFlow, model);
		}
		accountFlowService.save(accountFlow);
		addMessage(redirectAttributes, "保存账户流水成功");
		return "redirect:"+Global.getAdminPath()+"/income/accountFlow/?repage";
	}
	
	@RequiresPermissions("income:accountFlow:edit")
	@RequestMapping(value = "delete")
	public String delete(AccountFlow accountFlow, RedirectAttributes redirectAttributes) {
		accountFlowService.delete(accountFlow);
		addMessage(redirectAttributes, "删除账户流水成功");
		return "redirect:"+Global.getAdminPath()+"/income/accountFlow/?repage";
	}

}