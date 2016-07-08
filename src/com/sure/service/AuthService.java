package com.sure.service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sure.dao.AuthMapper;

@Service("AuthService")
public class AuthService {
	
	@Resource
	AuthMapper authMapper;
	
	@Resource(name="ShiroFilerChainManager")
	ShiroFilerChainManager shiroFilerChainManager;

	/**
	 * 初始化动态URL权限控制
	 */
	@PostConstruct  
	public void initFilterChain() {  
		shiroFilerChainManager.initFilterChains(authMapper.findAuths());  
	} 
	
}
