/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.income.web;

import com.thinkgem.jeesite.common.utils.MacUtils;
import com.thinkgem.jeesite.common.utils.NumberOperateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.act.constant.ActConstant;
import com.thinkgem.jeesite.modules.act.entity.BaseReview;
import com.thinkgem.jeesite.modules.act.service.ActTaskService;
import com.thinkgem.jeesite.modules.contract.constant.ContConstant;
import com.thinkgem.jeesite.modules.contract.entity.ContApply;
import com.thinkgem.jeesite.modules.contract.entity.Contract;
import com.thinkgem.jeesite.modules.contract.service.ContApplyService;
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
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
//	@Autowired
//	private RuleService ruleService;
//	@Autowired
//	private RuleItemService ruleItemService;
	@Autowired
	private DistOfficeService distOfficeService;
//	@Autowired
//	private RuleGroupService ruleGroupService;
	@Autowired
	private DistributeService distributeService;
//	@Autowired
//	private ContApplyService contApplyService;
	@Autowired
	private DistTypeService distTypeService;
	@Autowired
    private DistService distService;


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
			Map<String,Object> variables = new HashMap<String, Object>();
			variables.put("businessId", id);
			String processInstanceId=actTaskService.startProcess(ActConstant.INCOME_DISTRIBUTE_PROCESS_KEY, IncomeConstant.INCOME_TABLE_NAME,id,ActConstant.INCOME_DISTRIBUTE_PROCESS_TITLE,variables);
			//String processInstanceId=actTaskService.startProcess(ActConstant.INCOME_DISTRIBUTE_PROCESS_KEY, IncomeConstant.INCOME_TABLE_NAME,id,ActConstant.INCOME_DISTRIBUTE_PROCESS_TITLE);
			actTaskService.completeFirstTask(processInstanceId);
			Income income =incomeService.get(id);
			income.setStatus(2);
			incomeService.save(income);//
			result.put("result","success");

		} catch (Exception e) {
			e.printStackTrace();
			result.put("result","error");

		} finally {
			return result;
		}


	}
    //部门分配类型
	@RequestMapping(value = {"officeDist"})
	public String officeDist(DistOfficeProc distOfficeProc, Model model, HttpServletRequest request) throws Exception{
		Task task=actTaskService.getTask(  distOfficeProc.getTaskId());
		String incomeId =(String)actTaskService.getTaskVariable(task.getId(),"businessId");
		//distOfficeProc.setIncomeId(incomeId);
		DistType distType=new DistType();
		distType.setIncomeId(incomeId);
		List<DistType> distTypes=distTypeService.findList(distType);
		Income income=incomeService.get(incomeId);
		HttpSession session=request.getSession();
		session.removeAttribute("showDraw");
		session.removeAttribute("showPlan");
		boolean showDraw=false,showPlan=false;
		for(DistType dt:distTypes){
			if(dt.getType().equals("1")){
				showDraw=true;
				session.setAttribute("showDraw",true);
			}
			if(dt.getType().equals("2")){
				showPlan=true;
				session.setAttribute("showPlan",true);

			}
		}

		List<Comment> comments=actTaskService.getTaskHistoryCommentList(distOfficeProc.getTaskId());
		model.addAttribute("taskId", distOfficeProc.getTaskId());
		model.addAttribute("comments",comments);
		model.addAttribute("review", new BaseReview());
		model.addAttribute("incomeId", incomeId);
		model.addAttribute("distTypes", distTypes);
		model.addAttribute("distType", distType);
		model.addAttribute("income",income);
		model.addAttribute("showDraw",showDraw);
		model.addAttribute("showPlan",showPlan);
		model.addAttribute("drawSave",session.getAttribute("drawSave"));
		model.addAttribute("planSave",session.getAttribute("planSave"));

		return "modules/income/distOffice";
	}

	//部门分配规则
	@RequestMapping(value = {"officeDistRule"})
	public String officeDistRule(DistOfficeProc distOfficeProc, String[] groups,String type,HttpServletRequest request, Model model) throws Exception{
		List<DistOfficeVo> distOffices=new ArrayList<DistOfficeVo>();
		Task task=actTaskService.getTask(  distOfficeProc.getTaskId());
		String incomeId =(String)actTaskService.getTaskVariable(task.getId(),"businessId");
		DistType distType=new DistType();
		distType.setIncomeId(incomeId);
		distType.setType(type);
		distType=distTypeService.get(distType);
		distOfficeProc.setTypeId(distType.getId());
		distService.rule(distOfficeProc,groups,distOffices,type);
		Income income=incomeService.get(incomeId);
		HttpSession session=request.getSession();
		model.addAttribute("taskId", distOfficeProc.getTaskId());
		model.addAttribute("review", new BaseReview());
		model.addAttribute("incomeId", incomeId);
		model.addAttribute("distOffices", distOffices);
		model.addAttribute("income",income);
		model.addAttribute("showDraw",	session.getAttribute("showDraw"));
		model.addAttribute("showPlan",session.getAttribute("showPlan"));
		model.addAttribute("type",type);
		model.addAttribute("distType",distType);
		return "modules/income/distOfficeRule";
	}



	@RequestMapping(value = {"officeDistSave"},method= RequestMethod.POST)
	public String officeDistSave(DistOfficeProc distOfficeProc,String[] groups,String type,HttpServletRequest request,BaseReview review,Model model)  throws Exception{
        distService.ruleSave( distOfficeProc,groups);

		List<DistOfficeVo> distOffices=new ArrayList<DistOfficeVo>();
		Task task=actTaskService.getTask(  distOfficeProc.getTaskId());
		String incomeId =(String)actTaskService.getTaskVariable(task.getId(),"businessId");
		DistType distType=new DistType();
		distType.setIncomeId(incomeId);
		distType.setType(type);
		distType=distTypeService.get(distType);
		distOfficeProc.setTypeId(distType.getId());
        distService.rule(distOfficeProc,groups,distOffices,type);
		Income income=incomeService.get(incomeId);
		HttpSession session=request.getSession();
		model.addAttribute("taskId", distOfficeProc.getTaskId());
		model.addAttribute("review", new BaseReview());
		model.addAttribute("incomeId", incomeId);
		model.addAttribute("distOffices", distOffices);
		model.addAttribute("income",income);
		model.addAttribute("showDraw",	session.getAttribute("showDraw"));
		model.addAttribute("showPlan",session.getAttribute("showPlan"));
		model.addAttribute("type",type);
		model.addAttribute("distType",distType);
		addMessage(model, "保存成功");
		if(type.equals("1")){
			income=incomeService.get(incomeId);
			income.setDraw(2);
			incomeService.save(income);

		}
		if(type.equals("2")){
			income=incomeService.get(incomeId);
			income.setPlan(2);
			incomeService.save(income);


		}
		return "modules/income/distOfficeRule";





	}


	@RequestMapping(value = {"officeDistSubmit"},method= RequestMethod.POST)
	public String officeDistSubmit(DistOfficeProc distOfficeProc,String[] groups,String type, BaseReview review,RedirectAttributes redirectAttributes)  throws Exception{


		    Task task=actTaskService.getTask(  distOfficeProc.getTaskId());
			String taskId=task.getId();
			String processInstanceId = task.getProcessInstanceId(); // 获取流程实例id
		    Map<String, Object> variables=new HashMap<String,Object>();
		    String incomeId =(String)actTaskService.getTaskVariable(taskId,"businessId");

		    List<String> userLoginNameList=distOfficeService.findChiefLoginNameList(incomeId);
			if(review.getComment()==null||review.getComment().equals("")){
			 review.setComment("部门分配完成");
			}
		    variables.put("role","contract");
		    variables.put("msg", "pass");
			variables.put("assigneeList",userLoginNameList);

			User user=UserUtils.getUser();
			Authentication.setAuthenticatedUserId(  "【项目部门】" +user.getName());// 设置用户id
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
		Task task=actTaskService.getTask(  distOfficeProc.getTaskId());
		String incomeId =(String)actTaskService.getTaskVariable(task.getId(),"businessId");
		Income income=incomeService.get(incomeId);
        distOfficeProc.setIncomeId(incomeId);
        distService.rule(distOfficeProc,groups,distOffices,null);
		List<Comment> comments=actTaskService.getTaskHistoryCommentList(distOfficeProc.getTaskId());
		model.addAttribute("taskId", distOfficeProc.getTaskId());
		model.addAttribute("comments",comments);
		model.addAttribute("review", new BaseReview());
		model.addAttribute("incomeId", incomeId);
		model.addAttribute("income",income);
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
			if(review.getComment()==null||review.getComment().equals("")){
				review.setComment("通过");
			}
		}else if(state==2){
			variables.put("msg", "reject");
			if(review.getComment()==null||review.getComment().equals("")){
				review.setComment("反对");
			}

		}
        variables.put("role","contract");
		User user=UserUtils.getUser();
		Authentication.setAuthenticatedUserId("【部门确认】" +user.getName());// 设置用户id
		actTaskService.complete(taskId,processInstanceId,review.getComment(),variables);
		/*if(state==2) {
			actTaskService.jumpTask(processInstanceId,"usertask11",variables);
		}*/
		addMessage(redirectAttributes, "操作成功");
		return "redirect:"+adminPath+ ActConstant.MY_TASK_LIST;

	}

	//合同管理员审核
	@RequestMapping(value = "contAudit")
	public String contAudit(DistOfficeProc distOfficeProc, String[] groups, String type,Model model)  throws Exception{
		List<DistOfficeVo> distOffices=new ArrayList<DistOfficeVo>();
		String incomeId =(String)actTaskService.getTaskVariable(distOfficeProc.getTaskId(),"businessId");
		Income income=incomeService.get(incomeId);
        distOfficeProc.setIncomeId(incomeId);
        distService.rule(distOfficeProc,groups,distOffices,null);
		List<Comment> comments=actTaskService.getTaskHistoryCommentList(distOfficeProc.getTaskId());
		model.addAttribute("taskId", distOfficeProc.getTaskId());
		model.addAttribute("comments",comments);
		model.addAttribute("review", new BaseReview());
		model.addAttribute("incomeId", incomeId);
		model.addAttribute("income", income);
		model.addAttribute("distOffices", distOffices);
		return "modules/income/distContAudit";
	}
	@RequestMapping(value = {"contAuditSubmit"},method= RequestMethod.POST)
	public String contAuditSubmit(DistOfficeProc distOfficeProc,String[] groups, BaseReview review,RedirectAttributes redirectAttributes)  throws Exception{
	    Task task=actTaskService.getTask(  distOfficeProc.getTaskId());
        String taskId=task.getId();
        String processInstanceId = task.getProcessInstanceId(); // 获取流程实例id
        Map<String, Object> variables=new HashMap<String,Object>();
        int state=review.getState();
        if( state==1){
            variables.put("msg", "pass");
            variables.put("role","AllocationCheck");
            if(review.getComment()==null||review.getComment().equals("")){
                review.setComment("通过");
            }
        }else if(state==2){
            variables.put("msg", "reject");
        }
        User user=UserUtils.getUser();
        Authentication.setAuthenticatedUserId( "【合同确认】" +user.getName());// 设置用户id
        actTaskService.complete(taskId,processInstanceId,review.getComment(),variables);
        addMessage(redirectAttributes, "操作成功");
        return "redirect:"+adminPath+ ActConstant.MY_TASK_LIST;

    }




	//经营审核
	@RequestMapping(value = "busAudit")
	public String busAudit(DistOfficeProc distOfficeProc, String[] groups,String type, Model model)  throws Exception{
		List<DistOfficeVo> distOffices=new ArrayList<DistOfficeVo>();
		String incomeId =(String)actTaskService.getTaskVariable(distOfficeProc.getTaskId(),"businessId");
        Income income=incomeService.get(incomeId);
        distOfficeProc.setIncomeId(incomeId);
        distService.rule(distOfficeProc,groups,distOffices,null);

		List<Comment> comments=actTaskService.getTaskHistoryCommentList(distOfficeProc.getTaskId());
		model.addAttribute("taskId", distOfficeProc.getTaskId());
		model.addAttribute("comments",comments);
		model.addAttribute("review", new BaseReview());
		model.addAttribute("incomeId", incomeId);
		model.addAttribute("income", income);
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
		if( state==1){
			variables.put("msg", "pass");
			variables.put("role","finance");
			if(review.getComment()==null||review.getComment().equals("")){
				review.setComment("通过");
			}
		}else if(state==2){
			variables.put("msg", "reject");
		}
		User user=UserUtils.getUser();
		Authentication.setAuthenticatedUserId( "【经营确认】" +user.getName());// 设置用户id
		actTaskService.complete(taskId,processInstanceId,review.getComment(),variables);
		addMessage(redirectAttributes, "操作成功");
		return "redirect:"+adminPath+ ActConstant.MY_TASK_LIST;

	}

	//财务确认
	@RequestMapping(value = "finAudit")
	public String finAudit(DistOfficeProc distOfficeProc, String[] groups, Model model)  throws Exception{
		List<DistOfficeVo> distOffices=new ArrayList<DistOfficeVo>();
		String incomeId =(String)actTaskService.getTaskVariable(distOfficeProc.getTaskId(),"businessId");
		Income income=incomeService.get(incomeId);
        distOfficeProc.setIncomeId(incomeId);
        distService.rule(distOfficeProc,groups,distOffices,null);
		List<Comment> comments=actTaskService.getTaskHistoryCommentList(distOfficeProc.getTaskId());
		model.addAttribute("taskId", distOfficeProc.getTaskId());
		model.addAttribute("comments",comments);
		model.addAttribute("review", new BaseReview());
		model.addAttribute("incomeId", incomeId);
		model.addAttribute("income", income);
		model.addAttribute("distOffices", distOffices);
		return "modules/income/distFinAudit";
	}

    //财务提交
	@RequestMapping(value = {"finAuditSubmit"},method= RequestMethod.POST)
	public String finAuditSubmit(DistOfficeProc distOfficeProc,String[] groups, BaseReview review,RedirectAttributes redirectAttributes)  throws Exception{

		Task task=actTaskService.getTask(  distOfficeProc.getTaskId());
		String taskId=task.getId();
		String processInstanceId = task.getProcessInstanceId(); // 获取流程实例id
        Map<String, Object> variables=new HashMap<String,Object>();
        variables.put("msg", "pass");
        if(review.getComment()==null||review.getComment().equals("")){
				review.setComment("通过");
        }
        String incomeId =(String)actTaskService.getTaskVariable(distOfficeProc.getTaskId(),"businessId");
        distributeService.saveAcount(incomeId,distOfficeProc.getTypeId());
        User user=UserUtils.getUser();
		Authentication.setAuthenticatedUserId(  user.getName()+ "【"+UserUtils.getUser().getLoginName()+"】");// 设置用户id
		actTaskService.complete(taskId,processInstanceId,review.getComment(),variables);
		addMessage(redirectAttributes, "操作成功");
		return "redirect:"+adminPath+ ActConstant.MY_TASK_LIST;

	}







/*



	private String  rule(DistOffice distOffice, String[] groups,List<DistOfficeVo> distOffices)throws Exception{
		List<DistOffice> distOfficeList= distOfficeService.findList(distOffice);
		String typeId="";
		for(int k=0;k<distOfficeList.size();k++){
			DistOffice item=distOfficeList.get(k);
			typeId=item.getTypeId();
			DistOfficeVo dov=new DistOfficeVo();
			dov.setId(item.getId());
			dov.setOfficeId(item.getOffice().getId());
			dov.setOfficeName(item.getOffice().getName());
			dov.setType(item.getType().getType());
			if(item.getValue()!=null) {
				dov.setValue(item.getValue().toString());
			}
			List<RuleGroup> list=ruleGroupService.findListByOfficeId(item.getOffice().getId());
			List<RuleGroupVo> ruleGroups=new ArrayList<RuleGroupVo>();
			RuleGroupVo rgv=new RuleGroupVo();
			if(groups!=null&&(!groups[k].equals("-1"))){
				dov.setRuleGroupId(groups[k]);
			}else{
				Map<String,String> paramMap=new HashMap<String, String>();
				paramMap.put("typeId",item.getTypeId());
				paramMap.put("officeId",item.getOffice().getId());
			    String groupId=distOfficeService.findGroupId(paramMap);
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
      return typeId;
	}
	private String  rule(DistOffice distOffice, String[] groups,List<DistOfficeVo> distOffices,String type)throws Exception{
		List<DistOffice> distOfficeList= distOfficeService.findList(distOffice);
		String typeId="";
		for(int k=0;k<distOfficeList.size();k++){
			DistOffice item=distOfficeList.get(k);
			typeId=item.getTypeId();
			DistOfficeVo dov=new DistOfficeVo();
			dov.setId(item.getId());
			dov.setOfficeId(item.getOffice().getId());
			dov.setOfficeName(item.getOffice().getName());
			if(item.getValue()!=null) {
				dov.setValue(item.getValue().toString());
			}
			List<RuleGroup> list=ruleGroupService.findListByOfficeId(item.getOffice().getId(),type);
			List<RuleGroupVo> ruleGroups=new ArrayList<RuleGroupVo>();
			RuleGroupVo rgv=new RuleGroupVo();
			if(groups!=null&&(!groups[k].equals("-1"))){
				dov.setRuleGroupId(groups[k]);
			}else{
				Map<String,String> paramMap=new HashMap<String, String>();
				paramMap.put("typeId",item.getTypeId());
				paramMap.put("officeId",item.getOffice().getId());
				String groupId=distOfficeService.findGroupId(paramMap);
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
		return typeId;
	}

	private void ruleSave(DistOffice distOffice, String[] groups)throws Exception{
		Distribute dbDelete =new Distribute();
		dbDelete.setTypeId(distOffice.getTypeId());
		dbDelete.setIncomeId(distOffice.getIncomeId());
		distributeService.delete(dbDelete);
		List<DistOffice> distOfficeList= distOfficeService.findList(distOffice);
		List<Distribute> distributeList = new ArrayList<Distribute>();
		for(int k=0;k<distOfficeList.size();k++){
			DistOffice item=distOfficeList.get(k);

			if(groups[k]!=null) {
				item.setGroupId(groups[k]);
				distOfficeService.save(item);
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
									distribute.setAccount(IncomeConstant.ACCOUNT_TAX);
									distribute.setDes("税");
									distribute.setTypeId(item.getTypeId());
									tax = NumberOperateUtils.mul(itemValue, 0.067);
									distribute.setValue(new BigDecimal(tax));
									distribute.setOfficeId(item.getOffice().getId());
									distribute.setIncomeId(item.getIncomeId());
									distributeList.add(distribute);
								}
								if (ri.getIsFilingFee().equals(0)) {
									distribute = new Distribute();
									distribute.preInsert();
									distribute.setRuleItemId(ri.getId());
									distribute.setStatus(1);
									distribute.setAccount(ri.getAccount()+IncomeConstant.ACCOUNT_FILING_FEE_POSTFIX);
									distribute.setDes("归档费");
									distribute.setTypeId(item.getTypeId());
									filingFee = NumberOperateUtils.sub(itemValue, tax);
									filingFee = NumberOperateUtils.mul(filingFee, 0.02);
									distribute.setValue(new BigDecimal(filingFee));
									distribute.setOfficeId(item.getOffice().getId());
									distribute.setIncomeId(item.getIncomeId());
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
								distribute.setTypeId(item.getTypeId());
								distribute.setValue(new BigDecimal(itemValue));
								distribute.setOfficeId(item.getOffice().getId());
								distribute.setIncomeId(item.getIncomeId());
								distributeList.add(distribute);

							}
						}

					}
				}

			}

		}
		distributeService.addBatch( distributeList);
	}
	*/
}