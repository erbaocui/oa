/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.modules.contract.entity.ContSplitDetail;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.contract.entity.ContSplitItem;
import com.thinkgem.jeesite.modules.contract.service.ContSplitItemService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 拆分分项Controller
 * @author cuijp
 * @version 2019-05-05
 */
@Controller
@RequestMapping(value = "${adminPath}/cont/split/item")
public class ContSplitItemController extends BaseController {

	@Autowired
	private ContSplitItemService contSplitItemService;
	
	@ModelAttribute
	public ContSplitItem get(@RequestParam(required=false) String id) {
		ContSplitItem entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = contSplitItemService.get(id);
		}
		if (entity == null){
			entity = new ContSplitItem();
		}
		return entity;
	}
	
	//@RequiresPermissions("contract:contSplitItem:view")
	@RequestMapping(value = {"list", ""})
	public String list(ContSplitItem contSplitItem, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ContSplitItem> page = contSplitItemService.findPage(new Page<ContSplitItem>(request, response), contSplitItem); 
		model.addAttribute("page", page);
		return "modules/contract/contSplitItemList";
	}

	//@RequiresPermissions("contract:contSplitItem:view")
	@RequestMapping(value = "form")
	public String form(ContSplitItem contSplitItem, Model model) {
		model.addAttribute("contSplitItem", contSplitItem);
		return "modules/contract/contSplitItemForm";
	}

	//@RequiresPermissions("contract:contSplitItem:edit")
	@RequestMapping(value = "save")
	@ResponseBody
	public String save(ContSplitItem contSplitItem, Model model, RedirectAttributes redirectAttributes) {

		contSplitItemService.save(contSplitItem);
		return "success";
	}

	@RequestMapping(value = "delete")
	@ResponseBody
	 public String delete (String id) {
		ContSplitItem contSplitItem=new ContSplitItem();
		contSplitItem.setId(id);
		contSplitItemService.delete(contSplitItem);
		return "success";
	}



}