package com.sure.controller.system;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sure.controller.base.BaseController;
import com.sure.pojo.GridData;
import com.sure.pojo.User;
import com.sure.service.UserService;

@Controller
@RequestMapping(value="/user")
public class UserController extends BaseController {
	
	@Resource(name="UserService")
	UserService userService;
	
	/**
	 * 用户管理页面
	 * @return
	 */
	@RequestMapping(value="/userMngView")
	public ModelAndView userMngView()throws Exception{
		ModelAndView mv = new ModelAndView();
		mv.setViewName("system/userMng");
		return mv;
	}
	
	/**
	 * 获取用户列表
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getUsers")
	@ResponseBody
	public Object getUsers(int page, int rows)throws Exception{
		Map requestData = getRequestData();
		
		List<User> users = userService.getUsers(page, rows, requestData);
		
		return GridData.parse(users);
	}
	
	/**
	 * 判断用户名是否存在
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/checkUserNameExist")
	@ResponseBody
	public boolean checkUserNameExist(String userName)throws Exception{
		return userService.checkUserNameExist(userName);
	}
	
	/**
	 * 新增用户
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/addUser")
	@ResponseBody
	public int addUser(User user)throws Exception{
		return userService.addUser(user);
	}
	
	/**
	 * 编辑用户
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/editUser")
	@ResponseBody
	public int editUser(User user)throws Exception{
		return userService.editUser(user);
	}
	
	/**
	 * 删除用户
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/delUser")
	@ResponseBody
	public int delUser(String userName)throws Exception{
		return userService.delUser(userName);
	}
}
