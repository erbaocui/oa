/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.income.web;

import com.thinkgem.jeesite.common.utils.NumberOperateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.act.constant.ActConstant;
import com.thinkgem.jeesite.modules.act.entity.BaseReview;
import com.thinkgem.jeesite.modules.act.service.ActTaskService;
import com.thinkgem.jeesite.modules.contract.constant.ContConstant;
import com.thinkgem.jeesite.modules.contract.entity.Contract;
import com.thinkgem.jeesite.modules.contract.service.ContService;
import com.thinkgem.jeesite.modules.income.constant.IncomeConstant;
import com.thinkgem.jeesite.modules.income.entity.*;
import com.thinkgem.jeesite.modules.income.service.*;
import com.thinkgem.jeesite.modules.income.vo.*;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 进款分配流程Controller
 * @author cuijp
 * @version 2018-05-28
 */
@Controller
@RequestMapping(value = "${adminPath}/income/distProc")
public class DistProcController extends BaseController {

	@Autowired
	private IncomeService incomeService;
	@Autowired
	private RuleService ruleService;
	@Autowired
	private RuleItemService ruleItemService;
	@Autowired
	private DistOfficeService distOfficeService;
	@Autowired
	private RuleGroupService ruleGroupService;
	@Autowired
	private DistributeService distributeService;


	@Autowired
	private ActTaskService actTaskService;
	@Autowired
	private OfficeService officeService;




	/**
	 * 发起分配流程
	 *
	 * @param id //incomeId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/start")
	@ResponseBody
	public Map start(String id) throws Exception {
		Map<String,Object> result=new HashMap<String, Object>();
		try {
			Map<String,Object> variables = null;
			variables = new HashMap<String, Object>();
			variables.put("businessId", id);

			String processInstanceId=actTaskService.startProcess(ActConstant.INCOME_DISTRIBUTE_PROCESS_KEY, IncomeConstant.INCOME_TABLE_NAME,id,ActConstant.INCOME_DISTRIBUTE_PROCESS_TITLE,variables);
			//String processInstanceId=actTaskService.startProcess(ActConstant.INCOME_DISTRIBUTE_PROCESS_KEY, IncomeConstant.INCOME_TABLE_NAME,id,ActConstant.INCOME_DISTRIBUTE_PROCESS_TITLE);
			actTaskService.completeFirstTask(processInstanceId);
			Income income =incomeService.get(id);
			income.setStatus(2);
			incomeService.save(income);// 修改请假单状态*/
			result.put("result","success");

		} catch (Exception e) {
			e.printStackTrace();
			result.put("result","error");

		} finally {
			return result;
		}


	}
    //分配部门
	@RequestMapping(value = {"officeDist"})
	public String officeDist(String id,String taskId, Model model) throws Exception{
		Income income=incomeService.get(id);
		List<Comment> comments=actTaskService.getTaskHistoryCommentList(taskId);
		model.addAttribute("taskId", taskId);
		model.addAttribute("income", income);
		model.addAttribute("comments",comments);
		model.addAttribute("review", new BaseReview());
		return "modules/income/distOffice";
	}

	@RequestMapping(value = {"officeDistSubmit"},method= RequestMethod.POST)
	public String distOfficeSubmit(BaseReview review,RedirectAttributes redirectAttributes) {
		Task task=actTaskService.getTask( review.getTaskId());
		String taskId=task.getId();
		String processInstanceId = task.getProcessInstanceId(); // 获取流程实例id
		Map<String, Object> variables=new HashMap<String,Object>();
		variables.put("role","contractAuditor");

		User user=UserUtils.getUser();
		Authentication.setAuthenticatedUserId(  user.getName()+ "【"+UserUtils.getUser().getLoginName()+"】");// 设置用户id
		actTaskService.complete(taskId,processInstanceId,review.getComment(),variables);
		addMessage(redirectAttributes, "操作成功");
		return "redirect:"+adminPath+ ActConstant.MY_TASK_LIST;

	}

    //分配规则
	@RequestMapping(value = "distRule")
	public String distRule(DistOfficeProc distOfficeProc, String[] groups, Model model)  throws Exception{
		List<DistOfficeVo> distOffices=new ArrayList<DistOfficeVo>();

		String incomeId=rule(distOfficeProc,groups,distOffices);
		List<Comment> comments=actTaskService.getTaskHistoryCommentList(distOfficeProc.getTaskId());
		model.addAttribute("taskId", distOfficeProc.getTaskId());
		model.addAttribute("comments",comments);
		model.addAttribute("review", new BaseReview());
		model.addAttribute("incomeId", incomeId);
		model.addAttribute("distOffices", distOffices);
		return "modules/income/distRule";
	}


	@RequestMapping(value = {"distRuleSubmit"},method= RequestMethod.POST)
	public String distRuleSubmit(DistOfficeProc distOfficeProc,String[] groups, BaseReview review,RedirectAttributes redirectAttributes)  throws Exception{


		    Task task=actTaskService.getTask(  distOfficeProc.getTaskId());
			String taskId=task.getId();
			String processInstanceId = task.getProcessInstanceId(); // 获取流程实例id
			Map<String, Object> variables=new HashMap<String,Object>();
			int state=review.getState();
			if( state==1){
				variables.put("msg", "pass");
				String incomeId =(String)task.getProcessVariables().get("businessId");
				DistOffice distOffice=new DistOffice();
				distOffice.setIncomeId(incomeId);
				List<DistOffice> distOfficeList= distOfficeService.findList(distOffice);
				List<String> userLoginNameList=new ArrayList<String>();
				for( DistOffice item:distOfficeList){
					String officeId= item.getOffice().getId();
					Office office=(Office)officeService.get(officeId);
					String loginName=office.getPrimaryPerson().getLoginName();
					userLoginNameList.add(loginName);
				}
				variables.put("assigneeList",userLoginNameList);
				ruleSave( distOfficeProc,groups);
			}else if(state==2){
				variables.put("msg", "reject");
			}

			User user=UserUtils.getUser();
			Authentication.setAuthenticatedUserId(  user.getName()+ "【"+UserUtils.getUser().getLoginName()+"】");// 设置用户id
			actTaskService.complete(taskId,processInstanceId,review.getComment(),variables);
			addMessage(redirectAttributes, "操作成功");
			return "redirect:"+adminPath+ ActConstant.MY_TASK_LIST;

	}


	//部门审核
	@RequestMapping(value = "officeAudit")
	public String officeAudit(DistOfficeProc distOfficeProc, String[] groups, Model model)  throws Exception{
		List<DistOfficeVo> distOffices=new ArrayList<DistOfficeVo>();
		Office office=new Office();
		office.setId(UserUtils.getUser().getOffice().getId());
		distOfficeProc.setOffice(office);
		String incomeId=rule(distOfficeProc,groups,distOffices);
		List<Comment> comments=actTaskService.getTaskHistoryCommentList(distOfficeProc.getTaskId());
		model.addAttribute("taskId", distOfficeProc.getTaskId());
		model.addAttribute("comments",comments);
		model.addAttribute("review", new BaseReview());
		model.addAttribute("incomeId", incomeId);
		model.addAttribute("distOffices", distOffices);
		return "modules/income/distOfficeAudit";
	}


	@RequestMapping(value = {"officeAuditSubmit"},method= RequestMethod.POST)
	public String officeAuditSubmit(DistOfficeProc distOfficeProc,String[] groups, BaseReview review,RedirectAttributes redirectAttributes)  throws Exception{

		Task task=actTaskService.getTask(  distOfficeProc.getTaskId());
		String taskId=task.getId();
		String processInstanceId = task.getProcessInstanceId(); // 获取流程实例id
		Map<String, Object> variables=new HashMap<String,Object>();
		int state=review.getState();
		if( state==1){
			variables.put("msg", "pass");
		}else if(state==2){
			variables.put("msg", "reject");
			//actTaskService.completeBrotherTask(taskId,variables);
		}
		variables.put("role","business");
		User user=UserUtils.getUser();
		Authentication.setAuthenticatedUserId(  user.getName()+ "【"+UserUtils.getUser().getLoginName()+"】");// 设置用户id
		actTaskService.complete(taskId,processInstanceId,review.getComment(),variables);
		/*if(state==2) {
			actTaskService.jumpTask(processInstanceId,"usertask11",variables);
		}*/
		addMessage(redirectAttributes, "操作成功");
		return "redirect:"+adminPath+ ActConstant.MY_TASK_LIST;

	}



	//运营审核
	@RequestMapping(value = "busAudit")
	public String busAudit(DistOfficeProc distOfficeProc, String[] groups, Model model)  throws Exception{
		List<DistOfficeVo> distOffices=new ArrayList<DistOfficeVo>();
		String incomeId=rule(distOfficeProc,groups,distOffices);
		List<Comment> comments=actTaskService.getTaskHistoryCommentList(distOfficeProc.getTaskId());
		model.addAttribute("taskId", distOfficeProc.getTaskId());
		model.addAttribute("comments",comments);
		model.addAttribute("review", new BaseReview());
		model.addAttribute("incomeId", incomeId);
		model.addAttribute("distOffices", distOffices);
		return "modules/income/distBusAudit";
	}


	@RequestMapping(value = {"busAuditSubmit"},method= RequestMethod.POST)
	public String busAuditSubmit(DistOfficeProc distOfficeProc,String[] groups, BaseReview review,RedirectAttributes redirectAttributes)  throws Exception{

		Task task=actTaskService.getTask(  distOfficeProc.getTaskId());
		String taskId=task.getId();
		String processInstanceId = task.getProcessInstanceId(); // 获取流程实例id
		Map<String, Object> variables=new HashMap<String,Object>();
		int state=review.getState();
		/*if( state==1){
			variables.put("msg", "pass");
			variables.put("role","finance");
		}else if(state==2){
			variables.put("msg", "reject");
			variables.put("role","contractAuditor");
		}*/
		variables.put("msg", "reject");
		variables.put("role","contractAuditor");

		User user=UserUtils.getUser();
		Authentication.setAuthenticatedUserId(  user.getName()+ "【"+UserUtils.getUser().getLoginName()+"】");// 设置用户id
		actTaskService.complete(taskId,processInstanceId,review.getComment(),variables);
		addMessage(redirectAttributes, "操作成功");
		return "redirect:"+adminPath+ ActConstant.MY_TASK_LIST;

	}

	//运营审核
	@RequestMapping(value = "finAudit")
	public String finAudit(DistOfficeProc distOfficeProc, String[] groups, Model model)  throws Exception{
		List<DistOfficeVo> distOffices=new ArrayList<DistOfficeVo>();
		String incomeId=rule(distOfficeProc,groups,distOffices);
		List<Comment> comments=actTaskService.getTaskHistoryCommentList(distOfficeProc.getTaskId());
		model.addAttribute("taskId", distOfficeProc.getTaskId());
		model.addAttribute("comments",comments);
		model.addAttribute("review", new BaseReview());
		model.addAttribute("incomeId", incomeId);
		model.addAttribute("distOffices", distOffices);
		return "modules/income/distFinAudit";
	}


	@RequestMapping(value = {"finAuditSubmit"},method= RequestMethod.POST)
	public String finAuditSubmit(DistOfficeProc distOfficeProc,String[] groups, BaseReview review,RedirectAttributes redirectAttributes)  throws Exception{

		Task task=actTaskService.getTask(  distOfficeProc.getTaskId());
		String taskId=task.getId();
		String processInstanceId = task.getProcessInstanceId(); // 获取流程实例id
		String incomeId=(String)task.getProcessVariables().get("businessId");
		Map<String, Object> variables=new HashMap<String,Object>();
		int state=review.getState();
		if( state==1){
			variables.put("msg", "pass");
			distributeService.saveAcount(distOfficeProc.getIncomeId());
		}else if(state==2){
			variables.put("msg", "reject");
		}
		User user=UserUtils.getUser();
		Authentication.setAuthenticatedUserId(  user.getName()+ "【"+UserUtils.getUser().getLoginName()+"】");// 设置用户id
		actTaskService.complete(taskId,processInstanceId,review.getComment(),variables);
		addMessage(redirectAttributes, "操作成功");
		return "redirect:"+adminPath+ ActConstant.MY_TASK_LIST;

	}











	private String  rule(DistOffice distOffice, String[] groups,List<DistOfficeVo> distOffices)throws Exception{
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
      return incomeId;
	}

	private void ruleSave(DistOffice distOffice, String[] groups)throws Exception{
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
	}
}