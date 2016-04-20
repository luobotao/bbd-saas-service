<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
<<<<<<< HEAD
	<title>棒棒达快递</title>
=======
	<title>配送系统首页  - 棒棒达快递</title>
>>>>>>> 4d30ff264c57e930fec4345512c53ad7c248dadd
	<link href="<c:url value="/resources/bootstrap/css/bootstrap.min.css" />" rel="stylesheet"  type="text/css" />
	<link href="<c:url value="/resources/stylesheets/main.css" />" rel="stylesheet"  type="text/css" /><!--自定义css-->
	<script src="<c:url value="/resources/adminLTE/plugins/jQuery/jQuery-2.1.3.min.js" />"> </script>
	<script src="<c:url value="/resources/bootstrap/js/bootstrap.min.js" />" type="text/javascript"></script>
</head>
<body>
<!-- S nav -->
<nav class="navbar navbar-default b-navbar">
<<<<<<< HEAD
	<div class="container-fluid">
=======
	<div class="container">
>>>>>>> 4d30ff264c57e930fec4345512c53ad7c248dadd
		<!-- Brand and toggle get grouped for better mobile display -->
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
				<span class="sr-only">Toggle navigation</span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
<<<<<<< HEAD
			<a class="navbar-brand" href="<c:url value="/" />"><img src="<c:url value="/resources/images/logo.png" />" alt="logo" /></a>
=======
			<a class="navbar-brand" href="#"><img src="static/images/logo.png" alt="logo" /></a>
>>>>>>> 4d30ff264c57e930fec4345512c53ad7c248dadd
		</div>

		<!-- Collect the nav links, forms, and other content for toggling -->
		<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
			<ul class="nav navbar-nav navbar-right f16">
				<li><a href="<c:url value="/" />">首页</a></li>
				<li><a href="javascript:void(0);"><i class="glyphicon glyphicon-user orange">${user.realName}</i></a></li>
				<li><a href="<c:url value="/logout" />">退出登录</a></li>
			</ul>
		</div><!-- /.navbar-collapse -->
	</div><!-- /.container-fluid -->
</nav>
<!-- E nav -->
<!-- S content -->
<div class="clearfix">
	<div class="b-con">
<<<<<<< HEAD
		<span class="cloud"><img src="<c:url value="/resources/images/cloud.png" />" /></span>
		<div class="container">
			<ul class="row">
				<li class="b-status col-xs-12 col-sm-6 col-md-4 col-lg-4">
					<a href="<c:url value="/?typ=arrive" />">
						<div class="b-status-card">
							<span><img src="<c:url value="/resources/images/arrive.png" />" alt="包裹到站" /></span>
=======
		<span class="cloud"><img src="static/images/cloud.png" /></span>
		<div class="container">
			<ul class="row">
				<li class="b-status col-xs-12 col-sm-6 col-md-4 col-lg-4">
					<a href="<c:url value="/packageToSite" />">
						<div class="b-status-card">
							<span><img src="static/images/arrive.png" alt="包裹到站" /></span>
>>>>>>> 4d30ff264c57e930fec4345512c53ad7c248dadd
							<h3>包裹到站</h3>
						</div>
					</a>

				</li>
				<li class="b-status col-xs-12 col-sm-6 col-md-4 col-lg-4">
<<<<<<< HEAD
					<a href="<c:url value="/?typ=asign" />">
						<div class="b-status-card">
							<span><img src="<c:url value="/resources/images/asign.png" />" alt="运单分派" /></span>
=======
					<a href="<c:url value="/packageDispatch" />">
						<div class="b-status-card">
							<span><img src="static/images/asign.png" alt="运单分派" /></span>
>>>>>>> 4d30ff264c57e930fec4345512c53ad7c248dadd
							<h3>运单分派</h3>
						</div>
					</a>
				</li>
				<li class="b-status col-xs-12 col-sm-6 col-md-4 col-lg-4">
<<<<<<< HEAD
					<a href="<c:url value="/?typ=error" />">
						<div class="b-status-card">
							<span><img src="<c:url value="/resources/images/error.png" />" alt="异常件处理" /></span>
=======
					<a href="<c:url value="/handleAbnormal" />">
						<div class="b-status-card">
							<span><img src="static/images/error.png" alt="异常件处理" /></span>
>>>>>>> 4d30ff264c57e930fec4345512c53ad7c248dadd
							<h3>异常件处理</h3>
						</div>
					</a>
				</li>
				<li class="b-status col-xs-12 col-sm-6 col-md-4 col-lg-4">
<<<<<<< HEAD
					<a href="<c:url value="/?typ=query" />">
						<div class="b-status-card">
							<span><img src="<c:url value="/resources/images/query.png" />" alt="数据查询" /></span>
=======
					<a href="<c:url value="/dataQuery" />">
						<div class="b-status-card">
							<span><img src="static/images/query.png" alt="数据查询" /></span>
>>>>>>> 4d30ff264c57e930fec4345512c53ad7c248dadd
							<h3>数据查询</h3>
						</div>
					</a>
				</li>
				<li class="b-status col-xs-12 col-sm-6 col-md-4 col-lg-4">
<<<<<<< HEAD
					<a href="<c:url value="/?typ=set" />">
						<div class="b-status-card">
							<span><img src="<c:url value="/resources/images/set.png" />" alt="系统设置" /></span>
=======
					<a href="<c:url value="/deliverRegion/map/1" />">
						<div class="b-status-card">
							<span><img src="static/images/set.png" alt="系统设置" /></span>
>>>>>>> 4d30ff264c57e930fec4345512c53ad7c248dadd
							<h3>系统设置</h3>
						</div>
					</a>
				</li>
			</ul>
		</div>

	</div>
	<div class="b-con-bot"></div>
</div>
<!-- E content -->
<!-- S footer -->
<footer class="container tc">
	<em class="b-copy">京ICP备 465789765 号 版权所有 &copy; 2016-2020 棒棒达       北京棒棒达科技有限公司</em>
</footer>
<!-- E footer -->
<<<<<<< HEAD
=======
<script src="<c:url value="/resources/javascripts/main.js" />"> </script>
>>>>>>> 4d30ff264c57e930fec4345512c53ad7c248dadd
</body>
</html>
