/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.contract.entity.ContSplitDetailItem;
import com.thinkgem.jeesite.modules.contract.service.ContSplitDetailItemService;

import java.util.HashMap;
import java.util.Map;

/**
 * 合同拆分细化分项Controller
 * @author cuijp
 * @version 2019-05-07
 */
@Controller
@RequestMapping(value = "${adminPath}/cont/split/detail/item")
public class ContSplitDetailItemController extends BaseController {

	@Autowired
	private ContSplitDetailItemService contSplitDetailItemService;
	
	@ModelAttribute
	public ContSplitDetailItem get(@RequestParam(required=false) String id) {
		ContSplitDetailItem entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = contSplitDetailItemService.get(id);
		}
		if (entity == null){
			entity = new ContSplitDetailItem();
		}
		return entity;
	}
	
	@RequiresPermissions("contract:contSplitDetailItem:view")
	@RequestMapping(value = {"list", ""})
	public String list(ContSplitDetailItem contSplitDetailItem, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ContSplitDetailItem> page = contSplitDetailItemService.findPage(new Page<ContSplitDetailItem>(request, response), contSplitDetailItem); 
		model.addAttribute("page", page);
		return "modules/contract/contSplitDetailItemList";
	}

	@RequiresPermissions("contract:contSplitDetailItem:view")
	@RequestMapping(value = "form")
	public String form(ContSplitDetailItem contSplitDetailItem, Model model) {
		model.addAttribute("contSplitDetailItem", contSplitDetailItem);
		return "modules/contract/contSplitDetailItemForm";
	}

	//@RequiresPermissions("contract:contSplitDetailItem:edit")
	@RequestMapping(value = "save")
	@ResponseBody
	public String save(ContSplitDetailItem contSplitDetailItem, Model model, RedirectAttributes redirectAttributes) {

		contSplitDetailItemService.save(contSplitDetailItem);
		return "success";
	}
	
	//@RequiresPermissions("contract:contSplitDetailItem:edit")
	@ResponseBody
	@RequestMapping(value = "delete")
	public Map delete(ContSplitDetailItem contSplitDetailItem, RedirectAttributes redirectAttributes) {
		contSplitDetailItemService.delete(contSplitDetailItem);
		Map<String,Object> result=new HashMap<String, Object>();
		result.put("result","success");
		result.put("msg","成功");
		return  result;
	}

	@RequestMapping(value = "check")
	@ResponseBody
	public Map check(String detailId, Integer type, RedirectAttributes redirectAttributes) {
		ContSplitDetailItem contSplitDetailItem=new ContSplitDetailItem();

		contSplitDetailItem=new ContSplitDetailItem();
		contSplitDetailItem.setDetailId(detailId);
		contSplitDetailItem.setType(type);
		ContSplitDetailItem item=contSplitDetailItemService.getOne(contSplitDetailItem);
		Map<String,Object> result=new HashMap<String, Object>();
		if(item==null){
			result.put("result","success");
			result.put("msg","成功");
		}else{
			result.put("result","error");
			result.put("msg","已存在");
		}
		return  result;
	}


}