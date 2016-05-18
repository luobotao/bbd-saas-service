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
	<script src="<c:url value="/resources/bootstrap/js/jquery.form.js" />" type="text/javascript"></script>
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
				<li><a href="javascript:void(0);" class="j-pwd" onclick="document.getElementById('userForm').reset();"><i class="p-icon p-user"></i>　<em class="orange">${user.realName}</em></a></li>
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
					<li class="lv1"><a href="#"><i class="b-icon p-query"></i>数据查询</a></li>
					<ul class="menu dn">
						<li id="mailQuery"><a href="<c:url value="/mailQuery" />" target="iframe1">运单查询</a></li>
						<%--<li><a href="<c:url value="/mailMonitor" />" target="iframe1">运单监控</a></li>--%>
					</ul>
					<li id="capacityDistribution" class="lv1"><a href="<c:url value="/capacityDistribution" />" target="iframe1" ><i class="b-icon p-capacity"></i>运力分布</a></li>
					<li id="deliverArea" class="lv1"><a href="<c:url value="/deliverArea/map?activeNum=1" />" target="iframe1" ><i class="b-icon p-dis"></i>配送区域</a></li>
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
<!--S 修改密码-->
<div class="j-pwd-pop modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog b-modal-dialog middleS" role="document">
		<div class="modal-content">
			<div class="modal-header b-modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
				<h4 class="modal-title tc">修改密码</h4>
			</div>
			<div class="modal-body b-modal-body form-inline form-inline-n">
				<c:url var="actionUrl" value="/userManage/editPass?${_csrf.parameterName}=${_csrf.token}"/>
				<form role="form" action="${actionUrl}" method="post" id="userForm" class="form-inline form-inline-n">
					<ul class="txt-complex f16">
						<li class="pb20">
							<i>　用户名：</i> ${user.realName}
						</li>
						<li class="pb20">
							<i>原始密码：</i>
							<input type="password" class="form-control form-bod" id="passwordOld" name="passwordOld" placeholder="原始密码"/>
						</li>
						<li class="pb20">
							<i>　新密码：</i>
							<input type="password" class="form-control form-bod j-n-pwd" id="password" name="password" placeholder="新密码"  />
						</li>
						<li class="pb20">
							<i>确认密码：</i>
							<input type="password" class="form-control form-bod j-c-pwd" id="passwordC" name="passwordC" placeholder="确认密码"   />
						</li>
					</ul>
				</form>
				<div>

				</div>
				<div class="clearfix">
					<a href="javascript:void(0);" class="sbtn sbtn2 l col-md-12" id="saveUserBtn">保存</a>
				</div>

			</div>

		</div>
	</div>
</div>
<!--E 修改密码-->


<!--S 提示信息-->
<div class="b-prompt">
	<i class="b-prompt-txt">用户名不存在</i>
</div>
<!--E 提示信息-->
<!-- S alert -->
<div class="j-alert-pop modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog b-modal-dialog middleS" role="document">
		<div class="modal-content">
			<div class="modal-header b-modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
				<h4 class="modal-title tc j-c-tie">提示信息</h4>
			</div>
			<div class="modal-body b-modal-body">
				<em class="f16 j-alert-con">alert内容</em>
				<div class="clearfix mt20">
					<a href="javascript:void(0);" class="sbtn sbtn2 l col-md-12" data-dismiss="modal">确认</a>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- E alert -->

<script>
	$('.b-sidebar .menu li').click(function() {
		$(this).addClass('curr').siblings('li').removeClass('curr');
		$(this).addClass('side-cur').siblings('li').removeClass('side-cur');
	});
	var typ="${typ}";
	if(typ!=null && typ!=""){
		$("li").each(function(){
			if($(this).attr("id")==typ){
				$(this).addClass("curr").siblings().removeClass("curr");
				if ($(this).parents('ul.menu').css("display") == "block") {//menu有dn
					$(this).parents('ul.menu').slideUp();
				} else {//menu没有dn
					$(this).addClass("side-cur").siblings().removeClass("side-cur");
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
	$("#saveUserBtn").click(function(){

		var passwordOld = $.trim($('input[name="passwordOld"]').val());
		if(passwordOld==""){
			outDiv("请输入原始密码");
			return false;
		}
		var password = $.trim($('input[name="password"]').val());
		if(password==""){
			outDiv("请输入新密码");
			return false;
		}
		if(!pwdreg.test(password)){
			outDiv("请输入6-12位数字和字母结合的密码");
			return false;
		}
		var passwordC = $.trim($('input[name="passwordC"]').val());
		if(passwordC==""){
			outDiv("请输入确认密码");
			return false;
		}
		if(passwordC!=password){
			outDiv("新密码和确认密码不一致，请检查");
			return false;
		}
		$("#userForm").ajaxSubmit({
			dataType: 'json',
			success: function(response){
				if(response==true){
					$(".j-pwd-pop").modal("hide");
					outDiv("密码修改成功");
				}else{
					outDiv("原始密码不正确");
					return false;
				}

			},
			error: function(JsonHttpRequest, textStatus, errorThrown){
				alert_mine("错误","未知错误");
			}
		});
	})
</script>

</body>
</html>
