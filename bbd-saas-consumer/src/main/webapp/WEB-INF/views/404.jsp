<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <!-- <meta http-equiv="X-UA-Compatible" content="IE=edge"> -->
    <meta name="keywords" content="棒棒哒-左侧菜单"/>
    <meta name="description" content="棒棒哒-左侧菜单"/>
    <title>左侧菜单</title>
</head>
<body>
<script>
    alert("访问页面不存在，请与管理员联系！");
    window.location.href = "<c:url value="/login" />"
</script>
</body>
</html>