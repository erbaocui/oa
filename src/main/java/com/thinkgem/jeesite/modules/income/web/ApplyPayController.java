/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.income.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.modules.income.entity.ApplyPayCont;
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
import com.thinkgem.jeesite.modules.income.entity.ApplyPay;
import com.thinkgem.jeesite.modules.income.service.ApplyPayService;

/**
 * 请款功能Controller
 * @author cuijp
 * @version 2018-05-02
 */
@Controller
@RequestMapping(value = "${adminPath}/income/applyPay")
public class ApplyPayController extends BaseController {

	@Autowired
	private ApplyPayService applyPayService;
	
	@ModelAttribute
	public ApplyPay get(@RequestParam(required=false) String id) {
		ApplyPay entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = applyPayService.get(id);
		}
		if (entity == null){
			entity = new ApplyPay();
		}
		return entity;
	}
	
	@RequiresPermissions("income:applyPay:view")
	@RequestMapping(value = {"list", ""})
	public String list(ApplyPay applyPay, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ApplyPay> page = applyPayService.findPage(new Page<ApplyPay>(request, response), applyPay);
		model.addAttribute("page", page);
		return "modules/income/applyPayList";
	}

	@RequiresPermissions("income:applyPay:view")
	@RequestMapping(value = "form")
	public String form(ApplyPay applyPay, Model model) {
		applyPay=get(applyPay.getId());
		model.addAttribute("applyPay", applyPay);
		return "modules/income/applyPayForm";
	}


	@RequiresPermissions("income:applyPay:edit")
	@RequestMapping(value = "save")
	public String save(ApplyPay applyPay, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, applyPay)){
			return form(applyPay, model);
		}
		applyPayService.save(applyPay);
		addMessage(redirectAttributes, "保存成功");
		return "redirect:"+Global.getAdminPath()+"/income/applyPay/?repage";
	}

	@RequiresPermissions("income:applyPay:edit")
	@RequestMapping(value = "delete")
	public String delete(ApplyPay applyPay, RedirectAttributes redirectAttributes) {
		applyPayService.delete(applyPay);
		addMessage(redirectAttributes, "删除成功");
		return "redirect:"+Global.getAdminPath()+"/income/applyPay/?repage";
	}

	@RequiresPermissions("income:applyPay:edit")
	@RequestMapping(value = "contDelete")
	public String contDelete(ApplyPay applyPay,String contractId,Model model) {
		ApplyPayCont applyPayCont = new ApplyPayCont();
		applyPayCont.setApplyPayId(applyPay.getId());
		applyPayCont.setContractId(contractId);
		applyPayService.deleteContract(applyPayCont);
		applyPay = get(applyPay.getId());
		model.addAttribute("applyPay",applyPay);
		addMessage(model, "删除合同成功");
		return "modules/income/applyPayForm";
	}

	@RequiresPermissions("income:applyPay:edit")
	@RequestMapping(value = "contAdd")
	public String contAdd(ApplyPay applyPay,String contractId,Model model) {
		ApplyPayCont applyPayCont = new ApplyPayCont();

		applyPayCont.setApplyPayId(applyPay.getId());
		applyPayCont.setContractId(contractId);
		applyPayService.addContract(applyPayCont);
		applyPay = get(applyPay.getId());
		model.addAttribute("applyPay",applyPay);

		addMessage(model, "添加合同成功");
		return "modules/income/applyPayForm";
	}


}