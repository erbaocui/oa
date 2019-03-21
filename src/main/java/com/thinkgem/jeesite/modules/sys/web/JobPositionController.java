/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.entity.JobPosition;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.JobPositionService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 设计岗位Controller
 * @author cuijp
 * @version 2019-02-27
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/user/jobPosition")
public class JobPositionController extends BaseController {

	@Autowired
	private JobPositionService jobPositionService;
	
	@ModelAttribute
	public JobPosition get(@RequestParam(required=false) String id) {
		JobPosition entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = jobPositionService.get(id);
		}
		if (entity == null){
			entity = new JobPosition();
		}
		return entity;
	}
	

	@RequestMapping(value = {"list", ""})
	public String list(JobPosition jobPosition, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<JobPosition> list=jobPositionService.findList(jobPosition);
		model.addAttribute("user", jobPosition.getUser());
		model.addAttribute("list", list);
		model.addAttribute("jobPosition", jobPosition);
		model.addAttribute("show", false);
		return "modules/sys/userJobPositionList";
	}

	@RequestMapping(value = "form")
	public String form(JobPosition jobPosition,Model model) {
		if(null!=jobPosition&&null!=jobPosition.getId()){
			jobPosition =get(jobPosition.getId());
		}
		User user = new User();
		user.setId(jobPosition.getUser().getId());
		JobPosition findJobPosition=new JobPosition();
		findJobPosition.setUser(user);
		List<JobPosition> list=jobPositionService.findList(findJobPosition);
		model.addAttribute("user",user);
		model.addAttribute("jobPosition", jobPosition);
		model.addAttribute("show", true);
		model.addAttribute("list", list);
		return "modules/sys/userJobPositionList";
	}

	@RequestMapping(value = "save")
	public String save(JobPosition jobPosition, String positions,Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, jobPosition)){
			return form(jobPosition, model);
		}

		for(String position:positions.split(",")){
			if(position.equals("responsibl")){
				jobPosition.setResponsible("1");
			}
			if(position.equals("approval")){
				jobPosition.setApproval("1");
			}
			if(position.equals("audit")){
				jobPosition.setAudit("1");
			}
			if(position.equals("proof")){
				jobPosition.setProof("1");
			}
			if(position.equals("design")){
				jobPosition.setDesign("1");
			}
			if(position.equals("draw")){
				jobPosition.setDraw("1");
			}

		}
		jobPositionService.save(jobPosition);
		jobPosition=get(jobPosition.getId());
		User user = new User();
		user.setId(jobPosition.getUser().getId());
		JobPosition findJobPosition=new JobPosition();
		findJobPosition.setUser(user);
		List<JobPosition> list=jobPositionService.findList(findJobPosition);
		addMessage(redirectAttributes, "保存设计岗位成功");
		model.addAttribute("user",user);
		model.addAttribute("list", list);
		model.addAttribute("jobPosition", jobPosition);
		model.addAttribute("show", false);
		return "redirect:"+Global.getAdminPath()+"/sys/user/jobPosition/list/?user.id="+jobPosition.getUser().getId()+"&repage";

	}

	@RequestMapping(value = "delete")
	public String delete(JobPosition jobPosition,Model model, RedirectAttributes redirectAttributes) {
		jobPositionService.delete(jobPosition);
		User user = new User();
		user.setId(jobPosition.getUser().getId());
		JobPosition findJobPosition=new JobPosition();
		findJobPosition.setUser(user);
		List<JobPosition> list=jobPositionService.findList(findJobPosition);
		model.addAttribute("user",user);
		model.addAttribute("show", false);
		model.addAttribute("list", list);
		addMessage(redirectAttributes, "删除设计岗位成功");
		return "redirect:"+Global.getAdminPath()+"/sys/user/jobPosition/list?user.id="+jobPosition.getUser().getId()+"&repage";
	}

	/**
	 * 验证专业设计岗位已存在
	 * @param jobPosition
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("sys:user:edit")
	@RequestMapping(value = "checkProfessional")
	public String checkProfessional(JobPosition jobPosition) {
		   jobPosition.setProfessional(jobPosition.getProfessional().split(",")[1]);
	       List<JobPosition> list=jobPositionService.findList(jobPosition);
		if (list ==null||list.size()==0) {
			return "true";
		}
		return "false";
	}


}