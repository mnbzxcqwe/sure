<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<!-- jsp文件头和头部 -->
		<%@ include file="top.jsp"%>
		
		<link rel="stylesheet" type="text/css" href="resources/css/index.css">
		
		<script type="text/javascript">
		    $(function(){
		    	$(window).trigger('resize');
		    });
		    
		    function addTab(title, href, icon) {
		        var $tabs = $('#tabs');
		        icon = icon || 'menu_icon_datadeal';
		        if ($tabs.tabs('exists', title)) {
		        	$tabs.tabs('select', title);
		        } else {
		            if (href) {
		                var content = '<iframe frameborder="0" src="' + handleHref(href) + '" style="border:0;width:100%;height:99.5%;"></iframe>';
		            } else {
		                var content = '';
		            }
		            $tabs.tabs('add', {
		                title : title,
		                content : content,
		                closable : true,
		                iconCls: icon
		            });
		        }
		    }
		    
		    function handleHref(href){
		    	if(href.indexOf("/") == 0){
		    		href = href.substring(1);
		    	}
		    	return href;
		    }
	    </script>
	</head>
	
	<body class="easyui-layout">
	    <div data-options="region:'north',title:'North Title',split:false" style="height:100px;">
	    	<a href="logout">1注销</a>
	    	<a href="permitted">鉴权</a>
	    	<a href="permitted1">鉴权1</a>
	    	<a href="clean">清缓存</a>
	    </div>
	    
	    <%--左侧菜单栏 --%>
	    <div data-options="region:'west',split:false" style="width:300px;">
	    	<div id="menu" class="easyui-accordion i_accordion_menu" style="border:0;width:100%;height:100%;">
	    		
				<c:forEach items="${authTree.children }" var="menu">
	    			<div title="${menu.authName }" style="overflow: auto;">
						<c:forEach items="${menu.children }" var="page">
							<div class="nav-item">
								<a href="javascript:addTab('${page.authName }','${page.authEntry }','menu_icon_datadeal')">
									<span class="menu_icon_datadeal"></span>
									<span>${page.authName }</span>
								</a>
							</div>
						</c:forEach>
					</div>
				</c:forEach>
	    		
	    	</div>
	    </div>
	    
	    <%--主页面区 --%>
	    <div data-options="region:'center'" style="overflow: hidden;">
	    	<div id="tabs" class="easyui-tabs" style="overflow: hidden;" data-options="border:false,fit:true">
                <div title="首页" data-options="border:false" style="overflow: hidden;">
                </div>
            </div>
	    </div>
	    
	    <div data-options="region:'south',split:false" style="height:100px;"></div>
	</body>
</html>