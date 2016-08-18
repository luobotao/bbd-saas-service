<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.bbd.saas.mongoModels.User" %>
<%@ page import="com.bbd.saas.enums.UserRole" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>配送系统首页  - 棒棒达快递</title>
	<meta name="renderer" content="webkit">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<link rel="shortcut icon" href="<c:url value="/resources/images/favicon.ico" />"/>
	<link rel="bookmark" href="<c:url value="/resources/images/favicon.ico" />"/>
	<link href="<c:url value="/resources/bootstrap/css/bootstrap.min.css" />" rel="stylesheet"  type="text/css" />
	<link href="<c:url value="/resources/stylesheets/main.css?_123" />" rel="stylesheet"  type="text/css" /><!--自定义css-->
	<script type="text/javascript" src="<c:url value="/resources/javascripts/jquery-1.12.4.min.js"/>"></script>

	<script src="<c:url value="/resources/bootstrap/js/bootstrap.min.js" />" type="text/javascript"></script>
	<script src="<c:url value="/resources/bootstrap/js/jquery.form.js" />" type="text/javascript"></script>
	<script src="<c:url value="/resources/javascripts/main.js" />"> </script>
	<script type="application/javascript">
		$(".j-guide-pop").show().addClass("in");
	</script>
	<!–[if IE]>
	<script>
		(function() {
			if (!
						/*@cc_on!@*/
							0) return;
			var e = "abbr, article, aside, audio, canvas, datalist, details, dialog, eventsource, figure, footer, header, hgroup, mark, menu, meter, nav, output, progress, section, time, video".split(', ');
			var i= e.length;
			while (i--){
				document.createElement(e[i])
			}
		})()
	</script>
	<![endif]–>
	<!-- 解决 ie8 不识别 mediaQuery 问题 -->
	<script src="<c:url value="/resources/javascripts/respond.js" />"> </script>
</head>
<body>

<!-- S alert -->
<div class="j-alert-pop modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" style="margin-left: -110px">
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
<% 
User user = (User)request.getAttribute("user");
%>
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
<div class="clearfix">
	<div class="b-con">
		<span class="cloud"><img src="<c:url value="/resources/images/cloud.png" />" /></span>
		<div class="container">
			<ul class="row">
				<% 
					if(user.getRole() == UserRole.SITEMASTER){
				%>
					<li class="b-status col-xs-12 col-sm-6 col-md-4 col-lg-4">
					<a href="<c:url value="/?typ=arrive" />">
						<div class="b-status-card">
							<span><img src="<c:url value="/resources/images/arrive.png" />" alt="包裹到站" /></span>
							<h3>包裹到站</h3>
						</div>
					</a>

					</li>
					<li class="b-status col-xs-12 col-sm-6 col-md-4 col-lg-4">
						<a href="<c:url value="/?typ=asign" />">
							<div class="b-status-card">
								<span><img src="<c:url value="/resources/images/asign.png" />" alt="运单分派" /></span>
								<h3>运单分派</h3>
							</div>
						</a>
					</li>
					<li class="b-status col-xs-12 col-sm-6 col-md-4 col-lg-4">
						<a href="<c:url value="/?typ=error" />">
							<div class="b-status-card">
								<span><img src="<c:url value="/resources/images/error.png" />" alt="异常件处理" /></span>
								<h3>异常件处理</h3>
							</div>
						</a>
					</li>
					<li class="b-status col-xs-12 col-sm-6 col-md-4 col-lg-4">
						<a href="<c:url value="/?typ=query" />">
							<div class="b-status-card">
								<span><img src="<c:url value="/resources/images/query.png" />" alt="数据查询" /></span>
								<h3>数据查询</h3>
							</div>
						</a>
					</li>
					<li class="b-status col-xs-12 col-sm-6 col-md-4 col-lg-4">
						<a href="<c:url value="/?typ=set" />">
							<div class="b-status-card">
								<span><img src="<c:url value="/resources/images/d_area.png" />" alt="配送区域" /></span>
								<h3>配送区域</h3>
							</div>
						</a>
					</li>
					<li class="b-status col-xs-12 col-sm-6 col-md-4 col-lg-4">
						<a href="<c:url value="/?typ=user" />">
							<div class="b-status-card">
								<span><img src="<c:url value="/resources/images/set.png" />" alt="系统设置" /></span>
								<h3>系统设置</h3>
							</div>
						</a>
					</li>
				<%
					}else if(user.getRole() == UserRole.SENDMEM){
				%>
					<li class="b-status col-xs-12 col-sm-6 col-md-4 col-lg-4">
						<a href="<c:url value="/?typ=arrive" />">
							<div class="b-status-card">
								<span><img src="<c:url value="/resources/images/arrive.png" />" alt="包裹到站" /></span>
								<h3>包裹到站</h3>
							</div>
						</a>
					</li>

				<% 
					}else{
				%>
					<li class="b-status col-xs-12 col-sm-6 col-md-4 col-lg-4">
					<a href="<c:url value="/?typ=mailQuery" />">
						<div class="b-status-card">
							<span><img src="<c:url value="/resources/images/query.png" />" alt="数据查询" /></span>
							<h3>数据查询</h3>
						</div>
					</a>
					</li>
					<li class="b-status col-xs-12 col-sm-6 col-md-4 col-lg-4">
						<a href="<c:url value="/?typ=capacityDistribution" />">
							<div class="b-status-card">
								<span><img src="<c:url value="/resources/images/n_capacity.png" />" alt="运力分布" /></span>
								<h3>运力分布</h3>
							</div>
						</a>
					</li>
					<li class="b-status col-xs-12 col-sm-6 col-md-4 col-lg-4">
						<a href="<c:url value="/?typ=deliverArea" />">
							<div class="b-status-card">
								<span><img src="<c:url value="/resources/images/d_area.png" />" alt="配送区域" /></span>
								<h3>配送区域</h3>
							</div>
						</a>
					</li>
					<li class="b-status col-xs-12 col-sm-6 col-md-4 col-lg-4">
						<a href="<c:url value="/?typ=siteManage" />">
							<div class="b-status-card">
								<span><img src="<c:url value="/resources/images/set.png" />" alt="系统设置" /></span>
								<h3>系统设置</h3>
							</div>
						</a>
					</li>
				<% 
					}
				%>									
			</ul>
		</div>

	</div>
	<div class="b-con-bot"></div>
</div>
<!-- E content -->

<!-- S pop -->
<div id="mask"></div>
<!--S 引导页-->
<div class="j-guide-pop modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog modal-lg" role="document">
		<div class="modal-content">
			<jsp:include page="live-guide.jsp"/>
		</div>
	</div>
</div>
<!--E 引导页-->

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
							<input type="password" class="form-control form-bod j-n-pwd" id="password" name="password" placeholder="密码"  />
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


<!-- E alert -->
<footer class="container tc">
	<em class="b-copy">京ICP备 465789765 号 版权所有 &copy; 2016-2020 棒棒达       北京棒棒达科技有限公司</em>
</footer>
<!-- E footer -->
<script type="application/javascript">
	if(${user.loginCount==1} && ${user.role=="SITEMASTER"}){
		$(".j-guide-pop").show().addClass("in");
		$("#mask").show();
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
					location.reload();
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
