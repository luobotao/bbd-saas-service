<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<html>
<head>
	<title>棒棒达快递</title>
	<jsp:include page="../main.jsp" flush="true" />
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
			<i class="nav navbar-nav navbar-right re-navbar f16">已有账号？<a href="<c:url value="/login"/>" class="orange n-login">马上登录</a></i>
		</div><!-- /.navbar-collapse -->
	</div><!-- /.container-fluid -->
</nav>
<!-- S content-->
<div class="container n-re-con">
	<div class="n-progress-area">
		<div class="n-progress-outer">
			<div class="n-progress-inner">

			</div>
		</div>
		<div class="n-progress-txt">
			<span class="n-step n-fcur">注册账号</span>
			<span class="n-step">选择类型</span>
			<span>填写资料</span>
		</div>
	</div>
	<!-- S 注册信息 -->
	<div class="n-form-infoA">
		<div class="n-form-info clearfix">
		<c:url var="actionUrl" value="/register/saveUser?${_csrf.parameterName}=${_csrf.token}"/>
		<form role="form" action="${actionUrl}" method="post" id="userForm" class="form-inline form-inline-n">
			<!-- S 第一步 -->
			<div class="n-form-con form-inline form-inline-n clearfix">
				<div class="txt-input pb30">
					<i>　手机号：</i><input type="text" class="form-control w317"  id="username" name="username" onkeyup="this.value=this.value.replace(/[^\d]/g,'')" onblur="checkLoginName(this.value)"/>
					<input type="text" class="form-control" id="usernameFlag" name="usernameFlag" value="1" style="display:none;">
				</div>
				<div class="txt-input pb30">
					<i>　验证码：</i><input type="text" class="form-control w317" id="verifyCode" name="verifyCode" />
					<span class="ser-btn l" id="sendVerifyCode" onclick="sendVerifyCode();">获取验证码</span>
				</div>
				<div class="txt-input pb30">
					<i>设置密码：</i><input type="password" class="form-control w317 j-n-pwd" id="password" name="password" placeholder="密码"  />
				</div>
				<div class="txt-input pb30">
					<i>确认密码：</i><input type="password" class="form-control w317 j-c-pwd" id="passwordC" name="passwordC"  />
				</div>
				<div class="n-agreement pb20">
					<label>
						<input type="checkbox"  id="agreeCheck" checked/>
						同意
					</label>
					<a href="http://ht.neolix.cn/www/wap/protocal.html" target="_blank"><em class="orangefa">《棒棒达快递注册协议》</em></a>
				</div>
				<div class="n-agreement"><a href="javascript:void(0);" class="ser-btn l" id="saveUserBtn">立即注册</a></div>
			</div>
			<!-- E 第一步 -->
		</form>
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
<script type="text/javascript">
	function checkLoginName(loginName){
		var phone = $("#username").val();
		if(phone==""){
			outDiv("请输入手机号");
			return false;
		} else{
			if(checkMobile(phone)==false){
				outDiv("请输入正确的手机号");
				return false;
			}
		}
		var linkUrl = "<c:url value="/register/checkLoginName?loginName=" />"+phone
		$.ajax({
			url: linkUrl,
			type: 'GET',
			cache: false,
			dataType: "text",
			data: {},
			success: function(response){
				console.log(response);
				if(response=="false"){
					outDiv("手机号已存在");
					$("#usernameFlag").val(0);
				}else{
					$("#usernameFlag").val(1);
				}
			},
			error: function(){
				alert_mine("错误","服务器繁忙，请稍后再试！");
			}
		});
	}
	function sendVerifyCode(){
		var phone = $("#username").val();
		if(phone==""){
			outDiv("请输入手机号");
			return false;
		} else{
			if(checkMobile(phone)==false){
				outDiv("请输入正确的手机号");
				return false;
			}
		}
		var linkUrl = "<c:url value="/register/checkLoginName?loginName=" />"+phone
		$.ajax({
			url: linkUrl,
			type: 'GET',
			cache: false,
			dataType: "text",
			data: {},
			success: function(response){
				console.log(response);
				if(response=="false"){
					outDiv("手机号已存在");
					$("#usernameFlag").val(0);
				}else{
					$("#usernameFlag").val(1);
					$.ajax({
						url: "<c:url value="/sendVerifyCode?phone=" />"+phone,
						type: 'GET',
						cache: false,
						dataType: "json",
						data: {},
						success: function(response){
							console.log(response);
							if(response!="" && response!=null && response.status=="1"){
								alert_mine("成功","发送成功");
							}else{
								alert_mine("失败","发送失败");
							}
						},
						error: function(){
							alert('服务器繁忙，请稍后再试！');
						}
					});
				}
			},
			error: function(){
				alert_mine("错误","服务器繁忙，请稍后再试！");
			}
		});
	}

	$("#saveUserBtn").click(function(){
		var flag = $("#agreeCheck").is(':checked');
		if(flag==false){
			alert_mine("错误","请先同意《棒棒达快递注册协议》");
			return false;
		}
		var username = $.trim($('input[name="username"]').val());
		var usernameFlag = $("#usernameFlag").val();
		if(username==""){
			outDiv("请输入手机号");
			return false;
		} else{
			if(checkMobile(username)==false){
				outDiv("请输入正确的手机号");
				return false;
			}
		}
		var verifyCode = $.trim($('input[name="verifyCode"]').val());
		if(verifyCode==""){
			outDiv("请输入验证码");
			return false;
		}
		var password = $.trim($('input[name="password"]').val());
		if(password==""){
			outDiv("请输入密码");
			return false;
		}
		if(!pwdreg.test(password)){
			outDiv("请输入6-12位数字和字母结合的密码");
			return false;
		}
		var passwordC = $.trim($('input[name="passwordC"]').val());
		if(passwordC==""){
			outDiv("请确认密码");
			return false;
		}
		if(passwordC!=password){
			outDiv("两次密码不一致");
			return false;
		}
		<%--window.location.href="<c:url value="/register/registerSecond?phone=" />"+username;--%>
		$("#userForm").ajaxSubmit({
			type: 'post',
			dataType: 'json',
			success: function(response){
				if(response!=null && response.status=="1"){
					window.location.href="<c:url value="/register/registerSecond?phone=" />"+username;
				}else {
					alert_mine("错误",response.msg);
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