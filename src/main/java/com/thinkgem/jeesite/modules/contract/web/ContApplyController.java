/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.web;

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
import com.thinkgem.jeesite.modules.contract.entity.ContApply;
import com.thinkgem.jeesite.modules.contract.service.ContApplyService;

import java.util.List;

/**
 * 合同请款Controller
 * @author cuijp
 * @version 2018-06-07
 */
@Controller
@RequestMapping(value = "${adminPath}/contract/contApply")
public class ContApplyController extends BaseController {

	@Autowired
	private ContApplyService contApplyService;
	
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
	
	@RequiresPermissions("cont:base:view")
	@RequestMapping(value = {"applyPay", ""})
	public String list(ContApply contApply, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<ContApply> list = contApplyService.findList(contApply);
		model.addAttribute("ContApply", list);
		return "modules/contract/contractApplyPay";
	}

	@RequiresPermissions("cont:base:view")
	@RequestMapping(value = "form")
	public String form(ContApply contApply, Model model) {
		model.addAttribute("contApply", contApply);
		return "modules/contract/contApplyForm";
	}

	@RequiresPermissions("cont:base:edit")
	@RequestMapping(value = "save")
	public String save(ContApply contApply, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, contApply)){
			return form(contApply, model);
		}
		contApplyService.save(contApply);
		addMessage(redirectAttributes, "保存合同请款成功");
		return "redirect:"+Global.getAdminPath()+"/contract/contApply/?repage";
	}
	
	@RequiresPermissions("cont:base:edit")
	@RequestMapping(value = "delete")
	public String delete(ContApply contApply, RedirectAttributes redirectAttributes) {
		contApplyService.delete(contApply);
		addMessage(redirectAttributes, "删除合同请款成功");
		return "redirect:"+Global.getAdminPath()+"/contract/contApply/?repage";
	}

}