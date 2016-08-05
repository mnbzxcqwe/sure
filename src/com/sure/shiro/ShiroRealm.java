package com.sure.shiro;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;

import com.sure.dao.AuthMapper;
import com.sure.pojo.Auth;
import com.sure.pojo.AuthTree;
import com.sure.utils.Config;
import com.sure.utils.Const;


/**
 * @author Bug.zheng
 *  2016-6-20
 */
public class ShiroRealm extends AuthorizingRealm {
	
	@Resource
	AuthMapper authMapper;

	/*
	 * 登录信息和用户验证信息验证(non-Javadoc)
	 * @see org.apache.shiro.realm.AuthenticatingRealm#doGetAuthenticationInfo(org.apache.shiro.authc.AuthenticationToken)
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

		 String userName = (String)token.getPrincipal();  				//得到用户名 
	     String password = new String((char[])token.getCredentials()); 	//得到密码
		
	     if(null != userName && null != password){
	    	 return new SimpleAuthenticationInfo(userName, password, getName());
	     }else{
	    	 return null;
	     }
	     
	}
	
	/*
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用,负责在应用程序中决定用户的访问控制的方法(non-Javadoc)
	 * @see org.apache.shiro.realm.AuthorizingRealm#doGetAuthorizationInfo(org.apache.shiro.subject.PrincipalCollection)
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection pc) {
		String userName = (String)pc.getPrimaryPrincipal();
		
		Subject currentUser = SecurityUtils.getSubject();  
		Session session = currentUser.getSession();
		
		SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();

		List<Auth> userAuths;
		
		if(Config.getString("ADMINISTRATORS_USER_NAME", "admin").equals(userName)){ //超级管理员
			userAuths = authMapper.findAuths(null);
		}else{ //普通用户
			userAuths = authMapper.findUserAuthsByUserName(userName);
		}
		
		//保存用户权限树
		AuthTree authTree = new AuthTree(userAuths);
		session.setAttribute(Const.SESSION_AUTH_TREE, authTree);
		
		List<String> authIds = new ArrayList<String>();
		
		//加入shiro权限列表,同时保存到session缓存
		for(Auth userAuth : userAuths){
			authIds.add(userAuth.getAuthId());
			info.addStringPermission(userAuth.getAuthId());
		}
		session.setAttribute(Const.SESSION_AUTH_IDS, authIds);
		
		return info;
	}
	
	public void clearAllCachedAuthorizationInfoPublic(){
		getAuthorizationCache().clear();
	}
	
	public void clearCachedAuthorizationInfoPublic(PrincipalCollection pc){
		clearCachedAuthorizationInfo(pc);
	}
	
}
