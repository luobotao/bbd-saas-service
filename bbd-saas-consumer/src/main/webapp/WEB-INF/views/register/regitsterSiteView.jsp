<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.bbd.saas.enums.SiteStatus" %>
<%@ page import="com.bbd.saas.enums.SiteTurnDownReasson" %>
<html>
<head>
	<jsp:include page="../main.jsp" flush="true" />
</head>
<body >

<!-- S nav -->
<nav class="navbar navbar-default b-navbar">
	<div class="container">
		<!-- Brand and toggle get grouped for better mobile display -->
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
				<span class="sr-only">Toggle navigation</span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="#"><img src="<c:url value="/resources/images/logo.png" />" alt="logo" /></a>
		</div>

		<!-- Collect the nav links, forms, and other content for toggling -->
		<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
			<i class="nav navbar-nav navbar-right re-navbar f16">已有账号？<a href="<c:url value="/login"/>"  class="orange n-login">马上登录</a></i>
		</div><!-- /.navbar-collapse -->
	</div><!-- /.container-fluid -->
</nav>
<!-- S content-->
<div class="container n-re-con">
	<!-- S 注册结果 -->
	<!-- S 注册成功 站点 -->
	<div class="n-site-c">
		<span class="n-result-tip">

			<c:if test="${site.status!=SiteStatus.TURNDOWN}">
				<img src="<c:url value="/resources/images/n_success.png" />" />
			</c:if>
			<c:if test="${site.status==SiteStatus.TURNDOWN}">
				<img src="<c:url value="/resources/images/n_warning.png" />" />
			</c:if>
			<i>
				<em>${site.memo}</em>
				<c:if test="${site.status==SiteStatus.TURNDOWN}">
					<c:if test="${site.turnDownReasson==SiteTurnDownReasson.OTHER}">

						<em class="red">${site.otherMessage}</em>
					</c:if>
					<c:if test="${site.turnDownReasson!=SiteTurnDownReasson.OTHER}">
						<em class="red">${site.turnDownReasson.message}</em>
					</c:if>
				</c:if>
				</i>
		</span>



		<div class="n-suc-info">
			<div class="txt-input pb30">
				<i>账号类型：</i>站点
			</div>
			<div class="txt-input pb30">
				<i>手机号：</i>${site.username}
			</div>
			<div class="txt-input pb30">
				<i>站点名称：</i>${site.name}
			</div>
			<div class="txt-input pb30">
				<i>所属公司：</i>${site.companyName}
			</div>
			<div class="txt-input pb30">
				<i>所在地：</i>${site.province}-${site.city}-${site.area} ${site.address}
			</div>
			<div class="txt-input pb30">
				<i>负责人姓名：</i>${site.responser}
			</div>
			<div class="txt-input pb30">
				<i>联系邮箱：</i>${site.email}
			</div>
			<span class="ser-btn l mli-wid" onclick="window.location.href='<c:url value="/login"/>'">返回首页</span>
		</div>
	</div>
	<!-- E 注册成功 站点-->
</div>
<!-- E content-->

<!-- S footer -->
<footer class="container tc">
	<em class="b-copy">京ICP备 465789765 号 版权所有 &copy; 2016-2020 棒棒达       北京棒棒达科技有限公司</em>
</footer>
<!-- E footer -->
<!-- Content Header (Page header) -->
</body>
</html>
