/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.entity.Position;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.PositionService;
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
 * 任职历史Controller
 * @author cuijp
 * @version 2019-02-27
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/user/position")
public class PositionController extends BaseController {

	@Autowired
	private PositionService positionService;
	
	@ModelAttribute
	public Position get(@RequestParam(required=false) String id) {
		Position entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = positionService.get(id);
		}
		if (entity == null){
			entity = new Position();
		}
		return entity;
	}
	

	@RequestMapping(value = {"list", ""})
	public String list(Position position, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<Position> list=positionService.findList(position);
		model.addAttribute("user", position.getUser());
		model.addAttribute("list", list);
		model.addAttribute("position", position);
		model.addAttribute("show", false);
		return "modules/sys/userPositionList";
	}

	@RequestMapping(value = "form")
	public String form(Position position,Model model) {
		if(null!=position&&null!=position.getId()){
			position =get(position.getId());
		}
		User user = new User();
		user.setId(position.getUser().getId());
		Position findPosition=new Position();
		findPosition.setUser(user);
		List<Position> list=positionService.findList(findPosition);
		model.addAttribute("user", user);
		model.addAttribute("position", position);
		model.addAttribute("show", true);
		model.addAttribute("list", list);
		return "modules/sys/userPositionList";
	}

	@RequestMapping(value = "save")
	public String save(Position position, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, position)){
			return form(position, model);
		}
		positionService.save(position);
		position=get(position.getId());
		User user = new User();
		user.setId(position.getUser().getId());
		Position findPosition=new Position();
		findPosition.setUser(user);
		List<Position> list=positionService.findList(findPosition);
		addMessage(redirectAttributes, "保存任职历史成功");
		model.addAttribute("user", user);
		model.addAttribute("list", list);
		model.addAttribute("position", position);
		model.addAttribute("show", false);
		return "redirect:"+Global.getAdminPath()+"/sys/user/position/list/?user.id="+position.getUser().getId()+"&repage";

	}

	@RequestMapping(value = "delete")
	public String delete(Position position,Model model, RedirectAttributes redirectAttributes) {
		positionService.delete(position);
		User user = new User();
		user.setId(position.getUser().getId());
		Position findPosition=new Position();
		findPosition.setUser(user);
		List<Position> list=positionService.findList(findPosition);
		model.addAttribute("user", user);
		model.addAttribute("show", false);
		model.addAttribute("list", list);
		addMessage(redirectAttributes, "删除任职历史成功");
		return "redirect:"+Global.getAdminPath()+"/sys/user/position/list?user.id="+position.getUser().getId()+"&repage";
	}

}