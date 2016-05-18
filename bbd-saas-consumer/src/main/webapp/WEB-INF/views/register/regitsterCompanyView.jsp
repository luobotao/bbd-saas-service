<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="../main.jsp"%>
<html>
<head>
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
	<!-- S 注册成功 配送公司 -->
	<div class="n-site-c">
				<span class="n-result-tip">
					<c:if test="${postcompany.sta==0}">
						<img src="<c:url value="/resources/images/n_success.png" />" />
						<i>您的棒棒达快递账号申请信息提交成功。我们将在1-3个工作日内完成审核</i>
					</c:if>
					<c:if test="${postcompany.sta==1}">
						<img src="<c:url value="/resources/images/n_success.png" />" />
						<i>您的棒棒达快递账号已审核通过</i>
					</c:if>
					<c:if test="${postcompany.sta==2}">
						<img src="<c:url value="/resources/images/n_warning.png" />" />
						<i>您的棒棒达快递账号已审核通过</i>
					</c:if>

				</span>
		<div class="n-result-info">
			<div class="txt-input pb30">
				<i>账号类型：</i>配送公司
			</div>
			<div class="txt-input pb30">
				<i>手机号：</i>${user.loginName}
			</div>
			<div class="txt-input pb30">
				<i>公司名称：</i>${postcompany.companyname}
			</div>
			<div class="txt-input pb30 clearfix">
				<i class="fl">营业执照：</i><img style="width: 200px;height: 200px;" alt="" src="${ossUrl}${postcompany.licensePic}@200w"/>
			</div>
			<div class="txt-input pb30">
				<i>所在地：</i>${postcompany.province}-${postcompany.city}-${postcompany.area} ${postcompany.address}
			</div>
			<div class="txt-input pb30">
				<i>负责人姓名：</i>${user.realName}
			</div>
			<div class="txt-input pb30">
				<i>联系邮箱：</i>${user.email}
			</div>
			<span class="ser-btn l mli-wid" onclick="window.location.href='<c:url value="/login"/>'">返回首页</span>
		</div>
	</div>
	<!-- E 注册成功 配送公司-->
</div>
<!-- E content-->

<!-- S footer -->
<footer class="container tc">
	<em class="b-copy">京ICP备 465789765 号 版权所有 &copy; 2016-2020 棒棒达       北京棒棒达科技有限公司</em>
</footer>
<!-- E footer -->
</body>
</html>
