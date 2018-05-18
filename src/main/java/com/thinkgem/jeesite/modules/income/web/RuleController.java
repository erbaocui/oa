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
import com.thinkgem.jeesite.modules.income.entity.Rule;
import com.thinkgem.jeesite.modules.income.service.RuleService;

/**
 * 分配规则Controller
 * @author cuijp
 * @version 2018-05-04
 */
@Controller
@RequestMapping(value = "${adminPath}/income/rule")
public class RuleController extends BaseController {

	@Autowired
	private RuleService ruleService;
	
	@ModelAttribute
	public Rule get(@RequestParam(required=false) String id) {
		Rule entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = ruleService.get(id);
		}
		if (entity == null){
			entity = new Rule();
		}
		return entity;
	}
	
	@RequiresPermissions("income:rule:view")
	@RequestMapping(value = {"list", ""})
	public String list(Rule rule, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Rule> page = ruleService.findPage(new Page<Rule>(request, response), rule); 
		model.addAttribute("page", page);
		return "modules/income/ruleList";
	}

	@RequiresPermissions("income:rule:view")
	@RequestMapping(value = "form")
	public String form(Rule rule, Model model) {
		model.addAttribute("rule", rule);
		return "modules/income/ruleForm";
	}

	@RequiresPermissions("income:rule:edit")
	@RequestMapping(value = "save")
	public String save(Rule rule, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, rule)){
			return form(rule, model);
		}
		ruleService.save(rule);
		addMessage(redirectAttributes, "保存分配规则成功");
		return "redirect:"+Global.getAdminPath()+"/income/rule/?repage";
	}
	
	@RequiresPermissions("income:rule:edit")
	@RequestMapping(value = "delete")
	public String delete(Rule rule, RedirectAttributes redirectAttributes) {
		ruleService.delete(rule);
		addMessage(redirectAttributes, "删除分配规则成功");
		return "redirect:"+Global.getAdminPath()+"/income/rule/?repage";
	}

}