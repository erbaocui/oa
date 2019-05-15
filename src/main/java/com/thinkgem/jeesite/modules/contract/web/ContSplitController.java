/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.modules.contract.constant.ContConstant;
import com.thinkgem.jeesite.modules.contract.entity.*;
import com.thinkgem.jeesite.modules.contract.service.ContService;
import com.thinkgem.jeesite.modules.contract.service.ContSplitDetailService;
import com.thinkgem.jeesite.modules.contract.service.ContSplitItemService;
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
import com.thinkgem.jeesite.modules.contract.service.ContSplitService;

import java.util.List;

/**
 * 合同拆分Controller
 * @author cuijp
 * @version 2019-05-05
 */
@Controller
@RequestMapping(value = "${adminPath}/cont/split")
public class ContSplitController extends BaseController {

	@Autowired
	private ContSplitService contSplitService;
	@Autowired
	private ContService contService;
	@Autowired
	private ContSplitItemService contSplitItemService;
	@Autowired
	private ContSplitDetailService contSplitDetailService;
	@ModelAttribute
	public ContSplit get(@RequestParam(required=false) String id) {
		ContSplit entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = contSplitService.get(id);
		}
		if (entity == null){
			entity = new ContSplit();
		}
		return entity;
	}
	
	//@RequiresPermissions("contract:contSplit:view")
	@RequestMapping(value = {"list", ""})
	public String list(String  contractId,Boolean readonly, Model model) {
		Contract contract=contService.get(contractId);
		ContSplit 	contSplit=new  ContSplit();
		contSplit.setContractId(contractId);
		contSplit=contSplitService.getOne(contSplit);
		if(contSplit==null){
			contSplit=new  ContSplit();
			contSplit.setContractId(contractId);
			contSplit.setStatus(ContConstant.SplitStatus.UNSTART.getValue());
			contSplit.setDraw(0);
			contSplit.setPlan(0);
			contSplitService.save(contSplit);
			contSplit=new  ContSplit();
			contSplit.setContractId(contractId);
			contSplit=contSplitService.getOne(contSplit);
		}
		ContSplitItem contSplitItem=new ContSplitItem();
		contSplitItem.setSplitId(contSplit.getId());
		List<ContSplitItem> list = contSplitItemService.findList(contSplitItem);
		ContSplitDetail contSplitDetail=new ContSplitDetail();
		contSplitDetail.setContractId(contractId);
		List<ContSplitDetail> contSplitDetails=contSplitDetailService.findList(contSplitDetail);
		model.addAttribute("contSplitItems", list);
		model.addAttribute("contSplitItem", contSplitItem);
		model.addAttribute("contractId",contractId);
		model.addAttribute("contract",contract);
		model.addAttribute("contSplit",contSplit);
		model.addAttribute("readonly",readonly);
		model.addAttribute("contSplitDetails",contSplitDetails);
		return "modules/contract/contractSplit";
	}

//	@RequestMapping(value = "/item/save", method= RequestMethod.GET)
//	@ResponseBody
//	public String itemSave(ContSplitItem contSplitItem, Model model, RedirectAttributes redirectAttributes) {
////		if (!beanValidator(model, contSplitItem)){
////			return form(contSplitItem, model);
////		}
//		contSplitItemService.save(contSplitItem);
//		return "success";
//	}


//	@RequestMapping(value = "/item/delete", method= RequestMethod.GET)
//	public String itemDelete (String id,String  contractId,Boolean readonly, RedirectAttributes redirectAttributes) {
//		ContSplitItem contSplitItem=new ContSplitItem();
//		contSplitItem.setId(id);
//		contSplitItemService.delete(contSplitItem);
//		addMessage(redirectAttributes, "删除拆分分项成功");
//		return "redirect:"+Global.getAdminPath()+"/cont/split/list?contractId="+contractId+"&readonly="+readonly+"&repage";
//	}




}