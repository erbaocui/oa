/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.entity.Home;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.HomeService;
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
 * 家庭成员Controller
 * @author cuijp
 * @version 2019-02-27
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/user/home")
public class HomeController extends BaseController {

	@Autowired
	private HomeService homeService;
	
	@ModelAttribute
	public Home get(@RequestParam(required=false) String id) {
		Home entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = homeService.get(id);
		}
		if (entity == null){
			entity = new Home();
		}
		return entity;
	}
	

	@RequestMapping(value = {"list", ""})
	public String list(Home home, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<Home> list=homeService.findList(home);
		model.addAttribute("user", home.getUser());
		model.addAttribute("list", list);
		model.addAttribute("home", home);
		model.addAttribute("show", false);
		return "modules/sys/userHomeList";
	}

	@RequestMapping(value = "form")
	public String form(Home home,Model model) {
		if(null!=home&&null!=home.getId()){
			home =get(home.getId());
		}
		User user = new User();
		user.setId(home.getUser().getId());
		Home findHome=new Home();
		findHome.setUser(user);
		List<Home> list=homeService.findList(findHome);
		model.addAttribute("user", user);
		model.addAttribute("home", home);
		model.addAttribute("show", true);
		model.addAttribute("list", list);
		return "modules/sys/userHomeList";
	}

	@RequestMapping(value = "save")
	public String save(Home home, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, home)){
			return form(home, model);
		}
		homeService.save(home);
		home=get(home.getId());
		User user = new User();
		user.setId(home.getUser().getId());
		Home findHome=new Home();
		findHome.setUser(user);
		List<Home> list=homeService.findList(findHome);
		addMessage(redirectAttributes, "保存家庭成员成功");
		model.addAttribute("user", user);
		model.addAttribute("list", list);
		model.addAttribute("home", home);
		model.addAttribute("show", false);
		return "redirect:"+Global.getAdminPath()+"/sys/user/home/list/?user.id="+home.getUser().getId()+"&repage";

	}

	@RequestMapping(value = "delete")
	public String delete(Home home,Model model, RedirectAttributes redirectAttributes) {
		homeService.delete(home);
		User user = new User();
		user.setId(home.getUser().getId());
		Home findHome=new Home();
		findHome.setUser(user);
		List<Home> list=homeService.findList(findHome);
		model.addAttribute("user", user);
		model.addAttribute("show", false);
		model.addAttribute("list", list);
		addMessage(redirectAttributes, "删除家庭成员成功");
		return "redirect:"+Global.getAdminPath()+"/sys/user/home/list?user.id="+home.getUser().getId()+"&repage";
	}

}