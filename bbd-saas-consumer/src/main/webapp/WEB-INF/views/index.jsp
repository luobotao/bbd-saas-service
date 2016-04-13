<%@ page import="com.bbd.saas.utils.Dates" %>
<%@ page import="java.util.Date" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>棒棒达后台管理系统</title>
	<meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>
	<!-- Bootstrap 3.3.2 -->
	<link href="<c:url value="/resources/bootstrap/css/bootstrap.min.css" />" rel="stylesheet"  type="text/css" />
	<!-- FontAwesome 4.3.0 -->
	<link href="<c:url value="/resources/adminLTE/css/font-awesome.min.css" />" rel="stylesheet"  type="text/css" />
	<!-- Ionicons 2.0.0 -->
	<link href="<c:url value="/resources/adminLTE/css/ionicons.min.css" />" rel="stylesheet"  type="text/css" />
	<!-- Theme style -->
	<link href="<c:url value="/resources/adminLTE/css/AdminLTE.css" />" rel="stylesheet"  type="text/css" />
	<!-- AdminLTE Skins. Choose a skin from the css/skins folder instead of downloading all of them to reduce the load. -->
	<link href="<c:url value="/resources/adminLTE/css/skins/_all-skins.css" />" rel="stylesheet"  type="text/css" />
	<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
	<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
	<!--[if lt IE 9]>
	<script src="<c:url value="/resources/adminLTE/plugins/src/html5shiv.js" />"> </script>
	<script src="<c:url value="/resources/adminLTE/plugins/src/respond.min.js" />"> </script>
	<![endif]-->
	<style type="text/css">
		.nav-user {
			float:right ;
			margin: 0;
		}
		.nav-user li {
			float:left ;
			list-style:none;
			width:auto;
			padding-left:25px;
			padding-right:25px;
			text-align:right ;
		}
		.nav-user li a {
			line-height:48px ;
			font-size:15px ;
		}
		.nav-user li a:hover,.nav-user li a.active {
			color:#47d4fd ;
			font-weight:bold ;
		}
		.subnav a.subnav-search{
			background-position:20px -369px;
		}
		.subnav a.subnav-rmb{
			background-position:20px -436px;
		}
		.subnav a.subnav-dollar{
			background-position:20px -502px;
		}

		.ico-user {
			width:20px ;
			height:20px ;
			display:inline-block ;
			background:url(<c:url value="/resources/images/admin/ico-1.png" />) no-repeat left -195px ;
			vertical-align:middle ;
		}
		.subnav{
			margin-top:40px;
			float: right;
			width:170px;
		}
		.subnav a{
			display:block;
			font-size:16px;
			line-height:60px;
			color:#464e62;
			background:url(<c:url value="/resources/images/admin/ico-1.png" />) no-repeat 20px -238px;
			padding-left:60px;
			font-weight:bold;
			margin-bottom:10px;
			border-radius: 6px 0 0 6px;
		}
		.subnav a.subnav-query{
			background-position:20px -305px;
		}
		.subnav a:hover,.subnav a.active{
			background-color:#dcdde5;
		}
	</style>
</head>
<body class="skin-blue">
<div class="wrapper">

	<header class="main-header">
		<!-- Logo -->
		<a href="/admin" class="logo"><img src="<c:url value="/resources/images/admin/logo.png" />"/></a>
		<!-- Header Navbar: style can be found in header.less -->
		<nav class="navbar navbar-static-top" role="navigation">
			<!-- Sidebar toggle button-->
			<a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button"  title="展示\收起侧边栏">
				<span class="sr-only">展示\收起侧边栏</span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</a>
			<div class="navbar-custom-menu">
				<ul class="nav-user">
					<li>
						<a href=" javascript:;" class="active"><i class="ico-user"></i>${user.realName}</a>
					</li>
					<li>
						<a href="/logout">退出登录</a>
					</li>
				</ul>
			</div>
		</nav>
	</header>
	<!-- Left side column. contains the logo and sidebar -->
	<aside class="main-sidebar">
		<!-- sidebar: style can be found in sidebar.less -->
		<section class="sidebar">
		<div class="user-panel">
			<div class="pull-left info">
				<p>您好，${user.realName}</p>
				今天<%=Dates.formatDate(new Date())%>
			</div>
		</div>
		<ul class="sidebar-menu">
			<li class="treeview">
				<a href="<c:url value="/packageToSite" />" target="mainFrame" class="subnav-city active">包裹到站</a>
			</li>
			<li class="treeview">
				<a href="<c:url value="/packageDispatch" />" target="mainFrame" class="subnav-search  ">包裹分派</a>
			</li>
			<li class="treeview">
				<a href="<c:url value="/handleAbnormal" />" target="mainFrame" class="subnav-dollar">异常件处理</a>
			</li>
			<li class="treeview">
				<a href="<c:url value="/dataQuery" />" target="mainFrame" class="subnav-rmb  ">数据查询</a>
			</li>
			<%--<li class="treeview">
				<a href="#">
					<i class="fa fa-dashboard"></i> <span>系统设置</span> <i class="fa fa-angle-left pull-right"></i>
				</a>
				<ul class="treeview-menu">--%>
					<li ><a href="<c:url value="/deliverRegion/map/1" />" target="mainFrame" class="subnav-query">配送区域</a></li>
					<li ><a href="<c:url value="/userManage/userList" />" target="mainFrame" class="subnav-query">用户管理</a></li>
			<%--</ul>
        </li>--%>
		</ul>
	</section>
		<!-- /.sidebar -->
	</aside>

	<!-- Content Wrapper. Contains page content -->
	<div class="content-wrapper" height="100%">
		<iframe id="mainFrame" name="mainFrame" src="<c:url value="/packageToSite" />" width="100%" height="100%" frameborder="0"></iframe>
	</div><!-- /.content-wrapper -->
	<footer class="main-footer">
		<div class="pull-right hidden-xs">
			<b>Version</b> 1.0
		</div>
		<strong>Copyright &copy; 2016 <a href="#">bbt</a>.</strong> All rights reserved.
	</footer>
</div><!-- ./wrapper -->



<!-- jQuery 2.1.3 -->
<script src="<c:url value="/resources/adminLTE/plugins/jQuery/jQuery-2.1.3.min.js" />"> </script>
<!-- jQuery UI 1.11.2 -->
<script src="<c:url value="/resources/adminLTE/plugins/src/jquery-ui.min.js" />"> </script>
<!-- Resolve conflict in jQuery UI tooltip with Bootstrap tooltip -->
<script>
	$.widget.bridge('uibutton', $.ui.button);

	/*function iFrameHeight() {
	 var ifm = document.getElementById("mainFrame");
	 var subWeb = document.frames ? document.frames["iframepage"].document
	 : ifm.contentDocument;
	 if (ifm != null && subWeb != null) {
	 ifm.height = subWeb.body.scrollHeight;
	 }
	 }*/
	var winH = $(window).height() - 110;
	$('#mainFrame').attr('height',winH);
	$("li").click(function(){
		$(this).addClass("active").siblings().removeClass("active");
	});
</script>
<!-- Bootstrap 3.3.2 JS -->
<script src="<c:url value="/resources/bootstrap/js/bootstrap.min.js" />" type="text/javascript"></script>
<script src="<c:url value="/resources/adminLTE/plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.all.min.js" />"> </script>
<!-- AdminLTE App -->
<script src="<c:url value="/resources/adminLTE/js/app.min.js" />"> </script>
<script>
$('.subnav a').on('click',function(){
	$(this).addClass('active').siblings().removeClass('active');
})
</script>
</body>
</html>
