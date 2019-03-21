/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.entity.Reward;
import com.thinkgem.jeesite.modules.sys.service.RewardService;
import java.util.List;

/**
 * 奖惩Controller
 * @author cuijp
 * @version 2019-03-11
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/user/reward")
public class RewardController extends BaseController {

	@Autowired
	private RewardService rewardService;
	
	@ModelAttribute
	public Reward get(@RequestParam(required=false) String id) {
		Reward entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = rewardService.get(id);
		}
		if (entity == null){
			entity = new Reward();
		}
		return entity;
	}
	
	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = {"list", ""})
	public String list(Reward reward, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<Reward> list=rewardService.findList(reward);
		model.addAttribute("user", reward.getUser());
		model.addAttribute("list", list);
		model.addAttribute("reward", reward);
		model.addAttribute("show", false);
		return "modules/sys/userRewardList";
	}

	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = "form")
	public String form(Reward reward, Model model) {
		User user = new User();
		user.setId(reward.getUser().getId());
		Reward findReward=new Reward();
		findReward.setUser(user);
		List<Reward> list=rewardService.findList(findReward);
		model.addAttribute("user", user);
		model.addAttribute("reward", reward);
		model.addAttribute("show", true);
		model.addAttribute("list", list);
		return "modules/sys/userRewardList";
	}

	@RequiresPermissions("sys:user:edit")
	@RequestMapping(value = "save")
	public String save(Reward reward, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, reward)){
			return form(reward, model);
		}

		rewardService.save(reward);
		reward=get(reward.getId());
		User user = new User();
		user.setId(reward.getUser().getId());
		Reward findReward=new Reward();
		findReward.setUser(user);
		List<Reward> list=rewardService.findList(findReward);
		addMessage(redirectAttributes, "保存奖惩成功");
		model.addAttribute("user", user);
		model.addAttribute("list", list);
		model.addAttribute("reward", reward);
		model.addAttribute("show", false);
		return "redirect:"+Global.getAdminPath()+"/sys/user/reward/list/?user.id="+reward.getUser().getId()+"&repage";

	}
	
	@RequiresPermissions("sys:user:edit")
	@RequestMapping(value = "delete")
	public String delete(Reward reward,Model model, RedirectAttributes redirectAttributes) {
		rewardService.delete(reward);
		User user = new User();
		user.setId(reward.getUser().getId());
		Reward findReward=new Reward();
		findReward.setUser(user);
		List<Reward> list=rewardService.findList(findReward);
		model.addAttribute("user", user);
		model.addAttribute("show", false);
		model.addAttribute("list", list);
		addMessage(redirectAttributes, "删除奖惩成功");
		return "redirect:"+Global.getAdminPath()+"/sys/user/reward/list?user.id="+reward.getUser().getId()+"&repage";

	}

}