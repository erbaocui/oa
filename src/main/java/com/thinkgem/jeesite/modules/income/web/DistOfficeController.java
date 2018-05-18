/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.income.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.common.utils.NumberOperateUtils;
import com.thinkgem.jeesite.modules.income.entity.DistOffice;
import com.thinkgem.jeesite.modules.income.entity.Rule;
import com.thinkgem.jeesite.modules.income.entity.RuleGroup;
import com.thinkgem.jeesite.modules.income.entity.RuleItem;
import com.thinkgem.jeesite.modules.income.service.RuleGroupService;
import com.thinkgem.jeesite.modules.income.service.RuleItemService;
import com.thinkgem.jeesite.modules.income.service.RuleService;
import com.thinkgem.jeesite.modules.income.vo.*;
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
import com.thinkgem.jeesite.modules.income.service.DistOfficeService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 部门分配Controller
 * @author cuijp
 * @version 2018-05-14
 */
@Controller
@RequestMapping(value = "${adminPath}/income/distOffice")
public class DistOfficeController extends BaseController {

	@Autowired
	private DistOfficeService distOfficeService;
	@Autowired
	private RuleGroupService ruleGroupService;
	@Autowired
	private RuleService ruleService;
	@Autowired
	private RuleItemService ruleItemService;
	
	@ModelAttribute
	public DistOffice get(@RequestParam(required=false) String id) {
		DistOffice entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = distOfficeService.get(id);
		}
		if (entity == null){
			entity = new DistOffice();
		}
		return entity;
	}
	
	@RequiresPermissions("income:distributeOffice:view")
	@RequestMapping(value = {"list", ""})
	public String list(DistOffice distributeOffice, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<DistOffice> page = distOfficeService.findPage(new Page<DistOffice>(request, response), distributeOffice); 
		model.addAttribute("page", page);
		return "modules/income/distributeOfficeList";
	}

	/*@RequiresPermissions("income:distributeOffice:view")*/
	@RequestMapping(value = "form")
	public String form(DistOffice distOffice,String[] groups, Model model) {
		List<DistOfficeVo> distOffices=new ArrayList<DistOfficeVo>();
		List<DistOffice> distOfficeList= distOfficeService.findList(distOffice);
		for(int k=0;k<distOfficeList.size();k++){
			DistOffice item=distOfficeList.get(k);
			DistOfficeVo dov=new DistOfficeVo();
			dov.setId(item.getId());
			dov.setOfficeName(item.getOffice().getName());
			dov.setValue(item.getValue().toString());
			List<RuleGroup> list=ruleGroupService.findListByOfficeId(item.getOffice().getId());
			List<RuleGroupVo> ruleGroups=new ArrayList<RuleGroupVo>();
			RuleGroupVo rgv=new RuleGroupVo();
			if(groups!=null&&(!groups[k].equals("-1"))){
				dov.setRuleGroupId(groups[k]);
			}

			rgv.setId("-1");
			rgv.setName("请选择......");
			ruleGroups.add(rgv);
			for(RuleGroup rg:list){
				rgv=new RuleGroupVo();
				rgv.setId(rg.getId());
				rgv.setName(rg.getName());
				ruleGroups.add(rgv);
			}
			dov.setRuleGroups(ruleGroups);
			if(dov.getRuleGroupId()!=null) {
				Rule r=new Rule();
				r.setGroupId(dov.getRuleGroupId());
				r.setOfficeId(item.getOffice().getId());
				List<Rule> ruleList = ruleService.findListByOfficeId(r);
				//List<Rule> ruleList=ruleService.findList(item.getOffice().getId());
				Integer rowspan = 0;
				List<RuleVo> ruleVoList = new ArrayList<RuleVo>();
				if (ruleList != null && !ruleList.isEmpty()) {
					Integer ruleRowspan = 0;
					for (Rule rule : ruleList) {
						RuleVo rv = new RuleVo();
						if (ruleList.size() == 1) {
							rv.setValue(item.getValue().toString());
						} else {
							//多条规则

						}
						RuleItem ruleItem = new RuleItem();
						ruleItem.setOfficeId(item.getOffice().getId());
						ruleItem.setRuleId(rule.getId());
						List<RuleItem> ruleItemList = ruleItemService.findList(ruleItem);
						List<RuleItemVo> ruleItemVoList = new ArrayList<RuleItemVo>();

						if (ruleItemList != null && !ruleItemList.isEmpty()) {
							for (RuleItem ri : ruleItemList) {
								Double itemValue = 0.0;
								Double tax = 0.0;
								Double filingFee = 0.0;
								itemValue = NumberOperateUtils.mul(new Double(rv.getValue()), ri.getPercent().doubleValue());
								RuleItemVo riv = new RuleItemVo();
								riv.setName(ri.getName());
								riv.setValue(itemValue.toString());
								List<DistributeVo> distributeVoList = new ArrayList<DistributeVo>();
								DistributeVo dbv;
								if (ri.getIsTax().equals(0)) {
									dbv = new DistributeVo();
									dbv.setName("税");
									tax = NumberOperateUtils.mul(itemValue, 0.067);
									dbv.setValue(tax.toString());
									distributeVoList.add(dbv);
								}
								if (ri.getIsFilingFee().equals(0)) {
									dbv = new DistributeVo();
									dbv.setName("归档费");
									filingFee = NumberOperateUtils.sub(itemValue, tax);
									filingFee = NumberOperateUtils.mul(filingFee, 0.02);
									dbv.setValue(filingFee.toString());
									distributeVoList.add(dbv);
								}
								itemValue = NumberOperateUtils.sub(itemValue, tax);
								itemValue = NumberOperateUtils.sub(itemValue, filingFee);
								dbv = new DistributeVo();
								dbv.setName("净值");
								dbv.setValue(itemValue.toString());
								distributeVoList.add(dbv);
								riv.setRowspan(distributeVoList.size());
								rowspan += distributeVoList.size();
								ruleRowspan += distributeVoList.size();
								riv.setDistributes(distributeVoList);
								ruleItemVoList.add(riv);
							}
						}
						rv.setRowspan(ruleRowspan);
						rv.setRoleItems(ruleItemVoList);
						ruleVoList.add(rv);
					}
				}
				dov.setRowspan(rowspan);
				dov.setRules(ruleVoList);
				distOffices.add(dov);
			}else{
				dov.setRowspan(1);
				dov.setRules(null);
				distOffices.add(dov);
			}

		}
		model.addAttribute("distOffices", distOffices);
		//model.addAttribute("distOffice", distOffice);
		return "modules/income/distOfficeForm";
	}

	
	@ResponseBody
	/*@RequiresPermissions("income:distributeOffice:edit")*/
	@RequestMapping(value = "add")
	public String add(DistOffice distOffice, String  officeId,Model model, RedirectAttributes redirectAttributes) {
		Office office=new Office();
		office.setId(officeId);
		distOffice.setOffice(office);
		distOfficeService.save(distOffice);
		return "success";
	}

	@ResponseBody
	/*@RequiresPermissions("income:distributeOffice:edit")*/
	@RequestMapping(value = "delete")
	public String delete(DistOffice distOffice, Model model, RedirectAttributes redirectAttributes) {
		distOfficeService.delete(distOffice);
		return "success";
	}
	
	/*@RequiresPermissions("income:distributeOffice:edit")
	@RequestMapping(value = "delete")
	public String delete(DistOfficeVo distOffice, RedirectAttributes redirectAttributes) {
		distOfficeService.delete(distOffice);
		addMessage(redirectAttributes, "删除部门分配成功");
		return "redirect:"+Global.getAdminPath()+"/income/distributeOffice/?repage";
	}*/

	@ResponseBody
	@RequestMapping(value = "distInit")
	public List distInit(DistOffice distOffice, Model model, RedirectAttributes redirectAttributes) {
		List<DistOfficeVo> distOffices=new ArrayList<DistOfficeVo>();
		List<DistOffice> distOfficeList= distOfficeService.findList(distOffice);
	    for(DistOffice item:distOfficeList){
			DistOfficeVo dov=new DistOfficeVo();
			dov.setId(item.getId());
			dov.setOfficeName(item.getOffice().getName());
			dov.setValue(item.getValue().toString());
			List<RuleGroup> list=ruleGroupService.findListByOfficeId(item.getOffice().getId());
			List<RuleGroupVo> ruleGroups=new ArrayList<RuleGroupVo>();
			for(RuleGroup rg:list){
				RuleGroupVo rgv=new RuleGroupVo();
				rgv.setId(rg.getId());
				rgv.setName(rg.getName());
				ruleGroups.add(rgv);
			}
			dov.setRuleGroups(ruleGroups);
			dov.setRowspan(1);
			distOffices.add(dov);
		}
		return distOffices;
	}

}