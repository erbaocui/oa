/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.income.web;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.NumberOperateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.income.entity.*;
import com.thinkgem.jeesite.modules.income.service.*;
import com.thinkgem.jeesite.modules.income.vo.RuleItemVo;
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

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
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


}