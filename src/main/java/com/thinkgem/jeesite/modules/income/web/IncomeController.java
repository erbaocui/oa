/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.income.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.modules.contract.entity.ContApply;
import com.thinkgem.jeesite.modules.contract.entity.Contract;
import com.thinkgem.jeesite.modules.contract.service.ContApplyService;
import com.thinkgem.jeesite.modules.contract.service.ContService;
import com.thinkgem.jeesite.modules.income.entity.Apply;
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
import com.thinkgem.jeesite.modules.income.entity.Income;
import com.thinkgem.jeesite.modules.income.service.IncomeService;

import java.math.BigDecimal;
import java.util.List;

/**
 * 收款Controller
 * @author cuijp
 * @version 2018-05-11
 */
@Controller
@RequestMapping(value = "${adminPath}/income/income")
public class IncomeController extends BaseController {

	@Autowired
	private IncomeService incomeService;
	@Autowired
	private ContService contService;
	@Autowired
	private ContApplyService contApplyService;
	
	@ModelAttribute
	public Income get(@RequestParam(required=false) String id) {
		Income entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = incomeService.get(id);
		}
		if (entity == null){
			entity = new Income();
		}
		return entity;
	}
	
	/*@RequiresPermissions("income:income:view")*/
	@RequestMapping(value = {"list", ""})
	public String list(Income income, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Income> page = incomeService.findPage(new Page<Income>(request, response), income);
		model.addAttribute("page", page);
		return "modules/income/incomeList";
	}


	@RequestMapping(value = {"contractIncome", ""})
	public String contractIncome(String contractId, Boolean readonly,HttpServletRequest request, HttpServletResponse response, Model model) {
		Income income=new Income();
		Contract contract=new Contract();
		contract=contService.get(contractId);
		income.setContract(contract);
		List<Income> list = incomeService.findList(income);
		model.addAttribute("incomes",list);
		model.addAttribute("contract",contract);
		model.addAttribute("readonly",readonly);
		return "modules/income/contractIncome";
	}

	/*@RequiresPermissions("income:income:view")*/
	@RequestMapping(value = "form")
	public String form(Income income, Model model) {
		//income=get(income.getId());
		model.addAttribute("income", income);
		return "modules/income/incomeForm";
	}

/*	@RequiresPermissions("income:income:edit")*/
	@RequestMapping(value = "save")
	public String save(Income income, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, income)){
			return form(income, model);
		}
		incomeService.save(income);
		addMessage(redirectAttributes, "保存收款成功");
		return "redirect:"+Global.getAdminPath()+"/income/income/?repage";
	}
	
/*	@RequiresPermissions("income:income:edit")*//*
	@RequestMapping(value = "delete")
	public String delete(Income income, RedirectAttributes redirectAttributes) {
		incomeService.delete(income);
		addMessage(redirectAttributes, "删除收款成功");
		return "redirect:"+Global.getAdminPath()+"/income/income/?repage";
	}*/

	/*@RequiresPermissions("income:applyPay:view")*/
	@RequestMapping(value = "applyPay")
	public String income(String  id, Model model) {
		ContApply contApply=contApplyService.get(id);
		Income income=new Income();
		income.setApplyId(id);
		List<Income> incomes=incomeService.findList(income);
		model.addAttribute("contApply", contApply);
		model.addAttribute("incomes",incomes);
		return "modules/income/contractApplyIncomeForm";
	}

	@ResponseBody
	@RequiresPermissions("income:applyPay:edit")
	@RequestMapping(value = "add")

	public String add(String applyId,Double incomeValue, Model model) {
		ContApply contApply=contApplyService.get(applyId);
		Income income=new Income();
		income.setApplyId(applyId);
		income.setStatus(1);
		income.setValue(new BigDecimal(incomeValue));
		Contract contract=new Contract();
		contract.setId(contApply.getContract().getId());
		income.setContract(contract);
		incomeService.save(income);
		addMessage(model, "添加收款成功");
		//return "redirect:"+Global.getAdminPath()+"/income/applyPayForm/?repage";
		//return "modules/income/applyPayForm";
		return  "success";
	}


/*	@RequiresPermissions("income:applyPay:edit")*/
  @ResponseBody
	@RequestMapping(value = "delete")

	public String  delete(String id, Model model) {
		Income income=new Income();
		income.setId(id);
		incomeService.delete(income);
	    return  "success";
	}


}