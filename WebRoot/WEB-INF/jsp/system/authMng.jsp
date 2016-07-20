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
				initAuthTree();
			});
		
			function initAuthTree(){
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
					view: {
						addHoverDom: addHoverDom,
						removeHoverDom: removeHoverDom
					}
				};
				
				$.ajax({
					url:'<%=basePath%>/auth/getAuths',
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
				for(var i=0,len=data.length; i<len; i++){
					if(data[i].authType >= 3){
						data[i].isParent = false;
					}else{
						data[i].isParent = true;
						data[i].open = true;
					}
				}
				return data;
			}
			
			function addHoverDom(treeId, treeNode){
				var aObj = $("#" + treeNode.tId + "_a");
				
				if ($("#addBtn_"+treeNode.tId).length==0 && treeNode.authType < 3){
					var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
						+ "' title='新增权限' onfocus='this.blur();'></span>";
						
					aObj.append(addStr);
					
					$("#addBtn_" + treeNode.tId).bind("click", treeNode, showAddAuth);
				}
				
				if ($("#editBtn_"+treeNode.tId).length==0 && treeNode.authType > 0){
					var editStr = "<span class='button edit' id='editBtn_" + treeNode.tId
						+ "' title='编辑权限' onfocus='this.blur();'></span>";
						
					aObj.append(editStr);
					
					$("#editBtn_" + treeNode.tId).bind("click", treeNode, showEditAuth);
				}
				
				if ($("#removeBtn_"+treeNode.tId).length==0 && treeNode.authType > 0 && !treeNode.children){
					var removeStr = "<span class='button remove' id='removeBtn_" + treeNode.tId
						+ "' title='删除权限' onfocus='this.blur();'></span>";
						
					aObj.append(removeStr);
					
					$("#removeBtn_" + treeNode.tId).bind("click", treeNode, doRemoveAuth);
				}
			}
			
			function removeHoverDom(treeId, treeNode){
				$("#addBtn_"+treeNode.tId).unbind().remove();
				$("#editBtn_"+treeNode.tId).unbind().remove();
				$("#removeBtn_"+treeNode.tId).unbind().remove();
			}
			
			function showAddAuth(event){
				var treeNode = event.data;
				
				$("#addDlg").dialog("open");
				$("#addForm").form("reset");
				
				$("#add_authParentName").val(treeNode.authName);
				$("#add_authParent").val(treeNode.authId);
				
				//非添加2级权限,页面地址隐藏
				if(treeNode.authType == 1){
					$("#add_authEntry").validatebox("enableValidation");
					$("#add_authEntry").parent().show();
				}else{
					$("#add_authEntry").validatebox("disableValidation");
					$("#add_authEntry").parent().hide();
				}
				
				//预设排序
				var maxOrder = 0;
				if(treeNode.children){
					for(var i=0,len=treeNode.children.length; i<len; i++){
						if(maxOrder < treeNode.children[i].authOrder){
							maxOrder = treeNode.children[i].authOrder;
						}
					}
				}
				
				maxOrder += 100;
				
				$("#add_authOrder").val(maxOrder);
				
				$("#addForm").form('validate');
			}
			
			function showEditAuth(event){
				var treeNode = event.data;
				
				$("#editDlg").dialog("open");
				$("#editForm").form("reset");
				
				$('#editForm').form('load',treeNode);
				
				//编辑2级权限,页面地址显示
				if(treeNode.authType == 2){
					$("#edit_authEntry").validatebox("enableValidation");
					$("#edit_authEntry").parent().show();
				}else{
					$("#edit_authEntry").validatebox("disableValidation");
					$("#edit_authEntry").parent().hide();
				}
				
				$("#editForm").form('validate');
			}
			
			function doAddAuth(){
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
							initAuthTree();
						}
					}
				});
			}
			
			function doEditAuth(){
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
							initAuthTree();
						}
					}
				});
			}
			
			function doRemoveAuth(event){
				var treeNode = event.data;
				
				$.messager.confirm('确认','确定删除该权限?',function(r){
					if (r){
						$.ajax({
							url:'<%=basePath%>/auth/delAuth',
							data:{authId:treeNode.authId},
							type:'post',
							dataType:'json',
							success:function(result){
								if (result.errorMsg){
									$.messager.alert('Error',result.errorMsg);
								} else {
									initAuthTree();
								}
							}
						});
					}
				});
			}
		
		</script>
	</head>
	
	<body >
		<ul id="authTree" class="ztree"></ul>
		
		<%-- 新增 --%>
		<div id="addDlg" class="easyui-dialog" style="width:400px;height:300px;padding:10px 20px"
			closed="true" buttons="#addDlgBtns" title="新增权限" modal="true" resizable="true">
			<div class="ftitle">权限信息</div>
			<form id="addForm" method="post" data-options="url:'<%=basePath%>/auth/addAuth'">
				<div class="fitem">
					<label>父节点:</label>
					<input id="add_authParentName" name="authParentName" class="easyui-validatebox" required="true" readonly="readonly">
					<input id="add_authParent" name="authParent" type="hidden" class="easyui-validatebox" required="true" readonly="readonly">
				</div>
				<div class="fitem">
					<label>权限ID:</label>
					<input name="authId" class="easyui-validatebox" required="true" validType="remote['<%=basePath%>/auth/checkAuthIdExist','authId']" invalidMessage="该ID已存在">
				</div>
				<div class="fitem">
					<label>权限名称:</label>
					<input name="authName" class="easyui-validatebox" required="true">
				</div>
				<div class="fitem">
					<label>页面地址:</label>
					<input id="add_authEntry" name="authEntry" class="easyui-validatebox" required="true">
				</div>
				<div class="fitem">
					<label>排序:</label>
					<input id="add_authOrder" name="authOrder" class="easyui-validatebox" required="true">
				</div>
			</form>
		</div>
		<div id="addDlgBtns">
			<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="doAddAuth()">保存</a>
			<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#addDlg').dialog('close')">取消</a>
		</div>
		
		<%-- 编辑 --%>
		<div id="editDlg" class="easyui-dialog" style="width:400px;height:300px;padding:10px 20px"
			closed="true" buttons="#editDlgBtns" title="编辑权限" modal="true" resizable="true">
			<div class="ftitle">权限信息</div>
			<form id="editForm" method="post" data-options="url:'<%=basePath%>/auth/editAuth'">
				<div class="fitem">
					<label>权限ID:</label>
					<input name="authId" class="easyui-validatebox" required="true" readonly="readonly">
				</div>
				<div class="fitem">
					<label>权限名称:</label>
					<input name="authName" class="easyui-validatebox" required="true">
				</div>
				<div class="fitem">
					<label>页面地址:</label>
					<input id="edit_authEntry" name="authEntry" class="easyui-validatebox" required="true">
				</div>
				<div class="fitem">
					<label>排序:</label>
					<input id="edit_authOrder" name="authOrder" class="easyui-validatebox" required="true">
				</div>
			</form>
		</div>
		<div id="editDlgBtns">
			<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="doEditAuth()">保存</a>
			<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#editDlg').dialog('close')">取消</a>
		</div>
	</body>
</html>