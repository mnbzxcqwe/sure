package com.sure.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.sure.dao.AuthMapper;
import com.sure.dao.RoleAuthMapper;
import com.sure.dao.RoleMapper;
import com.sure.dao.UserRoleMapper;
import com.sure.pojo.Role;
import com.sure.pojo.RoleAuthKey;
import com.sure.utils.Const;

@Service("RoleService")
public class RoleService {
	
	@Resource
	RoleMapper roleMapper;
	
	@Resource
	AuthMapper authMapper;
	
	@Resource
	RoleAuthMapper roleAuthMapper;
	
	@Resource
	UserRoleMapper userRoleMapper;
	
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
	@Transactional
	public int delRole(int roleId){
		
		userRoleMapper.deleteByRoleId(roleId);
		roleAuthMapper.deleteByRoleId(roleId);
		
		return roleMapper.deleteByPrimaryKey(roleId);
	}
	
	/**
	 * 获取角色权限
	 * @param roleId
	 * @return
	 */
	public Map getRoleAuths(int roleId){
		Map result = new HashMap();
		
		result.put("roleAuthIds", roleAuthMapper.findAuthIdsByRoleId(roleId));
		result.put("allAuths", authMapper.findAuths(null));
		
		return result;
	}
	
	/**
	 * 分配角色权限
	 * @param roleId
	 * @param authIds
	 * @return
	 */
	@Transactional
	public int updateRoleAuths(int roleId, List<String> authIds){
		roleAuthMapper.deleteByRoleId(roleId);
		
		RoleAuthKey roleAuth = new RoleAuthKey();
		roleAuth.setRoleId(roleId);
		
		int count = 0;
		
		if(authIds != null){
			for(int i=0,len=authIds.size(); i<len; i++){
				roleAuth.setAuthId(authIds.get(i));
				
				count += roleAuthMapper.insert(roleAuth);
			}
		}
		
		
		return count;
	}
}
