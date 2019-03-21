/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.modules.sys.entity.User;
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
import com.thinkgem.jeesite.modules.sys.entity.Study;
import com.thinkgem.jeesite.modules.sys.service.StudyService;

import java.util.Date;
import java.util.List;

/**
 * 学习履历Controller
 * @author cuijp
 * @version 2019-02-27
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/user/study")
public class StudyController extends BaseController {

	@Autowired
	private StudyService studyService;
	
	@ModelAttribute
	public Study get(@RequestParam(required=false) String id) {
		Study entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = studyService.get(id);
		}
		if (entity == null){
			entity = new Study();
		}
		return entity;
	}
	

	@RequestMapping(value = {"list", ""})
	public String list(Study study, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<Study> list=studyService.findList(study);
		model.addAttribute("user", study.getUser());
		model.addAttribute("list", list);
		model.addAttribute("study", study);
		model.addAttribute("show", false);
		return "modules/sys/userStudyList";
	}

	@RequestMapping(value = "form")
	public String form(Study study,Model model) {
		if(null!=study&&null!=study.getId()){
			study =get(study.getId());
		}
		User user = new User();
		user.setId(study.getUser().getId());
		Study findStudy=new Study();
		findStudy.setUser(user);
		List<Study> list=studyService.findList(findStudy);
		model.addAttribute("user", study.getUser());
		model.addAttribute("study", study);
		model.addAttribute("show", true);
		model.addAttribute("list", list);
		return "modules/sys/userStudyList";
	}

	@RequestMapping(value = "save")
	public String save(Study study, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, study)){
			return form(study, model);
		}
		studyService.save(study);
		study=get(study.getId());
		User user = new User();
		user.setId(study.getUser().getId());
		Study findStudy=new Study();
		findStudy.setUser(user);
		List<Study> list=studyService.findList(findStudy);
		addMessage(redirectAttributes, "保存学习履历成功");
		model.addAttribute("user", study.getUser());
		model.addAttribute("list", list);
		model.addAttribute("study", study);
		model.addAttribute("show", false);
		return "redirect:"+Global.getAdminPath()+"/sys/user/study/list/?user.id="+study.getUser().getId()+"&repage";

	}

	@RequestMapping(value = "delete")
	public String delete(Study study,Model model, RedirectAttributes redirectAttributes) {
		studyService.delete(study);
		User user = new User();
		user.setId(study.getUser().getId());
		Study findStudy=new Study();
		findStudy.setUser(user);
		List<Study> list=studyService.findList(findStudy);
		model.addAttribute("user", study.getUser());
		model.addAttribute("show", false);
		model.addAttribute("list", list);
		addMessage(redirectAttributes, "删除学习履历成功");
		return "redirect:"+Global.getAdminPath()+"/sys/user/study/list?user.id="+study.getUser().getId()+"&repage";
	}

}