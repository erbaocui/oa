/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.web;

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
import com.thinkgem.jeesite.modules.sys.entity.Detail;
import com.thinkgem.jeesite.modules.sys.service.DetailService;

/**
 * 员工详细信息Controller
 * @author cuijp
 * @version 2019-03-18
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/user/detail")
public class DetailController extends BaseController {

	@Autowired
	private DetailService detailService;
	
	@ModelAttribute
	public Detail get(@RequestParam(required=false) String id) {
		Detail entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = detailService.get(id);
		}
		if (entity == null){
			entity = new Detail();
		}
		return entity;
	}
	@ModelAttribute
	public Detail getByUserId(@RequestParam(required=false) String userId) {
		Detail entity = null;
		if (StringUtils.isNotBlank(userId)){
			entity = detailService.getByUserId(userId);
		}
		if (entity == null){
			entity = new Detail();
		}
		return entity;
	}
	

	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = "form")
	public String form(Detail detail, Model model) {
		if(detail.getUser()!=null&&detail.getUser().getId()!=null){
			detail=getByUserId(detail.getUser().getId());
		}


		model.addAttribute("user", detail.getUser());
		model.addAttribute("detail", detail);
		return "modules/sys/userDetailForm";
	}

	@RequiresPermissions("sys:user:edit")
	@RequestMapping(value = "save")
	public String save(Detail detail,String flag, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, detail)){
			return form(detail, model);
		}
		detailService.save(detail);
		model.addAttribute("user", detail.getUser());
		if(flag.equals("position")) {
			addMessage(redirectAttributes, "保存员工任职信息成功");
			return "redirect:" + Global.getAdminPath() + "/sys/user/detail/currentPosition?user.id=" + detail.getUser().getId() + "&repage";
		}else{
			addMessage(redirectAttributes, "保存员工详细信息成功");
			return "redirect:" + Global.getAdminPath() + "/sys/user/detail/form?user.id=" + detail.getUser().getId() + "&repage";
		}
	}
	
	@RequiresPermissions("sys:user:edit")
	@RequestMapping(value = "delete")
	public String delete(Detail detail, RedirectAttributes redirectAttributes) {
		detailService.delete(detail);
		addMessage(redirectAttributes, "删除员工详细信息成功");
		return "redirect:"+Global.getAdminPath()+"/sys/detail/?repage";
	}

	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = "currentPosition")
	public String currentPosition(Detail detail, Model model) {
		if(detail.getUser()!=null&&detail.getUser().getId()!=null){
			detail=getByUserId(detail.getUser().getId());
		}


		model.addAttribute("user", detail.getUser());
		model.addAttribute("detail", detail);
		return "modules/sys/userCurrentPostionForm";
	}

}