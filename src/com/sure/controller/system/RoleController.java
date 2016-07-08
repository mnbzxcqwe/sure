package com.sure.controller.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.sure.controller.base.BaseController;

@Controller
@RequestMapping(value="/role")
public class RoleController extends BaseController {
	
	/**
	 * 角色管理页面
	 * @return
	 */
	@RequestMapping(value="/roleMngView")
	public ModelAndView roleMngView()throws Exception{
		ModelAndView mv = new ModelAndView();
		mv.setViewName("system/roleMng");
		return mv;
	}
	
	
}
