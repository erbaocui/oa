/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.income.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.modules.contract.entity.Contract;
import com.thinkgem.jeesite.modules.income.entity.Income;
import com.thinkgem.jeesite.modules.income.service.IncomeService;
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
import com.thinkgem.jeesite.modules.income.entity.Apply;
import com.thinkgem.jeesite.modules.income.service.ApplyService;

import java.math.BigDecimal;

/**
 * 请款功能Controller
 * @author cuijp
 * @version 2018-05-02
 */
@Controller
@RequestMapping(value = "${adminPath}/income/applyPay")
public class ApplyController extends BaseController {

	@Autowired
	private ApplyService applyService;
	@Autowired
	private IncomeService incomeService;
	
	@ModelAttribute
	public Apply get(@RequestParam(required=false) String id) {
		Apply entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = applyService.get(id);
		}
		if (entity == null){
			entity = new Apply();
		}
		return entity;
	}
	
	@RequiresPermissions("income:applyPay:view")
	@RequestMapping(value = {"list", ""})
	public String list(Apply apply, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Apply> page = applyService.findPage(new Page<Apply>(request, response), apply);
		model.addAttribute("page", page);
		return "modules/income/applyPayList";
	}

	@RequiresPermissions("income:applyPay:view")
	@RequestMapping(value = "form")
	public String form(Apply apply, Model model) {
		apply=get(apply.getId());
		model.addAttribute("apply", apply);
		return "modules/income/applyPayForm";
	}

	@RequiresPermissions("income:applyPay:view")
	@RequestMapping(value = "income")
	public String income(Apply apply, Model model) {
		apply=get(apply.getId());
		model.addAttribute("apply", apply);
		return "modules/income/applyIncomeForm";
	}


	@RequiresPermissions("income:applyPay:edit")
	@RequestMapping(value = "save")
	public String save(Apply applyPay, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, applyPay)){
			return form(applyPay, model);
		}
		applyPay.setStatus(1);
		applyService.save(applyPay);
		addMessage(redirectAttributes, "保存成功");
		return "redirect:"+Global.getAdminPath()+"/income/applyPay/list?repage";
	}

	@RequiresPermissions("income:applyPay:edit")
	@RequestMapping(value = "delete")
	public String delete(Apply applyPay, RedirectAttributes redirectAttributes) {
		applyService.delete(applyPay);
		addMessage(redirectAttributes, "删除成功");
		return "redirect:"+Global.getAdminPath()+"/income/applyPay/list?repage";
	}

	@RequiresPermissions("income:applyPay:view")
	@RequestMapping(value = "")
	public String receive(Apply apply, Model model) {
		apply=get(apply.getId());
		model.addAttribute("apply", apply);
		return "modules/income/applyPayForm";
	}
/*
	@RequiresPermissions("income:applyPay:edit")
	@RequestMapping(value = "contDelete")
	public String contDelete(Apply applyPay, String contractId, Model model) {
		ApplyPayCont applyPayCont = new ApplyPayCont();
		applyPayCont.setApplyPayId(applyPay.getId());
		applyPayCont.setContractId(contractId);
		applyService.deleteContract(applyPayCont);
		applyPay = get(applyPay.getId());
		model.addAttribute("applyPay",applyPay);
		addMessage(model, "删除合同成功");
		return "modules/income/applyPayForm";
	}
*/
    @ResponseBody
	@RequiresPermissions("income:applyPay:edit")
	@RequestMapping(value = "addIncome")

	public String addIncome(String applyId,Double incomeValue, Model model) {
		Apply applyPay=applyService.get(applyId);
    	Income income=new Income();
		income.setApplyId(applyId);
		income.setStatus(1);
		income.setValue(new BigDecimal(incomeValue));
		Contract contract=new Contract();
		contract.setId(applyPay.getContract().getId());
		income.setContract(contract);
		incomeService.save(income);
		model.addAttribute("applyPay",applyPay);
		addMessage(model, "添加收款成功");
		//return "redirect:"+Global.getAdminPath()+"/income/applyPayForm/?repage";
		//return "modules/income/applyPayForm";
		return  "success";
	}


	@RequiresPermissions("income:applyPay:edit")
	@RequestMapping(value = "delIncome")
	public String delIncome(String applyId, String incomeId,Model model) {
		Income income=new Income();
		income.setId(incomeId);
    	incomeService.delete(income);
		Apply apply = get(applyId);
		model.addAttribute("apply", apply);
		addMessage(model, "删除进款成功");
		//redirectAttributes.addAttribute("apply", apply);
		//addMessage(redirectAttributes, "删除合同成功");
		//return "redirect:"+Global.getAdminPath()+"/income/applyPay/?repage";
		return "modules/income/applyIncomeForm";
	}



}