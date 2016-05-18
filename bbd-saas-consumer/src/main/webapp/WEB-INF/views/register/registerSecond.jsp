<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="../main.jsp"%>
<html>
<head>
	<title>注册页面</title>
</head>
<body>
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
<!-- E nav -->

<!-- S content-->
<div class="container n-re-con">
	<div class="n-progress-area">
		<div class="n-progress-outer">
			<div class="n-progress-inner n-progress-inner2">

			</div>
		</div>
		<div class="n-progress-txt">
			<span class="n-step n-fcur">注册账号</span>
			<span class="n-step n-fcur">选择类型</span>
			<span>填写资料</span>
		</div>
	</div>
	<!-- S 注册信息 -->
	<div class="n-form-infoA">
		<div class="n-form-info clearfix">
			<!-- S 第二步 -->
			<div class="n-form-con clearfix">
				<ul class="type-ul">
					<li>
						<div class="n-re-type">
							<img src="<c:url value="/resources/images/n_discompany.png" />" alt="配送公司" />
							<i>配送公司</i>
						</div>
						<a href="<c:url value="/register/registerCompany?phone=${phone}" />" class="ser-btn l w220 f18">申请开通</a>
					</li>
					<li>
						<div class="n-re-type">
							<img src="<c:url value="/resources/images/n_site.png" />" alt="站点" />
							<i>站点</i>
						</div>
						<a href="<c:url value="/register/registerSite?phone=${phone}" />" class="ser-btn l w220 f18">申请开通</a>
					</li>
					<li class="mr0">
						<div class="n-re-type">
							<img src="<c:url value="/resources/images/n_business.png" />" alt="电商商家" />
							<i>电商商家</i>
							<em class="n-forward">暂未开通，敬请期待</em>
						</div>
						<a href="javascript:void(0)" class="ser-btn b5 w220 f18">申请开通</a>
					</li>
				</ul>
			</div>
			<!-- E 第二步 -->
		</div>
	</div>
	<!-- E 注册信息 -->
</div>
<!-- E content-->

<!-- S footer -->
<footer class="container tc">
	<em class="b-copy">京ICP备 465789765 号 版权所有 &copy; 2016-2020 棒棒达       北京棒棒达科技有限公司</em>
</footer>
<!-- E footer -->

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