<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<!-- jsp文件头和头部 -->
		<%@ include file="../common/top.jsp"%>
		
		<script type="text/javascript" src="<%=basePath%>/resources/zTree/jquery.ztree.all.min.js"></script>
	
		<link rel="stylesheet" type="text/css" href="<%=basePath%>/resources/zTree/zTreeStyle/zTreeStyle.css">
		
		<script type="text/javascript">
		
			var authTree;
			
			$(function(){
				$(window).resize(function(){
					var fullScreen = getFullScreen("grid");
					$("#grid").datagrid('resize',{height:fullScreen});
				});
				
				$(window).trigger('resize');
			});
		
		
			function showAddRole(){
				$('#addDlg').dialog('open');
				$('#addForm').form('reset');
			}
			
			function showEditRole(){
				var row = $('#grid').datagrid('getSelected');
				if (row){
					$('#editDlg').dialog('open');
					$('#editForm').form('load',row);
				}else{
					$.messager.alert("提示","请选择需要编辑的角色");
				}
			}
			
			function showRoleAuth(){
				var row = $('#grid').datagrid('getSelected');
				if (row){
					$('#authDlg').dialog('open');
					
					$('#auth_roleId').val(row.roleId);
					
					getAuthTree(row.roleId);
				}else{
					$.messager.alert("提示","请选择需要分配权限的角色");
				}
			}
			
			function doSearch(){
				$("#grid").datagrid('load',$("#searchForm").serializeObject());
			}
			
			function doAddRole(){
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
			
			function doEditRole(){
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
			
			function doDelRole(){
				var row = $('#grid').datagrid('getSelected');
				if (row){
					$.messager.confirm('确认','确定删除该角色?',function(r){
						if (r){
							$.ajax({
								url:'<%=basePath%>/role/delRole',
								data:{roleId:row.roleId},
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
					$.messager.alert("提示","请选择需要删除的角色");
				}
			}
			
			function doUpdateRoleAuths(){
				var nodes = authTree.getCheckedNodes(true);
				
				var authIds = new Array();
				
				for(var i=0,len=nodes.length; i<len; i++){
					authIds.push(nodes[i].authId);
				}
				
				var roleId = $('#auth_roleId').val();
				
				$.ajax({
					url:'<%=basePath%>/role/updateRoleAuths',
					data:{roleId:roleId,authIds:authIds},
					type:'post',
					dataType:'json',
					success:function(result){
						if (result.errorMsg){
							$.messager.alert('Error',result.errorMsg);
						} else {
							$('#authDlg').dialog('close');
						}
					}
				});
			}
			
			function getAuthTree(roleId){
				var setting = {
					data: {
						key: {
							name: "authName"
						},
						simpleData: {
							enable: true,
							idKey: "authId",
							pIdKey: "authParent",
							rootPId: 1
						}
					},
					check: {
						enable: true,
						chkStyle: "checkbox",
						chkboxType: { "Y": "p", "N": "s" }
					}
				};
				
				$.ajax({
					url:'<%=basePath%>/role/getRoleAuths',
					data:{roleId:roleId},
					type:'post',
					dataType:'json',
					success:function(result){
						if (result.errorMsg){
							$.messager.alert('Error',result.errorMsg);
						} else {
							authTree = $.fn.zTree.init($("#authTree"), setting, handleTreeData(result));
						}
					}
				});
			}
			
			function handleTreeData(data){
				
				var auths = data.allAuths;
				var roleAuthIds = data.roleAuthIds;
				
				for(var i=0,len=auths.length; i<len; i++){
					if(auths[i].authType == 0){
						auths[i].isParent = true;
						auths[i].open = true;
						auths[i].nocheck = true;
					}else if(auths[i].authType == 1 || auths[i].authType == 2){
						auths[i].isParent = true;
						auths[i].open = true;
						auths[i].checked = (roleAuthIds.indexOf(auths[i].authId) >= 0)
					}else{
						auths[i].isParent = false;
						auths[i].checked = (roleAuthIds.indexOf(auths[i].authId) >= 0)
					}
				}
				return auths;
			}
		</script>
	</head>
	
	<body >
		<div id="searchDiv" class="easyui-panel" style="width:100%;padding:10px;margin-bottom: 10px;">
			<form id="searchForm">
				<label for="roleName">角色名:</label>
				<input class="easyui-validatebox" type="text" id="roleName" name="roleName" />
				
				<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="doSearch()">搜索</a>
			</form>
		</div>
		
		<div id="tb">
			<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="showAddRole()">新增角色</a>
			<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="showEditRole()">编辑角色</a>
			<a href="#" class="easyui-linkbutton" iconCls="icon-cut" plain="true" onclick="doDelRole()">删除角色</a>
			<a href="#" class="easyui-linkbutton" iconCls="icon-filter" plain="true" onclick="showRoleAuth()">分配角色权限</a>
		</div>
		
		<table id="grid" class="easyui-datagrid" style="width:100%;"
			data-options="url:'<%=basePath%>/role/getRoles',pagination:true,fitColumns:true,singleSelect:true,toolbar:'#tb'">
			<thead>
				<tr>
					<th data-options="field:'roleId',width:100">角色id</th>
					<th data-options="field:'roleName',width:100">角色名</th>
					<th data-options="field:'roleDesc',width:100">描述</th>
				</tr>
			</thead>
		</table>
		
		<%-- 新增 --%>
		<div id="addDlg" class="easyui-dialog" style="width:400px;height:300px;padding:10px 20px"
			closed="true" buttons="#addDlgBtns" title="新增角色" modal="true" resizable="true">
			<div class="ftitle">角色信息</div>
			<form id="addForm" method="post" data-options="url:'<%=basePath%>/role/addRole'">
				<div class="fitem">
					<label>角色名:</label>
					<input name="roleName" class="easyui-validatebox" required="true">
				</div>
				<div class="fitem">
					<label>描述:</label>
					<textarea name="roleDesc" class="easyui-validatebox" style="width: 250px; height: 100px;"></textarea>
				</div>
			</form>
		</div>
		<div id="addDlgBtns">
			<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="doAddRole()">保存</a>
			<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#addDlg').dialog('close')">取消</a>
		</div>
		
		<%-- 编辑 --%>
		<div id="editDlg" class="easyui-dialog" style="width:400px;height:300px;padding:10px 20px"
			closed="true" buttons="#editDlgBtns" title="编辑角色" modal="true" resizable="true">
			<div class="ftitle">角色信息</div>
			<form id="editForm" method="post" data-options="url:'<%=basePath%>/role/editRole'">
				<div class="fitem">
					<label>角色Id:</label>
					<input name="roleId" class="easyui-validatebox" readonly="readonly">
				</div>
				<div class="fitem">
					<label>角色名:</label>
					<input name="roleName" class="easyui-validatebox" required="true">
				</div>
				<div class="fitem">
					<label>描述:</label>
					<textarea name="roleDesc" class="easyui-validatebox" style="width: 250px; height: 100px;"></textarea>
				</div>
			</form>
		</div>
		<div id="editDlgBtns">
			<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="doEditRole()">保存</a>
			<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#editDlg').dialog('close')">取消</a>
		</div>
		
		<%-- 分配角色权限 --%>
		<div id="authDlg" class="easyui-dialog" style="width:400px;height:300px;padding:10px 20px"
			closed="true" buttons="#authDlgBtns" title="分配权限" modal="true" resizable="true">
			<input id="auth_roleId" name="roleId" type="hidden" >
			<ul id="authTree" class="ztree"></ul>
		</div>
		<div id="authDlgBtns">
			<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="doUpdateRoleAuths()">保存</a>
			<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#authDlg').dialog('close')">取消</a>
		</div>
	</body>
</html>