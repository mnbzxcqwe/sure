<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<!-- jsp文件头和头部 -->
		<%@ include file="../common/top.jsp"%>
	</head>
	
	<body >
	    <form id="login" method="post" action="login_login">
		    <div>
				<label for="name">账号:</label>
				<input class="easyui-validatebox" type="text" name="name" data-options="required:true" />
		    </div>
		    <div>
				<label for="pwd">密码:</label>
				<input class="easyui-validatebox" type="text" name="pwd" data-options="required:true" />
		    </div>
		    <button type="submit">登录</button>
		</form>
		
		<script type="text/javascript">
		 	$(function(){
		 		$('#login').form({
		 			dataType:'json',
		 			success:function(data){
		 				var data = eval('(' + data + ')');
		 				if(data.result == 'success'){
		 					window.location.href="index";
		 				}else{
			 				$.messager.alert('Info', data.result, 'info');
		 				}
		 			}
		 		});
		    })
		</script>
	</body>
</html>