/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.income.web;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.common.utils.NumberOperateUtils;
import com.thinkgem.jeesite.modules.income.entity.*;
import com.thinkgem.jeesite.modules.income.service.*;
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
	@Autowired
	private DistributeService distributeService;
	
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
	public String form(DistOffice distOffice,String[] groups, Model model)  throws Exception{
		List<DistOfficeVo> distOffices=new ArrayList<DistOfficeVo>();
		List<DistOffice> distOfficeList= distOfficeService.findList(distOffice);
		String incomeId="";
		for(int k=0;k<distOfficeList.size();k++){
			DistOffice item=distOfficeList.get(k);
			incomeId=item.getIncomeId();
			DistOfficeVo dov=new DistOfficeVo();
			dov.setId(item.getId());
			dov.setOfficeName(item.getOffice().getName());
			dov.setValue(item.getValue().toString());
			List<RuleGroup> list=ruleGroupService.findListByOfficeId(item.getOffice().getId());
			List<RuleGroupVo> ruleGroups=new ArrayList<RuleGroupVo>();
			RuleGroupVo rgv=new RuleGroupVo();
			if(groups!=null&&(!groups[k].equals("-1"))){
				dov.setRuleGroupId(groups[k]);
			}else{
				Map<String,String> paramMap=new HashMap<String, String>();
				paramMap.put("incomeId",item.getIncomeId());
				paramMap.put("officeId",item.getOffice().getId());
				String groupId=distributeService.findGroupId(paramMap);
				if(groupId!=null&&!groupId.equals("")){
					dov.setRuleGroupId(groupId);
				}
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
						Double ruleValue=0d;
						if (ruleList.size() == 1) {
							rv.setValue(item.getValue().toString());
							ruleValue=item.getValue().doubleValue();
						} else {
							//多条规则
							String sql=rule.getBaseSql();
							String conditon=rule.getCondition();
							Double baseValue=ruleService.runSql(sql).doubleValue();

							ScriptEngineManager manager = new ScriptEngineManager();
							ScriptEngine engine = manager.getEngineByName("js");
							engine.put("value", baseValue);
							engine.put("threshold",rule.getThreshold());
							Boolean before= (Boolean)engine.eval(conditon);
							engine.put("value", NumberOperateUtils.add(baseValue,item.getValue().doubleValue()));
							engine.put("threshold", rule.getThreshold());
							Boolean after= (Boolean)engine.eval(conditon);
							//触发条件
							if(before==false||after==true){
								ruleValue=Math.abs(NumberOperateUtils.sub(rule.getThreshold().doubleValue(),NumberOperateUtils.add(baseValue,item.getValue().doubleValue())));
							}
							if(before==true||after==false){
								ruleValue=Math.abs(NumberOperateUtils.sub(rule.getThreshold().doubleValue(),baseValue));
							}
							if(before==true&&after==true){
								ruleValue=item.getValue().doubleValue();

							}
							rv.setValue(ruleValue.toString());
							if(before==false&&after==false){
								continue;
							}


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
								itemValue = NumberOperateUtils.mul(ruleValue, ri.getPercent().doubleValue());
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
		model.addAttribute("incomeId", incomeId);
		model.addAttribute("distOffices", distOffices);
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


	/*@RequiresPermissions("income:distributeOffice:view")*/
	@RequestMapping(value = "saveDist")
	public String saveDist(DistOffice distOffice,String[] groups, Model model)  throws Exception{

		List<DistOffice> distOfficeList= distOfficeService.findList(distOffice);
		List<Distribute> distributeList = new ArrayList<Distribute>();
		for(int k=0;k<distOfficeList.size();k++){
			DistOffice item=distOfficeList.get(k);

			if(groups[k]!=null) {
				Rule r=new Rule();
				r.setGroupId(groups[k]);
				r.setOfficeId(item.getOffice().getId());
				List<Rule> ruleList = ruleService.findListByOfficeId(r);
				if (ruleList != null && !ruleList.isEmpty()) {
					for (Rule rule : ruleList) {

						Double ruleValue=0d;
						if (ruleList.size() == 1) {
							ruleValue=item.getValue().doubleValue();
						} else {
							//多条规则
							String sql=rule.getBaseSql();
							String conditon=rule.getCondition();
							Double baseValue=ruleService.runSql(sql).doubleValue();

							ScriptEngineManager manager = new ScriptEngineManager();
							ScriptEngine engine = manager.getEngineByName("js");
							engine.put("value", baseValue);
							engine.put("threshold",rule.getThreshold());
							Boolean before= (Boolean)engine.eval(conditon);
							engine.put("value", NumberOperateUtils.add(baseValue,item.getValue().doubleValue()));
							engine.put("threshold", rule.getThreshold());
							Boolean after= (Boolean)engine.eval(conditon);
							//触发条件
							if(before==false||after==true){
								ruleValue=Math.abs(NumberOperateUtils.sub(rule.getThreshold().doubleValue(),NumberOperateUtils.add(baseValue,item.getValue().doubleValue())));
							}
							if(before==true||after==false){
								ruleValue=Math.abs(NumberOperateUtils.sub(rule.getThreshold().doubleValue(),baseValue));
							}
							if(before==true&&after==true){
								ruleValue=item.getValue().doubleValue();

							}
							if(before==false&&after==false){
								continue;
							}

						}
						RuleItem ruleItem = new RuleItem();
						ruleItem.setOfficeId(item.getOffice().getId());
						ruleItem.setRuleId(rule.getId());
						List<RuleItem> ruleItemList = ruleItemService.findList(ruleItem);
						if (ruleItemList != null && !ruleItemList.isEmpty()) {
							for (RuleItem ri : ruleItemList) {
								Double itemValue = 0.0;
								Double tax = 0.0;
								Double filingFee = 0.0;
								itemValue = NumberOperateUtils.mul(ruleValue, ri.getPercent().doubleValue());
								RuleItemVo riv = new RuleItemVo();
								riv.setName(ri.getName());
								riv.setValue(itemValue.toString());

								Distribute  distribute;
								if (ri.getIsTax().equals(0)) {
									distribute = new Distribute();
									distribute.preInsert();
									distribute.setRuleItemId(ri.getId());
									distribute.setStatus(1);
									distribute.setAccount(ri.getAccount());
									distribute.setDes("税");
									distribute.setIncomeId(item.getIncomeId());
									tax = NumberOperateUtils.mul(itemValue, 0.067);
									distribute.setValue(new BigDecimal(tax));
									distributeList.add(distribute);
								}
								if (ri.getIsFilingFee().equals(0)) {
									distribute = new Distribute();
									distribute.preInsert();
									distribute.setRuleItemId(ri.getId());
									distribute.setStatus(1);
									distribute.setAccount(ri.getAccount());
									distribute.setDes("归档费");
									distribute.setIncomeId(item.getIncomeId());
									filingFee = NumberOperateUtils.sub(itemValue, tax);
									filingFee = NumberOperateUtils.mul(filingFee, 0.02);
									distribute.setValue(new BigDecimal(filingFee));
									distributeList.add(distribute);
								}
								itemValue = NumberOperateUtils.sub(itemValue, tax);
								itemValue = NumberOperateUtils.sub(itemValue, filingFee);
								distribute = new Distribute();
								distribute.preInsert();
								distribute.setRuleItemId(ri.getId());
								distribute.setStatus(1);
								distribute.setAccount(ri.getAccount());
								distribute.setDes("净值");
								distribute.setIncomeId(item.getIncomeId());
								distribute.setValue(new BigDecimal(itemValue));
								distributeList.add(distribute);

							}
						}

					}
				}

			}

		}
		distributeService.addBatch( distributeList);
		return "modules/income/distOfficeForm";
	}


	@RequestMapping(value = "saveAccount")
	public String saveAccount(String  incomeId, Model model)  throws Exception{
		distributeService.saveAcount(incomeId);
		return "modules/income/distOfficeForm";
	}

}