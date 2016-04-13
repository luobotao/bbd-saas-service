<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<!-- <meta http-equiv="X-UA-Compatible" content="IE=edge"> -->
	<meta name="keywords" content="棒棒哒-左侧菜单" />
	<meta name="description" content="棒棒哒-左侧菜单 />
	<title>左侧菜单</title>	
</head>
<body>
	<ul>
		<li><a href="<c:url value="/" />"  target="content-main">首页</a></li>
		<li><a href="<c:url value="/packageToSite" />"  target="content-main">包裹到站</a></li>
		<li><a href="<c:url value="/packageDispatch" />" target="content-main">包裹分派</a></li>
		<li><a href="<c:url value="/handleAbnormal" />" target="content-main">异常件处理</a></li>
		<li><a href="<c:url value="/dataQuery" />" target="content-main">数据查询</a></li>
		<li><a href="<c:url value="/deliverRegion/range/1" />" target="content-main">系统设置-配送范围</a></li>
		<li><a href="<c:url value="/userManage/listUser" />" target="content-main">系统设置-用户管理</a></li>
		<li><a href="<c:url value="/roleManage" />" target="content-main">系统设置-角色管理</a></li>
	</ul>
</body>
</html>