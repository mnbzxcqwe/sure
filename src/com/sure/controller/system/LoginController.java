package com.sure.controller.system;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sure.controller.base.BaseController;
import com.sure.service.LoginService;
import com.sure.utils.Const;

@Controller
public class LoginController extends BaseController {
	
	@Resource(name="LoginService")
	LoginService loginService;

	/**
	 * 访问登录页
	 * @return
	 */
	@RequestMapping(value="/login_toLogin")
	public ModelAndView toLogin()throws Exception{
		ModelAndView mv = new ModelAndView();
		mv.setViewName("system/login");
		return mv;
	}
	
	/**
	 * 请求登录，验证用户
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/login_login" ,produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object login()throws Exception{
		Map requestData = getRequestData();
		
		Map result = loginService.login(requestData);
		
		return result;
	}
	
	/**
	 * 注销
	 * @return
	 */
	@RequestMapping(value="/logout")
	public ModelAndView logout(){
		
		loginService.logout();
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("system/login");
		return mv;
	}
	
	/**
	 * 主页面
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/index")
	@ResponseBody
	public ModelAndView index()throws Exception{
		ModelAndView mv = new ModelAndView();
		
		//shiro管理的session
		Subject currentUser = SecurityUtils.getSubject();  
		Session session = currentUser.getSession();
		
		mv.addObject("authTree", session.getAttribute(Const.SESSION_AUTH_TREE));
		mv.setViewName("common/index");
		return mv;
	}
	
	/**
	 * 非法访问页面
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/unauthorized")
	public ModelAndView unauthorized()throws Exception{
		ModelAndView mv = new ModelAndView();
		mv.setViewName("system/unauthorized");
		return mv;
	}
	
}
