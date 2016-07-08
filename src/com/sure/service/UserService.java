package com.sure.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.sure.dao.UserMapper;
import com.sure.pojo.User;
import com.sure.utils.Const;
import com.sure.utils.MD5;

@Service("UserService")
public class UserService {
	
	@Resource
	UserMapper userMapper;
	
	/**
	 * 获取用户列表
	 * @param pageNum
	 * @param pageSize
	 * @param para
	 * @return
	 */
	public List<User> getUsers(int pageNum, int pageSize, Map para){
		
		PageHelper.startPage(pageNum, pageSize);
		List<User> users = userMapper.findUsers(para);
		
		return users;
	}
	
	/**
	 * 判断用户名是否存在
	 * @param userName
	 * @return
	 */
	public boolean checkNuserNameExist(String userName){
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
	public int delUser(String userName){
		
		int result = userMapper.deleteByPrimaryKey(userName);
		
		return result;
	}
}
