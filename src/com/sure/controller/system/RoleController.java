package com.sure.controller.system;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sure.controller.base.BaseController;
import com.sure.pojo.GridData;
import com.sure.pojo.Role;
import com.sure.service.RoleService;

@Controller
@RequestMapping(value="/role")
public class RoleController extends BaseController {
	
	@Resource(name="RoleService")
	RoleService roleService;
	
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
	
	/**
	 * 获取角色列表
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getRoles")
	@ResponseBody
	public Object getRoles(int page, int rows)throws Exception{
		Map requestData = getRequestData();
		
		List<Role> role = roleService.getRoles(page, rows, requestData);
		
		return GridData.parse(role);
	}
	
	/**
	 * 新增角色
	 * @param role
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/addRole")
	@ResponseBody
	@RequiresPermissions("AddRole")
	public int addRole(Role role)throws Exception{
		return roleService.addRole(role);
	}
	
	/**
	 * 编辑角色
	 * @param role
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/editRole")
	@ResponseBody
	@RequiresPermissions("EditRole")
	public int editRole(Role role)throws Exception{
		return roleService.editRole(role);
	}
	
	/**
	 * 删除角色
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/delRole")
	@ResponseBody
	@RequiresPermissions("DelRole")
	public int delRole(int roleId)throws Exception{
		return roleService.delRole(roleId);
	}
	
	/**
	 * 获取角色权限
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getRoleAuths", produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object getRoleAuths(int roleId)throws Exception{
		
		return roleService.getRoleAuths(roleId);
	}
	
	/**
	 * 分配角色权限
	 * @param roleId
	 * @param authIds
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/updateRoleAuths", produces="application/json;charset=UTF-8")
	@ResponseBody
	@RequiresPermissions("AllotAuth")
	public Object updateRoleAuths(int roleId, @RequestParam(value="authIds[]", required=false) List<String> authIds)throws Exception{
		
		return roleService.updateRoleAuths(roleId, authIds);
	}
}
