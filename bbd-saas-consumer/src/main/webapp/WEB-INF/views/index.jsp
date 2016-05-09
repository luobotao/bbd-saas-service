<%@ page import="com.bbd.saas.enums.UserRole" %>
<%@ page import="com.bbd.saas.mongoModels.User" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>棒棒达快递</title>
	<link href="<c:url value="/resources/bootstrap/css/bootstrap.min.css" />" rel="stylesheet"  type="text/css" />
	<link href="<c:url value="/resources/stylesheets/main.css" />" rel="stylesheet"  type="text/css" /><!--自定义css-->
	<script src="<c:url value="/resources/adminLTE/plugins/jQuery/jQuery-2.1.3.min.js" />"> </script>
	<script src="<c:url value="/resources/bootstrap/js/bootstrap.min.js" />" type="text/javascript"></script>
	<script src="<c:url value="/resources/javascripts/main.js" />"> </script>
</head>
<body id="psrE">
<!-- S nav -->
<nav class="navbar navbar-default b-navbar">
	<div class="container-fluid">
		<!-- Brand and toggle get grouped for better mobile display -->
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
				<span class="sr-only">Toggle navigation</span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="<c:url value="/" />"><img src="<c:url value="/resources/images/logo.png" />" alt="logo" /></a>
		</div>

		<!-- Collect the nav links, forms, and other content for toggling -->
		<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
			<ul class="nav navbar-nav navbar-right f16">
				<li><a href="<c:url value="/home" />">首页</a></li>
				<li><a href="javascript:void(0);"><i class="p-icon p-user"></i>　<em class="orange">${user.realName}</em></a></li>
				<li><a href="<c:url value="/logout" />">退出登录</a></li>
			</ul>
		</div><!-- /.navbar-collapse -->
	</div><!-- /.container-fluid -->
</nav>
<!-- E nav -->
<!-- S content -->
<div class="clearfix b-branch">
	<div class="container-fluid">
		<div class="row">
			<!-- S sidebar -->
			<div class="col-xs-12 col-sm-12 bbd-md-3">
				<ul class="b-sidebar">
					<%
						User user = (User) request.getAttribute("user");
						if (user!=null && UserRole.COMPANY.equals(user.getRole())) {
					%>
					<li class="lv1"><a href="#"><i class="b-icon p-set"></i>数据查询</a></li>
					<ul class="menu dn">
						<li><a href="<c:url value="/mailQuery" />" target="iframe1">运单查询</a></li>
						<li><a href="<c:url value="/mailMonitor" />" target="iframe1">运单监控</a></li>
					</ul>
					<li id="query" class="lv1"><a href="<c:url value="/dataQuery" />" target="iframe1" ><i class="b-icon p-query"></i>运力分布</a></li>
					<li id="set" class="lv1"><a href="<c:url value="/deliverRegion/map/1" />" target="iframe1"><i class="b-icon p-dis"></i>配送区域</a></li>
					<li class="lv1"><a href="#"><i class="b-icon p-set"></i>系统设置</a></li>
					<ul class="menu dn">
						<li id="siteManage"><a href="<c:url value="/system/siteManage" />" target="iframe1">站点管理</a></li>
						<li id="userForComp"><a href="<c:url value="/userManage" />" target="iframe1">用户管理</a></li>
					</ul>
					<%
					} else {
					%>
					<li id="arrive" class="lv1"><a href="<c:url value="/packageToSite" />" target="iframe1" ><i class="b-icon p-package"></i>包裹到站</a></li>
					<li id="asign" class="lv1"><a href="<c:url value="/packageDispatch" />" target="iframe1" ><i class="b-icon p-aign"></i>运单分派</a></li>
					<li id="error" class="lv1"><a href="<c:url value="/handleAbnormal" />" target="iframe1" ><i class="b-icon p-error"></i>异常件处理</a></li>
					<li id="query" class="lv1"><a href="<c:url value="/dataQuery" />" target="iframe1" ><i class="b-icon p-query"></i>数据查询</a></li>
					<li id="set" class="lv1"><a href="<c:url value="/deliverRegion/map/1" />" target="iframe1"><i class="b-icon p-dis"></i>配送区域</a></li>
					<li class="lv1"><a href="#"><i class="b-icon p-set"></i>系统设置</a></li>
					<ul class="menu dn">
						<li id="user"><a href="<c:url value="/userManage" />" target="iframe1">用户管理</a></li>
					</ul>
					<%
						}
					%>


				</ul>
			</div>
			<!-- E sidebar -->

		</div>
	</div>
</div>
<!-- E content -->
<!-- S detail -->
<div class="b-branch-hei">
	<iframe id="iframe1" class="i-hei" name="iframe1" src="" frameborder="0" marginheight="0" marginwidth="0" scrolling="no" width="100%" height="100%" onLoad="iFrameHeight();"></iframe>

</div><!-- /.content-wrapper -->
<!-- E detail -->

<script>

	$(".menu li").click(function(){
		$(this).addClass("curr").siblings().removeClass("curr");
	});
	var typ="${typ}";
	if(typ!=null && typ!=""){
		$("li").each(function(){
			if($(this).attr("id")==typ){
				$(this).addClass("side-cur").siblings().removeClass("side-cur");
				if ($(this).parents('ul.menu').css("display") == "block") {//menu有dn
					$(this).parents('ul.menu').slideUp();
				} else {//menu没有dn
					$(this).parents('ul.menu').prev().addClass("side-cur");
					$(this).parents('ul.menu').slideDown();
				}

				var href = $(this).find("a").attr("href");
				$("#iframe1").attr("src",href);
			}
		})
	}else{
		$("#arrive").addClass("side-cur").siblings().removeClass("side-cur");
		var href =$("#arrive").find("a").attr("href");
		$("#iframe1").attr("src",href);
	}

</script>

</body>
</html>
