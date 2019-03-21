/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.entity.UserContract;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.UserContractService;
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
 * 员工合同Controller
 * @author cuijp
 * @version 2019-02-27
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/user/contract")
public class UserContractController extends BaseController {

	@Autowired
	private UserContractService userContractService;
	
	@ModelAttribute
	public UserContract get(@RequestParam(required=false) String id) {
		UserContract entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = userContractService.get(id);
		}
		if (entity == null){
			entity = new UserContract();
		}
		return entity;
	}
	

	@RequestMapping(value = {"list", ""})
	public String list(UserContract userContract, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<UserContract> list=userContractService.findList(userContract);
		model.addAttribute("user", userContract.getUser());
		model.addAttribute("list", list);
		model.addAttribute("contract", userContract);
		model.addAttribute("show", false);
		return "modules/sys/userContractList";
	}

	@RequestMapping(value = "form")
	public String form(UserContract userContract, Model model) {
		if(null!=userContract&&null!=userContract.getId()){
			userContract =get(userContract.getId());
		}
		User user = new User();
		user.setId(userContract.getUser().getId());
		UserContract findContract=new UserContract();
		findContract.setUser(user);
		List<UserContract> list=userContractService.findList(findContract);

		model.addAttribute("user",user);
		model.addAttribute("contract", userContract);
		model.addAttribute("show", true);
		model.addAttribute("list", list);
		return "modules/sys/userContractList";
	}

	@RequestMapping(value = "save")
	public String save(UserContract userContract, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, userContract)){
			return form(userContract, model);
		}
		userContractService.save(userContract);
		userContract=get(userContract.getId());
		User user = new User();
		user.setId(userContract.getUser().getId());
		UserContract findContract=new UserContract();
		findContract.setUser(user);
		List<UserContract> list=userContractService.findList(findContract);
		addMessage(redirectAttributes, "保存员工合同成功");
		model.addAttribute("user",user);
		model.addAttribute("list", list);
		model.addAttribute("contract", userContract);
		model.addAttribute("show", false);
		return "redirect:"+Global.getAdminPath()+"/sys/user/contract/list/?user.id="+userContract.getUser().getId()+"&repage";

	}

	@RequestMapping(value = "delete")
	public String delete(UserContract userContract, Model model, RedirectAttributes redirectAttributes) {
		userContractService.delete(userContract);
		User user = new User();
		user.setId(userContract.getUser().getId());
		UserContract findContract=new UserContract();
		findContract.setUser(user);
		List<UserContract> list=userContractService.findList(findContract);
		model.addAttribute("user",user);
		model.addAttribute("show", false);
		model.addAttribute("list", list);
		addMessage(redirectAttributes, "删除员工合同成功");
		return "redirect:"+Global.getAdminPath()+"/sys/user/contract/list?user.id="+userContract.getUser().getId()+"&repage";
	}

}