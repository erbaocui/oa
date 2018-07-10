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
import com.thinkgem.jeesite.modules.contract.entity.BmTest;
import com.thinkgem.jeesite.modules.contract.service.BmTestService;

/**
 * 测试Controller
 * @author 测试
 * @version 2018-06-28
 */
@Controller
@RequestMapping(value = "${adminPath}/contract/bmTest")
public class BmTestController extends BaseController {

	@Autowired
	private BmTestService bmTestService;
	
	@ModelAttribute
	public BmTest get(@RequestParam(required=false) String id) {
		BmTest entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = bmTestService.get(id);
		}
		if (entity == null){
			entity = new BmTest();
		}
		return entity;
	}
	
	@RequiresPermissions("contract:bmTest:view")
	@RequestMapping(value = {"list", ""})
	public String list(BmTest bmTest, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BmTest> page = bmTestService.findPage(new Page<BmTest>(request, response), bmTest); 
		model.addAttribute("page", page);
		return "modules/contract/bmTestList";
	}

	@RequiresPermissions("contract:bmTest:view")
	@RequestMapping(value = "form")
	public String form(BmTest bmTest, Model model) {
		model.addAttribute("bmTest", bmTest);
		return "modules/contract/bmTestForm";
	}

	@RequiresPermissions("contract:bmTest:edit")
	@RequestMapping(value = "save")
	public String save(BmTest bmTest, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, bmTest)){
			return form(bmTest, model);
		}
		bmTestService.save(bmTest);
		addMessage(redirectAttributes, "保存测试成功");
		return "redirect:"+Global.getAdminPath()+"/contract/bmTest/?repage";
	}
	
	@RequiresPermissions("contract:bmTest:edit")
	@RequestMapping(value = "delete")
	public String delete(BmTest bmTest, RedirectAttributes redirectAttributes) {
		bmTestService.delete(bmTest);
		addMessage(redirectAttributes, "删除测试成功");
		return "redirect:"+Global.getAdminPath()+"/contract/bmTest/?repage";
	}

}