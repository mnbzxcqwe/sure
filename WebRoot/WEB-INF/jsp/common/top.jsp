<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<%-- <base href="<%=basePath%>"> --%>

<meta charset="utf-8" />

<script type="text/javascript" src="<%=basePath%>/resources/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/resources/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/resources/easyui/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=basePath%>/resources/js/common.js"></script>
	
<link rel="stylesheet" type="text/css" href="<%=basePath%>/resources/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css" href="<%=basePath%>/resources/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=basePath%>/resources/css/common.css">