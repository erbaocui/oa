/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.act.service.ActTaskService;
import com.thinkgem.jeesite.modules.contract.entity.ContAttach;
import com.thinkgem.jeesite.modules.contract.entity.ContItem;
import com.thinkgem.jeesite.modules.contract.entity.Contract;
import com.thinkgem.jeesite.modules.contract.service.ContAttachService;
import com.thinkgem.jeesite.modules.contract.service.ContItemService;
import com.thinkgem.jeesite.modules.contract.vo.QueryContract;
import com.thinkgem.jeesite.modules.contract.service.ContService;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.Dict;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.service.AreaService;
import com.thinkgem.jeesite.modules.sys.service.DictService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.activiti.engine.task.Comment;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 合同管理Controller
 * @author cuijp
 * @version 2018-04-19
 */
@Controller
@RequestMapping(value = "${adminPath}/cont/base")
public class ContController extends BaseController {

	@Autowired
	private ContService contService;
	@Autowired
	private ContItemService contItemService;
	@Autowired
	private ContAttachService contAttachService;


	@Autowired
	private ActTaskService actTaskService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private DictService dictService;




	@ModelAttribute
	public Contract get(@RequestParam(required=false) String id) {
		Contract entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = contService.get(id);
		}
		if (entity == null){
			entity = new Contract();
		}
		return entity;
	}

	/**
	 * 验证名是否有效
	 * @param oldCode
	 * @param code
	 * @return
	 */
	@ResponseBody

	@RequestMapping(value = "checkCode")
	public String checkCode(String oldCode, String code) {
		if ( code !=null &&  code.equals(oldCode)) {
			return "true";
		} else if (code!=null) {
			Contract contract=new Contract();
			contract.setCode(code);
			Contract cont=contService.get(contract);
			if(cont==null){
				return "true";
			}

		}
		return "false";
	}


	@RequiresPermissions("cont:base:view")
	@RequestMapping(value = {"list", ""})
	public String list(QueryContract queryContract, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Contract> page = contService.findPage(new Page<Contract>(request, response), queryContract);
		model.addAttribute("page", page);
		Area area=new Area();
		Area parent=new Area();
		parent.setId("1");
		area.setParent(parent);

		List<Area> provinceList =areaService.findList(area);
		model.addAttribute("provinceList",  provinceList );

		model.addAttribute("contract", queryContract);
		return "modules/contract/contractList";
	}

	/*@RequiresPermissions("cont:base:view")
	@RequestMapping(value = {"briefList", ""})
	public String briefList(QueryContract queryContract,String notContractIds, HttpServletRequest request, HttpServletResponse response, Model model) {
		if(!StringUtils.isEmpty(notContractIds.trim())){
			queryContract.setNoInArray(notContractIds.trim().split(","));
		}
		Page<UserContract> page=new Page<UserContract>(request, response);
		page.setPageSize(10);
		page = contService.findPage(page, queryContract);
		model.addAttribute("page", page);
		model.addAttribute("queryContract", queryContract);
		return "modules/contract/contractBriefList";
	}*/

	@RequiresPermissions("cont:base:view")
	@RequestMapping(value = "form")
	public String form(Contract contract,Boolean readonly, Model model) throws Exception{
//		List<Dict> list=new ArrayList<Dict>();
//		Dict dict =new Dict();
//		//dict.setLabel();
//		dict.setValue("1");
//		list.add(dict);
//		contract.setTypeList(list);
//		List<String> abc=new ArrayList<String>();
//		abc.add("1");
//		abc.add("2");
//		contract.setTestType(abc);
		model.addAttribute("contract", contract);
		model.addAttribute("readonly",  readonly);
		Area area=new Area();
		Area parent=new Area();
		parent.setId("1");
		area.setParent(parent);
		List<Area> provinceList =areaService.findList(area);
		model.addAttribute("provinceList",  provinceList );

		area=new Area();
		parent=new Area();
		if(contract!=null&&contract.getProvince()!=null&&(!StringUtils.isEmpty(contract.getProvince().getId()))){

			parent.setId(contract.getProvince().getId());
		}else{
			parent.setId( provinceList.get(0).getId());
		}
		area.setParent(parent);
		List<Area> cityList =areaService.findList(area);
		model.addAttribute("cityList",  cityList );
		if( contract.getProcInsId()!=null){
			if((contract.getStatus()!=1)&&(contract.getStatus()!=2)){
				List<Comment> comments=actTaskService.getProcessInstanceCommentList(contract.getProcInsId());
				model.addAttribute("comments",comments);
			}
		}
		//
		Dict dict =new Dict();
		dict.setType("contract_status");
		List<Dict> statusList= dictService.findList(dict);
		for(int i=0;i<statusList.size();i++){
			dict=statusList.get(i);
			if(dict.getValue().equals("1")){
				statusList.remove(i);
			}

		}
		for(int i=0;i<statusList.size();i++){
			dict=statusList.get(i);
			if(dict.getValue().equals("2")){
				statusList.remove(i);
			}

		}

		model.addAttribute("statusList",statusList);

		return "modules/contract/contractForm";
	}

	@RequestMapping(value = "save")
	public String save(Contract contract,Model model, RedirectAttributes redirectAttributes) throws Exception {
		if (!beanValidator(model, contract)){
			return form(contract,true, model);
		}
		if(StringUtils.isEmpty(contract.getId())){
			contract.setStatus(1);
            Office office = UserUtils.getUser().getOffice();
            contract.setOffice(office);
		}

		contService.save(contract);
		addMessage(redirectAttributes, "合同保存成功");
		//return "redirect:"+Global.getAdminPath()+"/cont/base/list/?repage";
		return "redirect:"+adminPath+"/cont/base/form/?id="+contract.getId()+"&readonly=false&repage";
	}
	
	@RequiresPermissions("cont:creator:edit")
	@RequestMapping(value = "delete")
	public String delete(Contract contract, RedirectAttributes redirectAttributes) {
		contService.delete(contract);
		addMessage(redirectAttributes, "删除合同成功");
		return "redirect:"+Global.getAdminPath()+"/cont/base/list/?repage";
	}


	@RequestMapping(value = {"city", ""})
	@ResponseBody
	public Map city(String provinceId) {
		Map<String,Object> result=new HashMap<String, Object>();
		Area area=new Area();
		Area parent=new Area();
		parent.setId(provinceId);
		area.setParent(parent);
		List<Area> citys =areaService.findList(area);
		result.put("citys",citys);
		result.put("result","success");

	    return result;
	}

	/**
	 * 验证是否可以启动流程
	 * @param id
	 * @return
	 */
	@ResponseBody

	@RequestMapping(value = "checkStartProcess")
	public Map checkStartProcess(String id) {
		Map<String,Object> result=new HashMap<String, Object>();
		result.put("result","success");
		ContItem contItem=new ContItem();
		contItem.setContractId(id);
		List<ContItem> itemlist=contItemService.findList(contItem);
		if(itemlist==null||itemlist.size()==0){
			result.put("result","error");
			result.put("msg","请先设置付款约定");
		}
		ContAttach contAttach=new ContAttach();
		contAttach.setContractId(id);
		List<ContAttach> attachlist=contAttachService.findList(contAttach);
		if(attachlist==null||attachlist.size()==0){
			result.put("result","error");
			result.put("msg","请先添加合同附件");
		}


			return result;
	}








}