package com.sure.service;

import java.util.Date;
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
import com.sure.dao.RoleMapper;
import com.sure.dao.UserMapper;
import com.sure.dao.UserRoleMapper;
import com.sure.pojo.User;
import com.sure.pojo.UserRoleKey;
import com.sure.utils.Const;
import com.sure.utils.MD5;

@Service("UserService")
public class UserService {
	
	@Resource
	UserMapper userMapper;
	
	@Resource
	UserRoleMapper userRoleMapper;
	
	@Resource
	RoleMapper roleMapper;
	
	/**
	 * 获取用户列表
	 * @param pageNum
	 * @param pageSize
	 * @param param
	 * @return
	 */
	public List<User> getUsers(int pageNum, int pageSize, Map param){
		
		PageHelper.startPage(pageNum, pageSize);
		List<User> users = userMapper.findUsers(param);
		
		return users;
	}
	
	/**
	 * 判断用户名是否存在
	 * @param userName
	 * @return
	 */
	public boolean checkUserNameExist(String userName){
		User user = userMapper.selectByPrimaryKey(userName);
		
		return user == null;
	}
	
	/**
	 * 新增用户
	 * @param user
	 * @return
	 */
	public int addUser(User user){
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		
		user.setUserPassword(MD5.md5(user.getUserPassword()));
		
		user.setCreateDate(new Date());
		
		user.setEditBy((String) session.getAttribute(Const.SESSION_USER_NAME));
		
		int result = userMapper.insertSelective(user);
		
		return result;
	}
	
	/**
	 * 编辑用户
	 * @param user
	 * @return
	 */
	public int editUser(User user){
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		
		if(user.getUserPassword() != null && !user.getUserPassword().isEmpty()){
			user.setUserPassword(MD5.md5(user.getUserPassword()));
		}else{
			user.setUserPassword(null);
		}
		
		user.setUpdateDate(new Date());
		
		user.setEditBy((String) session.getAttribute(Const.SESSION_USER_NAME));
		
		int result = userMapper.updateByPrimaryKeySelective(user);
		
		return result;
	}
	
	/**
	 * 删除用户
	 * @param userName
	 * @return
	 */
	@Transactional
	public int delUser(String userName){
		
		userRoleMapper.deleteByUserName(userName);
		
		return userMapper.deleteByPrimaryKey(userName);
	}
	
	/**
	 * 获取用户角色
	 * @param userName
	 * @return
	 */
	public Map getUserRoles(String userName){
		Map result = new HashMap();
		
		result.put("userRoleIds", userRoleMapper.findRoleIdsByUserName(userName));
		result.put("allRoles", roleMapper.findRoles(null));
		
		return result;
	}
	
	/**
	 * 分配用户角色
	 * @param userName
	 * @param roleIds
	 * @return
	 */
	@Transactional
	public int updateUserRoles(String userName, List<Integer> roleIds){
		userRoleMapper.deleteByUserName(userName);
		
		UserRoleKey userRole = new UserRoleKey();
		userRole.setUserName(userName);
		
		int count = 0;
		
		if(roleIds != null){
			for(int i=0,len=roleIds.size(); i<len; i++){
				userRole.setRoleId(roleIds.get(i));
				
				count += userRoleMapper.insert(userRole);
			}
		}
		
		return count;
	}
}
