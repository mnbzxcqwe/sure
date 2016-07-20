package com.sure.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sure.dao.AuthMapper;
import com.sure.exception.MyException;
import com.sure.pojo.Auth;
import com.sure.pojo.AuthTree;

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
		shiroFilerChainManager.initFilterChains(authMapper.findAuths(null));  
	} 
	
	/**
	 * 获取权限列表,拼装成树状结构
	 * @return
	 */
	public AuthTree getAuthTree(){
		List<Auth> authList = authMapper.findAuths(null);
		
		AuthTree authTree = new AuthTree(authList);
		
		return authTree;
	}
	
	/**
	 * 获取权限列表
	 * @return
	 */
	public List<Auth> getAuths(){
		List<Auth> auths = authMapper.findAuths(null);
		
		return auths;
	}
	
	/**
	 * 判断权限ID是否存在
	 * @param userName
	 * @return
	 */
	public boolean checkAuthIdExist(String AuthId){
		Auth auth = authMapper.selectByPrimaryKey(AuthId);
		
		return auth == null;
	}
	
	/**
	 * 新增权限
	 * @param auth
	 * @return
	 * @throws Exception
	 */
	public int addAuth(Auth auth) throws Exception{
		
		if(auth.getAuthParent() == null || "".equals(auth.getAuthParent())){
			throw new MyException("缺少父节点");
		}
		
		Auth parent = authMapper.selectByPrimaryKey(auth.getAuthParent());
		
		if(parent == null){
			throw new MyException("父节点ID错误");
		}
		
		if(parent.getAuthType() == 3){
			throw new MyException("3级权限下不能继续创建子权限");
		}
		
		auth.setAuthType(parent.getAuthType() + 1);
		
		return authMapper.insertSelective(auth);
	}
	
	/**
	 * 编辑权限
	 * @param auth
	 * @return
	 * @throws Exception
	 */
	public int editAuth(Auth auth) throws Exception{
		
		return authMapper.updateByPrimaryKeySelective(auth);
	}
	
	/**
	 * 删除权限
	 * @param authId
	 * @return
	 * @throws Exception
	 */
	public int delAuth(String authId) throws Exception{
		Map param = new HashMap();
		param.put("authParent", authId);
		
		List<Auth> children = authMapper.findAuths(param);
		
		if(children.size() > 0){
			throw new MyException("存在子权限,不能删除父权限");
		}
		
		return authMapper.deleteByPrimaryKey(authId);
	}
}
