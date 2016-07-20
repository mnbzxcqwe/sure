package com.sure.controller.system;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sure.controller.base.BaseController;
import com.sure.pojo.Auth;
import com.sure.service.AuthService;

@Controller
@RequestMapping(value="/auth")
public class AuthController extends BaseController {
	
	@Resource(name="AuthService")
	AuthService authService;
	
	/**
	 * 权限管理页面
	 * @return
	 */
	@RequestMapping(value="/authMngView")
	public ModelAndView authMngView()throws Exception{
		ModelAndView mv = new ModelAndView();
		mv.setViewName("system/authMng");
		return mv;
	}
	
	/**
	 * 获取权限列表
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getAuths", produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object getAuths()throws Exception{
		
		List<Auth> auths = authService.getAuths();
		
		return auths;
	}
	
	/**
	 * 判断权限ID是否存在
	 * @param authId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/checkAuthIdExist")
	@ResponseBody
	public boolean checkAuthIdExist(String authId)throws Exception{
		return authService.checkAuthIdExist(authId);
	}
	
	/**
	 * 新增权限
	 * @param auth
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/addAuth", produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object addAuth(Auth auth)throws Exception{
		
		return authService.addAuth(auth);
	}
	
	/**
	 * 编辑权限
	 * @param auth
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/editAuth", produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object editAuth(Auth auth)throws Exception{
		
		return authService.editAuth(auth);
	}
	
	/**
	 * 删除权限
	 * @param authId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/delAuth", produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object delAuth(String authId)throws Exception{
		
		return authService.delAuth(authId);
	}
}
