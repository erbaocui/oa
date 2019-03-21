/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.entity.Certificate;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.CertificateService;
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
 * 员工证书Controller
 * @author cuijp
 * @version 2019-02-27
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/user/certificate")
public class CertificateController extends BaseController {

	@Autowired
	private CertificateService certificateService;
	
	@ModelAttribute
	public Certificate get(@RequestParam(required=false) String id) {
		Certificate entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = certificateService.get(id);
		}
		if (entity == null){
			entity = new Certificate();
		}
		return entity;
	}
	

	@RequestMapping(value = {"list", ""})
	public String list(Certificate certificate, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<Certificate> list=certificateService.findList(certificate);
		model.addAttribute("user",  certificate.getUser());
		model.addAttribute("list", list);
		model.addAttribute("certificate", certificate);
		model.addAttribute("show", false);
		return "modules/sys/userCertificateList";
	}

	@RequestMapping(value = "form")
	public String form(Certificate certificate,Model model) {
		if(null!=certificate&&null!=certificate.getId()){
			certificate =get(certificate.getId());
		}
		User user = new User();
		user.setId(certificate.getUser().getId());
		Certificate findCertificate=new Certificate();
		findCertificate.setUser(user);
		List<Certificate> list=certificateService.findList(findCertificate);
		model.addAttribute("user",user);
		model.addAttribute("certificate", certificate);
		model.addAttribute("show", true);
		model.addAttribute("list", list);
		return "modules/sys/userCertificateList";
	}

	@RequestMapping(value = "save")
	public String save(Certificate certificate, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, certificate)){
			return form(certificate, model);
		}
		certificateService.save(certificate);
		certificate=get(certificate.getId());
		User user = new User();
		user.setId(certificate.getUser().getId());
		Certificate findCertificate=new Certificate();
		findCertificate.setUser(user);
		List<Certificate> list=certificateService.findList(findCertificate);
		addMessage(redirectAttributes, "保存员工证书成功");
		model.addAttribute("user",user);
		model.addAttribute("list", list);
		model.addAttribute("certificate", certificate);
		model.addAttribute("show", false);
		return "redirect:"+Global.getAdminPath()+"/sys/user/certificate/list/?user.id="+certificate.getUser().getId()+"&repage";

	}

	@RequestMapping(value = "delete")
	public String delete(Certificate certificate,Model model, RedirectAttributes redirectAttributes) {
		certificateService.delete(certificate);
		User user = new User();
		user.setId(certificate.getUser().getId());
		Certificate findCertificate=new Certificate();
		findCertificate.setUser(user);
		List<Certificate> list=certificateService.findList(findCertificate);
		model.addAttribute("user",user);
		model.addAttribute("show", false);
		model.addAttribute("list", list);
		addMessage(redirectAttributes, "删除员工证书成功");
		return "redirect:"+Global.getAdminPath()+"/sys/user/certificate/list?user.id="+certificate.getUser().getId()+"&repage";
	}

}