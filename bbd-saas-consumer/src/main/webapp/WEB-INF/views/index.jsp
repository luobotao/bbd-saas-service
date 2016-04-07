<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<!-- 兼容360浏览器 -->
	<!-- <meta http-equiv="X-UA-Compatible" content="IE=edge"> -->
	<meta name="keywords" content="棒棒哒-首页" />
	<meta name="description" content="棒棒哒-首页" />
	<title>首页</title>
	<link href="<c:url value="/resources/form.css" />" rel="stylesheet"  type="text/css" />		
	<style>
	   .page span {padding:50px 50px; width:110px; background-color:#ffcc00;  }	    
	</style>
</head>
<body width="100%">
<div style="border-bottom:solid 2px gray; height:30px">
	<span>棒棒达配送系统</span>
	<span style="float:right;">
		<span>${username}</span> 
		<a href="<c:url value="/" />"  target="_parent">退出</a>
	</span> 
</div>
<div class="page" style="padding-top:100px;">
	<span><a href="<c:url value="/packageToSite" />">包裹到站</a></span>
	<span><a href="<c:url value="/packageDispatch" />">包裹分派</a></span>
	<span><a href="<c:url value="/handleAbnormal" />">异常件处理</a></span>
	<span><a href="<c:url value="/dataQuery" />">数据查询</a></span>
	<br><br><br><br><br><br><br><br><br><br>
	<span><a href="<c:url value="/deliverRegion/map" />">系统设置</a></span>		
</div>  
</body>
</html>