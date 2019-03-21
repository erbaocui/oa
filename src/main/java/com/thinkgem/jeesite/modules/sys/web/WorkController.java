/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.modules.sys.entity.Study;
import com.thinkgem.jeesite.modules.sys.entity.User;
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
import com.thinkgem.jeesite.modules.sys.entity.Work;
import com.thinkgem.jeesite.modules.sys.service.WorkService;

import java.util.List;

/**
 * 工作履历Controller
 * @author cuijp
 * @version 2019-03-11
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/user/work")
public class WorkController extends BaseController {

	@Autowired
	private WorkService workService;
	
	@ModelAttribute
	public Work get(@RequestParam(required=false) String id) {
		Work entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = workService.get(id);
		}
		if (entity == null){
			entity = new Work();
		}
		return entity;
	}
	
	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = {"list", ""})
	public String list(Work work, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<Work> list=workService.findList(work);
		model.addAttribute("user", work.getUser());
		model.addAttribute("list", list);
		model.addAttribute("work", work);
		model.addAttribute("show", false);
		return "modules/sys/userWorkList";
	}

	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = "form")
	public String form(Work work, Model model) {
		User user = new User();
		user.setId(work.getUser().getId());
		Work findWork=new Work();
		findWork.setUser(user);
		List<Work> list=workService.findList(findWork);
		model.addAttribute("user", work.getUser());
		model.addAttribute("work", work);
		model.addAttribute("show", true);
		model.addAttribute("list", list);
		return "modules/sys/userWorkList";
	}

	@RequiresPermissions("sys:user:edit")
	@RequestMapping(value = "save")
	public String save(Work work, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, work)){
			return form(work, model);
		}
		workService.save(work);
		work=get(work.getId());
		User user = new User();
		user.setId(work.getUser().getId());
		Work findWork=new Work();
		findWork.setUser(user);
		List<Work> list=workService.findList(findWork);
		addMessage(redirectAttributes, "保存工作履历成功");
		model.addAttribute("user", work.getUser());
		model.addAttribute("list", list);
		model.addAttribute("work", work);
		model.addAttribute("show", false);
		return "redirect:"+Global.getAdminPath()+"/sys/user/work/list/?user.id="+work.getUser().getId()+"&repage";

	}
	
	@RequiresPermissions("sys:user:edit")
	@RequestMapping(value = "delete")
	public String delete(Work work,Model model, RedirectAttributes redirectAttributes) {
		workService.delete(work);
		User user = new User();
		user.setId(work.getUser().getId());
		Work findWork=new Work();
		findWork.setUser(user);
		List<Work> list=workService.findList(findWork);
		model.addAttribute("user", work.getUser());
		model.addAttribute("show", false);
		model.addAttribute("list", list);
		addMessage(redirectAttributes, "删除工作履历成功");
		return "redirect:"+Global.getAdminPath()+"/sys/user/work/list?user.id="+work.getUser().getId()+"&repage";

	}

}