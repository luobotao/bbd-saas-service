<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<html>
<head>
	<title>注册页面</title>
	<jsp:include page="../main.jsp" flush="true" />
</head>
<body class="fbg">
	<input type="button" onclick="register(0);" value="配送公司">
	<input type="button" onclick="register(1);" value="站点">

<script>
function register(type) {
	if(type==0){
		window.location.href="<c:url value="/register/registerCompany?phone=${phone}" />"
	}else{
		window.location.href="<c:url value="/register/registerSite?phone=${phone}" />"
	}
}
</script>
</body>
</html>