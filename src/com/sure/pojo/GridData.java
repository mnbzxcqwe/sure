package com.sure.pojo;

import java.util.List;

import com.github.pagehelper.Page;

/**
 * easyui datagrid接收标准格式
 * @author Bug.zheng
 *
 */
public class GridData {
	
	private List rows;
	
	private long total;
	
	public static GridData parse(List list){
		return new GridData(list);
	}
	
	private GridData(){}
	
	private GridData(List list){
		if(list instanceof Page){
			Page page = (Page)list;
			this.rows = page.getResult();
			this.total = page.getTotal();
		}else{
			this.rows = list;
			this.total = 0;
		}
	}

	public List getRows() {
		return rows;
	}

	public void setRows(List rows) {
		this.rows = rows;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}
	
	

}
