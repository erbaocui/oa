/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.entity.Performance;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.PerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 员工绩效Controller
 * @author cuijp
 * @version 2019-02-27
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/user/performance")
public class PerformanceController extends BaseController {

	@Autowired
	private PerformanceService performanceService;
	
	@ModelAttribute
	public Performance get(@RequestParam(required=false) String id) {
		Performance entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = performanceService.get(id);
		}
		if (entity == null){
			entity = new Performance();
		}
		return entity;
	}
	

	@RequestMapping(value = {"list", ""})
	public String list(Performance performance, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<Performance> list=performanceService.findList(performance);
		model.addAttribute("user", performance.getUser());
		model.addAttribute("list", list);
		model.addAttribute("performance", performance);
		model.addAttribute("show", false);
		return "modules/sys/userPerformanceList";
	}

	@RequestMapping(value = "form")
	public String form(Performance performance,Model model) {
		if(null!=performance&&null!=performance.getId()){
			performance =get(performance.getId());
		}
		User user = new User();
		user.setId(performance.getUser().getId());
		Performance findPerformance=new Performance();
		findPerformance.setUser(user);
		List<Performance> list=performanceService.findList(findPerformance);
		model.addAttribute("user", user);
		model.addAttribute("performance", performance);
		model.addAttribute("show", true);
		model.addAttribute("list", list);
		return "modules/sys/userPerformanceList";
	}

	@RequestMapping(value = "save")
	public String save(Performance performance, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, performance)){
			return form(performance, model);
		}
		performanceService.save(performance);
		performance=get(performance.getId());
		User user = new User();
		user.setId(performance.getUser().getId());
		Performance findPerformance=new Performance();
		findPerformance.setUser(user);
		List<Performance> list=performanceService.findList(findPerformance);
		addMessage(redirectAttributes, "保存员工绩效成功");
		model.addAttribute("user", user);
		model.addAttribute("list", list);
		model.addAttribute("performance", performance);
		model.addAttribute("show", false);
		return "redirect:"+Global.getAdminPath()+"/sys/user/performance/list/?user.id="+performance.getUser().getId()+"&repage";

	}

	@RequestMapping(value = "delete")
	public String delete(Performance performance,Model model, RedirectAttributes redirectAttributes) {
		performanceService.delete(performance);
		User user = new User();
		user.setId(performance.getUser().getId());
		Performance findPerformance=new Performance();
		findPerformance.setUser(user);
		List<Performance> list=performanceService.findList(findPerformance);
		model.addAttribute("user", user);
		model.addAttribute("show", false);
		model.addAttribute("list", list);
		addMessage(redirectAttributes, "删除员工绩效成功");
		return "redirect:"+Global.getAdminPath()+"/sys/user/performance/list?user.id="+performance.getUser().getId()+"&repage";
	}

}