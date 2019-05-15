/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.modules.contract.constant.ContConstant;
import com.thinkgem.jeesite.modules.contract.entity.ContSplitDetailItem;
import com.thinkgem.jeesite.modules.contract.entity.Contract;
import com.thinkgem.jeesite.modules.contract.service.ContService;
import com.thinkgem.jeesite.modules.contract.service.ContSplitDetailItemService;
import com.thinkgem.jeesite.modules.contract.service.ContSplitDetailOfficeService;
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
import com.thinkgem.jeesite.modules.contract.entity.ContSplitDetail;
import com.thinkgem.jeesite.modules.contract.service.ContSplitDetailService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 合同拆分细化Controller
 * @author cuijp
 * @version 2019-05-07
 */
@Controller
@RequestMapping(value = "${adminPath}/cont/split/detail")
public class ContSplitDetailController extends BaseController {
	@Autowired
	private ContService contService;
	@Autowired
	private ContSplitDetailService contSplitDetailService;
	@Autowired
	private ContSplitDetailItemService contSplitDetailItemService;
	@Autowired
	private ContSplitDetailOfficeService contSplitDetailOfficeService;
	
	@ModelAttribute
	public ContSplitDetail get(@RequestParam(required=false) String id) {
		ContSplitDetail entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = contSplitDetailService.get(id);
		}
		if (entity == null){
			entity = new ContSplitDetail();
		}
		return entity;
	}
	
	//@RequiresPermissions("contract:contSplitDetail:view")
	@RequestMapping(value = {"list", ""})
	public String list(ContSplitDetail contSplitDetail,String  contractId,Boolean readonly,HttpServletRequest request, HttpServletResponse response, Model model) {
		Contract contract=contService.get(contractId);
		contSplitDetail =get(contSplitDetail.getId());
		ContSplitDetailItem contSplitDetailItem=new ContSplitDetailItem();
		contSplitDetailItem.setDetailId(contSplitDetail.getId());
		List<ContSplitDetailItem> contSplitDetailItems=contSplitDetailItemService.findList(contSplitDetailItem);

		model.addAttribute("contractId", contractId);
		model.addAttribute("readonly", readonly);
		model.addAttribute("contract", contract);
		model.addAttribute("contSplitDetail", contSplitDetail);
		model.addAttribute("contSplitDetailItem",new ContSplitDetailItem());
		model.addAttribute("contSplitDetailItems",contSplitDetailItems);
		return "modules/contract/contractSplitDetail";
	}

	//@RequiresPermissions("contract:contSplitDetail:view")
	@RequestMapping(value = "form")
	public String form(ContSplitDetail contSplitDetail, Model model) {
		model.addAttribute("contSplitDetail", contSplitDetail);
		return "modules/contract/contractSplitDetail";
	}

	//@RequiresPermissions("contract:contSplitDetail:edit")
	@RequestMapping(value = "save")
	@ResponseBody
	public String save(ContSplitDetail contSplitDetail, Model model, RedirectAttributes redirectAttributes) {
//		if (!beanValidator(model, contSplitDetail)){
//			return form(contSplitDetail, model);
//		}
		contSplitDetail.setStatus(ContConstant.SplitDetailStatus.UNSTART.getValue());
		contSplitDetailService.save(contSplitDetail);
		return "success";
	}
	
	//@RequiresPermissions("contract:contSplitDetail:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(ContSplitDetail contSplitDetail, RedirectAttributes redirectAttributes) {
		contSplitDetailService.delete(contSplitDetail);
		return "success";
	}

	@RequestMapping(value = "check/process")
	@ResponseBody
	public Map checkProcess(ContSplitDetail contSplitDetail, RedirectAttributes redirectAttributes) {
		contSplitDetail=contSplitDetailService.get(contSplitDetail.getId());
		BigDecimal detailSum=contSplitDetailOfficeService.detailSum(contSplitDetail.getId());
		Map<String,Object> result=new HashMap<String, Object>();
		if(detailSum.doubleValue()==contSplitDetail.getTotal().doubleValue()){
			result.put("result","success");
			result.put("msg","成功");
		}else{
			result.put("result","error");
			result.put("msg","请先完成拆解细化");
		}
		return result;
	}

}