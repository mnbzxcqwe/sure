package com.sure.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import com.sure.dao.UserMapper;
import com.sure.pojo.User;
import com.sure.utils.Const;
import com.sure.utils.MD5;

@Service("LoginService")
public class LoginService {
	
	@Resource
	UserMapper userMapper;

	/**
	 * 登录验证
	 * @param requestData
	 * @return
	 */
	public Map login(Map requestData){
		Map result = new HashMap();
		
		String userName = (String) requestData.get("name");
		String password = (String) requestData.get("pwd");
		password = MD5.md5(password);
		
		User user = userMapper.selectByPrimaryKey(userName);
		
		if(user == null){
			result.put("result", "账号不存在");
		}else if(password == null){
			result.put("result", "密码不能为空");
		}else if(!password.equals(user.getUserPassword())){
			result.put("result", "密码错误");
		}else{
			result.put("result", "success");
			
			//shiro加入身份验证
			Subject subject = SecurityUtils.getSubject(); 
			//先登出,再重新登录,以免权限缓存出错
			subject.logout();
		    UsernamePasswordToken token = new UsernamePasswordToken(user.getUserName(), user.getUserPassword()); 
		    subject.login(token);
		    
		    Session session = subject.getSession();
		    session.setAttribute(Const.SESSION_USER_NAME, user.getUserName());
		    
		    //故意触发doGetAuthorizationInfo第一次执行
		    subject.isPermitted("1");
		}
		
		return result;
		
	}
	
	/**
	 * 登出注销
	 */
	public void logout(){
		//shiro销毁登录
		Subject subject = SecurityUtils.getSubject(); 
		subject.logout();
	}
	
}
