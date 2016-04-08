<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<!-- <meta http-equiv="X-UA-Compatible" content="IE=edge"> -->
	<meta name="keywords" content="棒棒哒-头部" />
	<meta name="description" content="棒棒哒-头部" />
	<title>头部</title>	
</head>
<body>
<div style="border-bottom:solid 2px gray; height:30px">
	<span>棒棒达配送系统</span>
	<span style="float:right;">
		<span><a href="<c:url value="/" />"  target="_parent">首页</a></span>
		<span>${username}</span> 
		<span><a href="<c:url value="/logout" />"  target="_parent">退出</a></span>
	</span> 
</div>  
</body>
</html>