<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.bbd.saas.enums.SiteStatus" %>
<%@ page import="com.bbd.saas.enums.SiteTurnDownReasson" %>
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
			<c:url var="actionUrl" value="/system/siteManage/saveSite?${_csrf.parameterName}=${_csrf.token}"/>
			<form role="form" action="${actionUrl}" method="post" id="siteForm" enctype="multipart/form-data" class="form-inline form-inline-n">
				<input id="phone" name="phone" value="${site.username}" type="hidden"/>
				<input id="from" name="from" value="1" type="hidden"/>
			<div class="txt-input pb30">
				<i>账号类型：</i>站点
			</div>
			<div class="txt-input pb30">
				<i>手机号：</i>${site.username}
			</div>
			<div class="txt-input pb30">
				<i>站点名称：</i><input type="text" class="form-control w317" id="name" name="name" value="${site.name}"/>
			</div>
			<div class="txt-input pb30">
				<i>所属公司：</i>
				<select class="form-control form-bod w150" id="companyId" name="companyId">
					<c:forEach var="postcompany" items="${postcompanyList}">
						<option value="${postcompany.id}" <c:if test="${postcompany.id==site.companyId}">selected</c:if> >${postcompany.companyname}</option>
					</c:forEach>
				</select>
			</div>
			<div class="txt-input pb30" id="city_4">
				<i class="fl mt8">所在地：</i>
				<div class="fl pb30">
					<em class="sel-mar">
						<select class="form-control form-bod w150 prov" name="prov"></select>
					</em>
					<em class="sel-mar">
						<select class="form-control form-bod w150 city" disabled="disabled"></select>
					</em>
					<em class="sel-mar">
						<select class="form-control form-bod w150 dist" name="dist" disabled="disabled"></select>
					</em>
					<input id="province" name="province" type="hidden" class="form-control"/>
					<input id="city" name="city" type="hidden" class="form-control"/>
					<input id="area" name="area" type="hidden" class="form-control"/>
				</div>
				<input type="text" class="form-control form-bod input-detail w317 mli-wid" id="address" name="address" placeholder="请输入详细地址" value="${site.address}"/>
			</div>
			<div class="txt-input pb30">
				<i>负责人姓名：</i>
				<input type="text" class="form-control w317" id="responser" name="responser" value="${site.responser}">
			</div>
			<div class="txt-input pb30">
				<i>联系邮箱：</i>
				<input type="text" class="form-control w317" id="email" name="email" value="${site.email}" onkeyup="value=value.replace(/[^a-zA-Z\-_@@\.0-9]/g,'')">
				<em class="input-tip mli-wid mt10">请填写本人常用邮箱，此邮箱将用来接收重要通知邮件</em>
			</div>
				<div class="txt-input"><span class="ser-btn l mli-wid" id="saveSiteBtn" >提交</span></div>
		</form>
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

<script type="text/javascript">
	var defprov = "${site.province}";
	var defcity = "${site.city}";
	var defdist = "${site.area}";

	$("#city_4").citySelect({
		prov: defprov,
		city: defcity,
		dist: defdist,
		nodata: "none"
	});

	$("#saveSiteBtn").click(function () {
		var flag = true;
		var name = $.trim($("#name").val());
		if (name == "" ) {
			outDiv("请输入站点名称");
			return false;
		}
		var province = $(".prov").val();
		var city = $(".city").val();
		var area = $(".dist").val();
		$("#province").val(province);
		$("#city").val(city);
		$("#area").val(area);

		var responser = $("#responser").val();
		if(responser==""){
			outDiv("请输站长姓名");
			return false;
		}

		var phone = $.trim($('input[name="phone"]').val());
		var phoneFlag = $("#phoneFlag").val();
		if(phone==""){
			outDiv("请输入手机号");
			return false;
		} else{
			if(checkMobile(phone)==false){
				outDiv("请输入正确的手机");
				return false;
			}else{
				if(phoneFlag==0){
					outDiv("手机号已存在");
					return false;
				}
			}
		}
//		var password = $("#password").val();
//		if(password==""){
//			outDiv("请输入登录密码");
//			return false;
//		}
//		var passwordConfirm = $("#passwordConfirm").val();
//		if(passwordConfirm==""){
//			outDiv("请确认登录密码");
//			return false;
//		}
//
//		if(!pwdreg.test(password)){
//			outDiv("请输入6-12位数字和字母结合的密码");
//			return false;
//		}
//		if(passwordConfirm!=password){
//			outDiv("两次密码不一致");
//			return false;
//		}

		var email = $("#email").val();
		if(email==""){
			outDiv("请输入邮箱");
			return false;
		} else{
			var emailFlag = checkemail(email);
			if(emailFlag==false){
				outDiv("邮箱格式不正确");
				return false;
			}
		}
		$("#siteForm").ajaxSubmit({
			success: function(data){
				if(data==true){
					window.location.href="<c:url value="/register/regitsterSiteView?siteid="/>${site.id}";
				}else{
					alert( "保存站点失败");
				}
			},
			error: function(JsonHttpRequest, textStatus, errorThrown){
				alert( "服务器异常!");
			}
		});
	})
</script>
</body>
</html>
