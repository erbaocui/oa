/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.income.web;

import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.income.entity.*;
import com.thinkgem.jeesite.modules.income.service.*;
import com.thinkgem.jeesite.modules.income.vo.DistOfficeProc;
import com.thinkgem.jeesite.modules.income.vo.DistOfficeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 分配Controller
 * @author cuijp
 * @version 2018-05-14
 */
@Controller
@RequestMapping(value = "${adminPath}/income/dist")
public class DistController extends BaseController {

	@Autowired
	private DistService distService;
	@Autowired
	private IncomeService incomeService;


	@RequestMapping(value = "addType")
	@ResponseBody
	public String addType(DistType distType) {

		distService.distTypeAdd(distType);
		return "success";
	}


	@RequestMapping(value = "deleteType")
	@ResponseBody
	public String deleteType(String ids) {

		distService.distTypeDelete(ids);
		return "success";
	}

	

	@RequestMapping(value = "addOffice")
	@ResponseBody
	public String addOffice(DistOffice distOffice, String  officeId) {

		distService.distOfficeAdd(distOffice,officeId);
		return "success";
	}


	@RequestMapping(value = "deleteOffice")
	@ResponseBody
	public String deleteOffice(String ids, String type,String incomeId,String typeId) {

		distService.distOfficeDelete(ids,type,incomeId);
		return "success";
	}

	//财务确认
	@RequestMapping(value = "detail")
	public String detail(String incomeId, Model model)  throws Exception{
		DistOfficeProc distOfficeProc=new DistOfficeProc();
		distOfficeProc.setIncomeId(incomeId);
		String[] groups=null;
		List<DistOfficeVo> distOffices=new ArrayList<DistOfficeVo>();
		Income income=incomeService.get(incomeId);

		distService.rule(distOfficeProc,groups,distOffices,null);

		model.addAttribute("incomeId", incomeId);
		model.addAttribute("income", income);
		model.addAttribute("distOffices", distOffices);
		return "modules/income/dist/distDetail";
	}




}