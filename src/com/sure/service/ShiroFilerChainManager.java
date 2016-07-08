package com.sure.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.NamedFilterList;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.sure.pojo.Auth;

@Service("ShiroFilerChainManager")
public class ShiroFilerChainManager {
	@Resource
	private DefaultFilterChainManager filterChainManager;  
    private Map<String, NamedFilterList> defaultFilterChains;  
    
    @PostConstruct  
    public void init() {  
        defaultFilterChains =   
          new LinkedHashMap<String, NamedFilterList>(filterChainManager.getFilterChains());  
    }
    
    public void initFilterChains(List<Auth> auths) {  
        //1、首先删除以前老的filter chain  
        filterChainManager.getFilterChains().clear();  
          
        //2、循环注册filter chain  
        for (Auth auth : auths) {
            //注册perms filter  
            if (auth.getAuthType() == 2 && !StringUtils.isEmpty(auth.getAuthEntry())) {  
                filterChainManager.addToChain(auth.getAuthEntry(), "perms", auth.getAuthId());  
            }  
        }
        
        //3、注册默认的filter chain,默认的通常是静态资源,影响范围大,故放到最后注册
        if(defaultFilterChains != null) {  
            filterChainManager.getFilterChains().putAll(defaultFilterChains);  
        }
    }  
}
