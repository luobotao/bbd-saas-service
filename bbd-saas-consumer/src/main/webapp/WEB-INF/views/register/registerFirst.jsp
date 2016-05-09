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
	<!-- S 搜索区域 -->
	<c:url var="actionUrl" value="/register/saveUser?${_csrf.parameterName}=${_csrf.token}"/>
	<form role="form" action="${actionUrl}" method="post" id="userForm" class="form-inline form-inline-n">
		<div class="search-area">
			<div class="row pb20">
				<div class="form-group col-xs-12 col-sm-6 col-md-4 col-lg-4">
					<label>手机号：</label>
					<input type="text" class="form-control form-bod wp80" id="username" name="username" onkeyup="this.value=this.value.replace(/[^\d]/g,'')" onblur="checkLoginName(this.value)">
					<input type="text" class="form-control" id="usernameFlag" name="usernameFlag" value="1" style="display:none;">
					<em class="tip-info" id="usernameP" style="display:none;">请输入正确的手机号,不允许重复</em>
				</div>
			</div>
			<div class="row pb20">
				<div class="form-group col-xs-12 col-sm-6 col-md-4 col-lg-4">
					<label>验证码：</label>
					<input type="text" id="verifyCode" name="verifyCode" placeholder="验证码" class="form-control"  />
					<a href="javascript:void(0);" class="ser-btn l" id="sendVerifyCode" onclick="sendVerifyCode();">获取验证码</a>
					<em class="tip-info" id="verifyCodeP" style="display:none;">请输入验证码</em>

				</div>
			</div>
			<div class="row pb20">
				<div class="form-group col-xs-12 col-sm-6 col-md-4 col-lg-4">
					<label>设置密码：</label>
					<input type="text" id="password" name="password" placeholder="密码" class="form-control"  />
					<em class="tip-info" id="passwordP" style="display:none;">请输入密码</em>
				</div>
			</div>
			<div class="row pb20">
				<div class="form-group col-xs-12 col-sm-6 col-md-4 col-lg-4">
					<label>确认密码：</label>
					<input type="text" id="confirmpass" name="confirmpass" placeholder="密码" class="form-control"  />
				</div>
			</div>



			<div class="modal-footer b-modal-body bod0">
				<div class="fl site-re">
					<label>
						<input type="checkbox"  id="agreeCheck" checked/>
						同意
					</label>
					<a href="http://ht.neolix.cn/www/wap/protocal.html" target="_blank"><em class="orange">《棒棒达快递注册协议》</em></a>
				</div>
			</div>

			<div class="row pb20">
				<div class="form-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
					<a href="javascript:void(0);" class="ser-btn l" id="saveUserBtn" >保存</a>
				</div>
			</div>

		</div>
	</form>

        <!-- E content -->
        <!-- S footer -->
        <footer class="pos-footer tc">
            <em class="b-copy">京ICP备 465789765 号 版权所有 &copy; 2016-2020 棒棒达       北京棒棒达科技有限公司</em>
        </footer>
        <!-- E footer -->
        <!-- S pop -->
        <!--S 新建-->

		<!--E 新建-->
        <!-- E pop -->
<script type="text/javascript">
	function checkLoginName(loginName){
		if(loginName!=""){
			var linkUrl = "<c:url value="/register/checkLoginName?loginName=" />"+loginName
			$.ajax({
				url: linkUrl,
				type: 'GET',
				cache: false,
				dataType: "text",
				data: {},
				success: function(response){
					console.log(response);
					if(response=="false"){
						$("#usernameFlag").val(0);
						$("#usernameP").html("手机号已存在");
						$("#usernameP").attr("style","color:red");
					}else{
						$("#usernameFlag").val(1);
						$("#usernameP").attr("style","display:none");
					}
				},
				error: function(){
					alert('服务器繁忙，请稍后再试！');
				}
			});
		}
	}
	function sendVerifyCode(){
		var phone = $("#username").val();
		if(phone!=""){
			var phoneFlag = $("#usernameFlag").val();
			if(phoneFlag==0){
				alert("请更换手机号");
				return false;
			}
			var linkUrl = "<c:url value="/sendVerifyCode?phone=" />"+phone
			$.ajax({
				url: linkUrl,
				type: 'GET',
				cache: false,
				dataType: "json",
				data: {},
				success: function(response){
					console.log(response);
					if(response!="" && response!=null && response.status=="1"){
						alert("发送成功");
					}else{
						alert("发送失败");
					}
				},
				error: function(){
					alert('服务器繁忙，请稍后再试！');
				}
			});
		}else{
			alert("请输入手机号");
		}
	}

	$("#saveUserBtn").click(function(){
		var flag = $("#agreeCheck").is(':checked');
		if(flag==false){
			alert("请先同意《棒棒达快递注册协议》");
			return false;
		}
		var username = $.trim($('input[name="username"]').val());
		var usernameFlag = $("#usernameFlag").val();
		if(username==""||usernameFlag==0){
			$("#usernameP").attr("style","color:red");
			flag = false;
		} else{
			if(checkMobile(username)==false){
				$("#usernameP").html("请输入正确的手机号");
				$("#usernameP").attr("style","color:red");
				flag = false;
			}else{
				$("#usernameP").attr("style","display:none");
			}
		}
		var verifyCode = $.trim($('input[name="verifyCode"]').val());
		if(verifyCode==""){
			$("#verifyCodeP").attr("style","color:red");
			flag = false;
		} else{
			$("#verifyCodeP").attr("style","display:none");
		}
		var password = $.trim($('input[name="password"]').val());
		if(password==""){
			$("#passwordP").attr("style","color:red");
			flag = false;
		} else{
			$("#passwordP").attr("style","display:none");
		}
		<%--window.location.href="<c:url value="/register/registerSecond?phone=" />"+username;--%>
		if(flag){
			$("#userForm").ajaxSubmit({
				type: 'post',
				dataType: 'json',
				success: function(response){
					if(response!=null && response.status=="1"){
						window.location.href="<c:url value="/register/registerSecond?phone=" />"+username;
					}else {
						alert(response.msg);
					}
				},
				error: function(JsonHttpRequest, textStatus, errorThrown){
					alert("未知错误");
				}
			});
		}else{
			return false;
		}
	})
</script>
</body>
</html>