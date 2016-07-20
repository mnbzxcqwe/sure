<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<%-- jsp文件头和头部 --%>
		<%@ include file="../common/top.jsp"%>
		
		<script type="text/javascript">
			$(function(){
				$(window).resize(function(){
					var fullScreen = getFullScreen("grid");
					$("#grid").datagrid('resize',{height:fullScreen});
				});
				
				$(window).trigger('resize');
			});
		
		
			function formatIsLocked(value,row,index){
				if(value == false){
					return "否";
				}else{
					return "已锁定";
				}
			}
			
			function showAddUser(){
				$('#addDlg').dialog('open');
				$('#addForm').form('reset');
			}
			
			function showEditUser(){
				var row = $('#grid').datagrid('getSelected');
				if (row){
					$('#editDlg').dialog('open');
					$('#editForm').form('load',row);
				}else{
					$.messager.alert("提示","请选择需要编辑的用户");
				}
			}
			
			function doSearch(){
				$("#grid").datagrid('load',$("#searchForm").serializeObject());
			}
			
			function doAddUser(){
				$('#addForm').form('submit',{
					onSubmit: function(){
						return $(this).form('validate');
					},
					success: function(result){
						var result = eval('('+result+')');
						if (result.errorMsg){
							$.messager.alert('Error',result.errorMsg);
						} else {
							$('#addDlg').dialog('close');
							$('#grid').datagrid('reload');
						}
					}
				});
			}
			
			function doEditUser(){
				$('#editForm').form('submit',{
					onSubmit: function(){
						return $(this).form('validate');
					},
					success: function(result){
						var result = eval('('+result+')');
						if (result.errorMsg){
							$.messager.alert('Error',result.errorMsg);
						} else {
							$('#editDlg').dialog('close');
							$('#grid').datagrid('reload');
						}
					}
				});
			}
			
			function doDelUser(){
				var row = $('#grid').datagrid('getSelected');
				if (row){
					$.messager.confirm('确认','确定删除该用户?',function(r){
						if (r){
							$.ajax({
								url:'<%=basePath%>/user/delUser',
								data:{userName:row.userName},
								type:'post',
								dataType:'json',
								success:function(result){
									if (result.errorMsg){
										$.messager.alert('Error',result.errorMsg);
									} else {
										$('#grid').datagrid('reload');
									}
								}
							});
						}
					});
				}else{
					$.messager.alert("提示","请选择需要删除的用户");
				}
			}
		</script>
		
	</head>
	
	<body>
		<div id="searchDiv" class="easyui-panel" style="width:100%;padding:10px;margin-bottom: 10px;">
			<form id="searchForm">
				<label for="userName">账号:</label>
				<input class="easyui-validatebox" type="text" id="userName" name="userName" />
				
				<label for="isLocked">是否锁定:</label>
				<select class="easyui-combobox" id="isLocked" name="isLocked" editable="false">
				    <option>全部</option>
				    <option value="0">否</option>
				    <option value="1">是</option>
				</select>
				
				<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="doSearch()">搜索</a>
			</form>
		</div>
		
		<div id="tb">
			<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="showAddUser()">新增用户</a>
			<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="showEditUser()">编辑用户</a>
			<a href="#" class="easyui-linkbutton" iconCls="icon-cut" plain="true" onclick="doDelUser()">删除用户</a>
		</div>
		
		<table id="grid" class="easyui-datagrid" style="width:100%;"
			data-options="url:'<%=basePath%>/user/getUsers',pagination:true,fitColumns:true,singleSelect:true,toolbar:'#tb'">
			<thead>
				<tr>
					<th data-options="field:'userName',width:100">账号</th>
					<th data-options="field:'isLocked',width:100,formatter:formatIsLocked">是否锁定</th>
					<th data-options="field:'remark',width:100">备注</th>
				</tr>
			</thead>
		</table>
		
		<%-- 新增 --%>
		<div id="addDlg" class="easyui-dialog" style="width:400px;height:280px;padding:10px 20px"
			closed="true" buttons="#addDlgBtns" title="新增用户" modal="true" resizable="true">
			<div class="ftitle">用户信息</div>
			<form id="addForm" method="post" data-options="url:'<%=basePath%>/user/addUser'">
				<div class="fitem">
					<label>账号:</label>
					<input name="userName" class="easyui-validatebox" required="true" validType="remote['<%=basePath%>/user/checkUserNameExist','userName']" invalidMessage="该用户名已存在">
				</div>
				<div class="fitem">
					<label>密码:</label>
					<input type="password" name="userPassword" class="easyui-validatebox" required="true">
				</div>
				<div class="fitem">
					<label>是否锁定:</label>
					<select class="easyui-combobox" name="isLocked" editable="false">
					    <option value="0" selected="selected">否</option>
					    <option value="1">是</option>
					</select>
				</div>
				<div class="fitem">
					<label>备注:</label>
					<input name="remark" class="easyui-validatebox">
				</div>
			</form>
		</div>
		<div id="addDlgBtns">
			<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="doAddUser()">保存</a>
			<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#addDlg').dialog('close')">取消</a>
		</div>
		
		<%-- 编辑 --%>
		<div id="editDlg" class="easyui-dialog" style="width:400px;height:280px;padding:10px 20px"
			closed="true" buttons="#editDlgBtns" title="编辑用户" modal="true" resizable="true">
			<div class="ftitle">用户信息</div>
			<form id="editForm" method="post" data-options="url:'<%=basePath%>/user/editUser'">
				<div class="fitem">
					<label>账号:</label>
					<input name="userName" class="easyui-validatebox" readonly="readonly">
				</div>
				<div class="fitem">
					<label>密码:</label>
					<input type="password" name="userPassword" class="easyui-validatebox" placeholder="无需修改密码请置空">
				</div>
				<div class="fitem">
					<label>是否锁定:</label>
					<select class="easyui-combobox" name="isLocked" editable="false">
					    <option value="0">否</option>
					    <option value="1">是</option>
					</select>
				</div>
				<div class="fitem">
					<label>备注:</label>
					<input name="remark" class="easyui-validatebox">
				</div>
			</form>
		</div>
		<div id="editDlgBtns">
			<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="doEditUser()">保存</a>
			<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#editDlg').dialog('close')">取消</a>
		</div>
		
	</body>
</html>