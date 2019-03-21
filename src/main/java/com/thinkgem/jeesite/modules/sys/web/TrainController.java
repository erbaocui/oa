/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.common.utils.Collections3;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.sys.entity.Study;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
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
import com.thinkgem.jeesite.modules.sys.entity.Train;
import com.thinkgem.jeesite.modules.sys.service.TrainService;

import java.util.ArrayList;
import java.util.List;

/**
 * 培训Controller
 * @author cuijp
 * @version 2019-03-13
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/train")
public class TrainController extends BaseController {
	@Autowired
	private OfficeService officeService;
	@Autowired
	private SystemService systemService;


	@Autowired
	private TrainService trainService;
	
	@ModelAttribute
	public Train get(@RequestParam(required=false) String id) {
		Train entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = trainService.get(id);
		}
		if (entity == null){
			entity = new Train();
		}
		return entity;
	}
	
	@RequiresPermissions("sys:train:view")
	@RequestMapping(value = {"list", ""})
	public String list(Train train, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Train> page = trainService.findPage(new Page<Train>(request, response), train); 
		model.addAttribute("page", page);
		return "modules/sys/trainList";
	}

	@RequiresPermissions("sys:train:view")
	@RequestMapping(value = "form")
	public String form(Train train, Model model) {
		model.addAttribute("train", train);
		return "modules/sys/trainForm";
	}

	@RequiresPermissions("sys:train:edit")
	@RequestMapping(value = "save")
	public String save(Train train, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, train)){
			return form(train, model);
		}
		trainService.save(train);
		addMessage(redirectAttributes, "保存培训成功");
		return "redirect:"+Global.getAdminPath()+"/sys/train/?repage";
	}
	
	@RequiresPermissions("sys:train:edit")
	@RequestMapping(value = "delete")
	public String delete(Train train, RedirectAttributes redirectAttributes) {
		trainService.delete(train);
		addMessage(redirectAttributes, "删除培训成功");
		return "redirect:"+Global.getAdminPath()+"/sys/train/?repage";
	}
	/**
	 * 培训人员选择 -- 打开人员分配页面
	 * @param train
	 * @param model
	 * @return
	 */

	@RequestMapping(value = "assign")
	public String assign(Train train, Model model) {
		List<User> userList = trainService.findUserByTrainId(train);
		model.addAttribute("userList", userList);
		return "modules/sys/trainAssign";
	}

	/**
	 * 培训人员选择 -- 打开人员分配对话框
	 * @param train
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:train:view")
	@RequestMapping(value = "usertotrain")
	public String selectUserToRole(Train train, Model model) {
		List<User> userList = trainService.findUserByTrainId(train);
		model.addAttribute("trian", train);
		model.addAttribute("userList", userList);
		model.addAttribute("selectIds", Collections3.extractToString(userList, "name", ","));
		model.addAttribute("officeList", officeService.findAll());
		return "modules/sys/selectUserToTrain";
	}

	/**
	 * 培训人员选择
	 * @param train
	 * @param idsArr
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:train:edit")
	@RequestMapping(value = "assignTrain")
	public String assignTrain(Train train, String[] idsArr, RedirectAttributes redirectAttributes) {

		StringBuilder msg = new StringBuilder();
		int newNum = 0;
		List<User> userList=new ArrayList<User>();
		for (int i = 0; i < idsArr.length; i++) {
			User user =systemService.getUser(idsArr[i]);


			//List<User> existUser =trainService.findUserByTrainId(train);
			if (null !=user && !user.equals("")) {
				msg.append("<br/>新增用户【" + user.getName() + "】到培训【" + train.getName() + "】！");
				newNum++;
				userList.add(user);
			}
		}
		train.setUserList(userList);
		trainService.saveTrainUser(train);
		addMessage(redirectAttributes, "已成功分配 "+newNum+" 个用户"+msg);
		return "redirect:" + adminPath + "/sys/train/assign?id="+train.getId();
	}
	/**
	 * 培训员工员工 -- 从培训中移除用户
	 * @param userId
	 * @param trainId
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:train:edit")
	@RequestMapping(value = "outTrain")
	public String outTrain(String userId, String trainId, RedirectAttributes redirectAttributes) {
		Train train=get(trainId);
		User user = UserUtils.get(userId);
		trainService.outUserInTrain(train,user);
		addMessage(redirectAttributes, "用户【" + user.getName() + "】从培训【" + train.getName() + "】中移除成功！");
		return "redirect:" + adminPath + "/sys/train/assign?id="+ train.getId();
	}

	@RequestMapping(value = {"userTrainList", ""})
	public String userTrainList(User user, HttpServletRequest request, HttpServletResponse response, Model model) {
		Train train =new Train();
		List<User> userList=new ArrayList<User>();
		userList.add(user);
		train.setUserList(userList);
		List<Train> list=trainService.findList(train);
		model.addAttribute("user", user);
		model.addAttribute("list", list);
		return "modules/sys/userTrainList";
	}




}