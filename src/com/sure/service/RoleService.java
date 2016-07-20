package com.sure.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.sure.dao.RoleMapper;
import com.sure.pojo.Role;
import com.sure.utils.Const;

@Service("RoleService")
public class RoleService {
	
	@Resource
	RoleMapper roleMapper;
	
	/**
	 * 获取角色列表
	 * @param pageNum
	 * @param pageSize
	 * @param param
	 * @return
	 */
	public List<Role> getRoles(int pageNum, int pageSize, Map param){
		
		PageHelper.startPage(pageNum, pageSize);
		List<Role> roles = roleMapper.findRoles(param);
		
		return roles;
	}
	
	/**
	 * 新增角色
	 * @param role
	 * @return
	 */
	public int addRole(Role role){
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		
		
		role.setCreateBy((String) session.getAttribute(Const.SESSION_USER_NAME));
		
		int result = roleMapper.insertSelective(role);
		
		return result;
	}
	
	/**
	 * 编辑角色
	 * @param role
	 * @return
	 */
	public int editRole(Role role){
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		
		int result = roleMapper.updateByPrimaryKeySelective(role);
		
		return result;
	}
	
	/**
	 * 删除角色
	 * @param roleId
	 * @return
	 */
	public int delRole(int roleId){
		
		int result = roleMapper.deleteByPrimaryKey(roleId);
		
		return result;
	}
}
