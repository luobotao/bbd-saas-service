<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>站点注册</title>
	<jsp:include page="../main.jsp" flush="true"/>
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
	<div class="n-progress-area">
		<div class="n-progress-outer">
			<div class="n-progress-inner n-progress-inner3">

			</div>
		</div>
		<div class="n-progress-txt">
			<span class="n-step n-fcur">注册账号</span>
			<span class="n-step n-fcur">选择类型</span>
			<span class="n-fcur">填写资料</span>
		</div>
	</div>
	<!-- S 注册信息 -->
	<div class="n-form-infoA">
		<div class="n-form-info clearfix">
			<!-- S 第三步 02 站点 -->
			<div class="n-form-con n-site-c form-inline form-inline-n clearfix">
				<c:url var="actionUrl" value="/register/saveSite?${_csrf.parameterName}=${_csrf.token}"/>
				<form role="form" action="${actionUrl}" method="post" id="siteForm" enctype="multipart/form-data" >
				<input type="hidden" id="phone" name="phone" value="${phone}">
				<div class="txt-input pb30">
					<i>站点名称：</i><input type="text" class="form-control w317" id="name" name="name" />
				</div>
				<div class="txt-input pb30 clearfix">
					<i class="fl">所属公司：</i>
					<em class="sel-mar">
						<select class="form-control form-bod min-w150" id="companyId" name="companyId">
							<c:forEach var="postcompany" items="${postcompanyList}">
								<option value="${postcompany.id}">${postcompany.companyname}</option>
							</c:forEach>
						</select>
					</em>
				</div>
				<div class="txt-input pb30" id="city_4">
					<i class="fl mt8">所在地：</i>
					<div class="fl pb30">
						<em class="sel-mar">
							<select class="form-control form-bod w150  prov" name="prov"></select>
						</em>
						<em class="sel-mar">
							<select class="form-control form-bod w150 city" disabled="disabled"></select>
						</em>
						<em class="sel-mar">
							<select class="form-control form-bod w150 dist" name="dist" disabled="disabled"></select>
						</em>
					</div>
					<input type="text" class="form-control form-bod input-detail w317 mli-wid" placeholder="请输入详细地址" id="address" name="address"/>
					<input id="province" name="province" type="hidden" class="form-control"/>
					<input id="city" name="city" type="hidden" class="form-control"/>
					<input id="area" name="area" type="hidden" class="form-control"/>
				</div>
				<div class="txt-input pb30">
					<i>负责人姓名：</i><input type="text" class="form-control w317" id="responser" name="responser" />
				</div>
				<div class="txt-input pb30">
					<i>联系邮箱：</i><input type="text" class="form-control w317" id="email" name="email" onkeyup="value=value.replace(/[^a-zA-Z\-_@@\.0-9]/g,'')" />
					<em class="input-tip mli-wid mt10">请填写本人常用邮箱，此邮箱将用来接收重要通知邮件</em>
				</div>
				<div class="txt-input mli-wid">
					<a href="javascript:history.go(-1);" class="ser-btn l mr46">上一步</a>
					<a href="javascript:void(0);" class="ser-btn l" id="saveSiteBtn">提交</a>
				</div>
				</form>
			</div>
			<!-- E 第三步 02 站点 -->
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
	var defprov = "北京";
	var defcity = "北京";
	var defdist = "朝阳区";
	if ($("#province").val() != "") {
		defprov = $("#province").val();
		defcity = $("#city").val();
		defdist = $("#area").val();
	}
	$("#city_4").citySelect({
		prov: defprov,
		city: defcity,
		dist: defdist,
		nodata: "none"
	});


	$("#saveSiteBtn").click(function () {
		var name = $.trim($('input[name="name"]').val());
		var nameP = $("#nameP").val();
		if (name == "" || name == 0) {
			outDiv("请输入站点名称");
			return false;
		}

		var province = $(".prov").val();
		var city = $(".city").val();
		var area = $(".dist").val();
		$("#province").val(province);
		$("#city").val(city);
		$("#area").val(area);

		var address = $("#address").val();
		if(address==""){
			outDiv("请输入详细地址");
			return false;
		}

		var responser = $("#responser").val();
		if(responser==""){
			outDiv("请输入负责人姓名");
			return false;
		}

		var email = $("#email").val();
		if(email==""){
			outDiv("请输入邮箱");
			return false;
		} else{
			var emailFlag = checkemail(email);
			if(emailFlag==false){
				outDiv("请输入正确的邮箱");
				return false;
			}
		}
		$("#siteForm").submit();
	})
</script>
</body>
</html>