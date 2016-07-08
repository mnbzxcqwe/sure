package com.sure.pojo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AuthTree {
    private String authId;
    private String authName;
    private String authEntry;
    private Integer authType;
    private Integer authOrder;
    private String authParent;
    
    private List<AuthTree> children = new ArrayList<AuthTree>();
    
    private AuthTree(Auth auth){
    	this.authId = auth.getAuthId();
    	this.authName = auth.getAuthName();
    	this.authEntry = auth.getAuthEntry();
    	this.authType = auth.getAuthType();
    	this.authOrder = auth.getAuthOrder();
    	this.authParent = auth.getAuthParent();
    }
    
    public AuthTree(List<Auth> authList){
    	//初始化根节点root
    	this.authId = "Root";
    	this.authName = "根目录";
    	this.authEntry = "";
    	this.authType = 0;
    	this.authOrder = 0;
    	this.authParent = "";
    	
    	Map<String, AuthTree> oneAuthTreeMap = new LinkedHashMap<String, AuthTree>();
    	Map<String, AuthTree> twoAuthTreeMap = new LinkedHashMap<String, AuthTree>();
    	Map<String, AuthTree> threeAuthTreeMap = new LinkedHashMap<String, AuthTree>();
    	
    	for(Auth auth : authList){
    		if(auth.getAuthType() == 0){ //根节点,更新信息,以数据库为准
    			this.authId = auth.getAuthId();
    	    	this.authName = auth.getAuthName();
    	    	this.authEntry = auth.getAuthEntry();
    	    	this.authType = auth.getAuthType();
    	    	this.authOrder = auth.getAuthOrder();
    	    	this.authParent = auth.getAuthParent();
    		}else if(auth.getAuthType() == 1){
    			oneAuthTreeMap.put(auth.getAuthId(), new AuthTree(auth));
    		}else if(auth.getAuthType() == 2){
    			twoAuthTreeMap.put(auth.getAuthId(), new AuthTree(auth));
    		}else if(auth.getAuthType() == 3){
    			threeAuthTreeMap.put(auth.getAuthId(), new AuthTree(auth));
    		}
    	}
    	
    	//将3级权限添加到2级权限子节点
    	for(AuthTree threeAuthTree : threeAuthTreeMap.values()){
    		twoAuthTreeMap.get(threeAuthTree.getAuthParent()).getChildren().add(threeAuthTree);
    	}
    	
    	//将2级权限添加到1级权限子节点
    	for(AuthTree twoAuthTree : twoAuthTreeMap.values()){
    		oneAuthTreeMap.get(twoAuthTree.getAuthParent()).getChildren().add(twoAuthTree);
    	}
    	
    	//将1级权限添加到根节点的子节点
    	for(AuthTree oneAuthTree : oneAuthTreeMap.values()){
    		this.children.add(oneAuthTree);
    	}
    }
    
	public String getAuthId() {
		return authId;
	}
	public void setAuthId(String authId) {
		this.authId = authId;
	}
	public String getAuthName() {
		return authName;
	}
	public void setAuthName(String authName) {
		this.authName = authName;
	}
	public String getAuthEntry() {
		return authEntry;
	}
	public void setAuthEntry(String authEntry) {
		this.authEntry = authEntry;
	}
	public Integer getAuthType() {
		return authType;
	}
	public void setAuthType(Integer authType) {
		this.authType = authType;
	}
	public Integer getAuthOrder() {
		return authOrder;
	}
	public void setAuthOrder(Integer authOrder) {
		this.authOrder = authOrder;
	}
	public String getAuthParent() {
		return authParent;
	}
	public void setAuthParent(String authParent) {
		this.authParent = authParent;
	}
	public List<AuthTree> getChildren() {
		return children;
	}
	public void setChildren(List<AuthTree> children) {
		this.children = children;
	}
    
    
}