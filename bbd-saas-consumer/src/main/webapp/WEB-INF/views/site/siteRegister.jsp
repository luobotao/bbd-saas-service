<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
	<jsp:include page="../main.jsp" flush="true" />
</head>
<body >
<!-- Content Header (Page header) -->
<section class="content-header">
	<h1 style="float:left; margin-left:40px">
		站点注册
	</h1>
</section>

<!-- Main content -->
<section class="content">
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12">
				<div class="tab-content" style="height:800px;">
					<c:if test="${not empty message}">
						<div id="message" class="success">${message}</div>
					</c:if>
					<c:url var="actionUrl" value="?${_csrf.parameterName}=${_csrf.token}"/>
					<form role="form" enctype="multipart/form-data" action="${actionUrl}" method="post" id="siteForm" >
						<div class="box-body">
							<div class="row" id="usernameDiv" style="margin-top:10px;">
								<div class="col-xs-4">
									<label for="username">账号:</label>
									<input type="text" class="form-control" id="username" name="username" onblur="checkSiteWithUsername(this.value)">
									<input type="text" class="form-control" id="usernameFlag" name="usernameFlag" value="1" style="display:none;">
									<p class="help-block" id="usernameP" style="display:none;">请输入账号,不允许重复</p>
								</div>
							</div>
							<div class="row" id="passwordDiv" style="margin-top:10px;">
								<div class="col-xs-4">
									<label for="password">密码:</label>
									<input type="text" class="form-control" id="password" name="password" >
									<p class="help-block" id="passwordP" style="display:none;">请输入密码</p>
								</div>
							</div>
							<div class="row">
								<div class="col-xs-4" style="margin-top:10px;">
									<label>站点名称:</label>
									<input type="text" class="form-control" id="name" name="name" >
									<p class="help-block" id="nameP" style="display:none;">请输入站点名称</p>
								</div>
							</div>
							<div class="row">
								<div class="col-xs-4" style="margin-top:10px;">
									<label>负责人:</label>
									<input type="text" class="form-control" id="responser" name="responser" >
									<p class="help-block" id="responserP" style="display:none;">请输入负责人</p>
								</div>
							</div>
							<div class="row" id="phoneDiv" style="margin-top:10px;">
								<div class="col-xs-4">
									<label for="phone">负责人电话:</label>
									<input type="text" class="form-control" id="phone" name="phone" onkeyup="this.value=this.value.replace(/[^\d]/g,'')" onblur="this.value=this.value.replace(/[^\d]/g,'')" maxlength="11">
								</div>
							</div>
							<div class="row" id="telephoneDiv" style="margin-top:10px;">
								<div class="col-xs-4">
									<label for="telephone">固定电话:</label>
									<input type="text" class="form-control" id="telephone" name="telephone" onkeyup="this.value=this.value.replace(/[^\d\-]/g,'')" onblur="this.value=this.value.replace(/[^\d\-]/g,'')">
									<p class="help-block" id="sitePhoneP" style="display:none;">负责人电话或固定电话必填一个</p>
								</div>
							</div>
							<div class="row" id="emailDiv" style="margin-top:10px;">
								<div class="col-xs-4">
									<label for="email">邮箱:</label>
									<input type="text" class="form-control" id="email" name="email" onkeyup="value=value.replace(/[^a-zA-Z\-_@@\.0-9]/g,'')">
									<p class="help-block" id="emailP" style="display:none;">请输入邮箱且格式正确</p>
								</div>
							</div>
							<div class="row" id="addressDiv" style="margin-top:10px;">
								<div class="col-xs-4">
									<label>地址:</label>
									<div id="city_4" >
										<select class="prov" name="prov" ></select>
										<select class="city" name="city" disabled="disabled"></select>
										<select class="dist" name="dist" disabled="disabled"></select>
									</div>
									<input id="province" name="province" placeholder="请输入省市区" type="hidden" class="form-control" />
									<input id="city" name="city" placeholder="请输入省市区" type="hidden" class="form-control" />
									<input id="area" name="area" placeholder="请输入省市区" type="hidden" class="form-control" />
									<input style="margin-top: 10px;" id="address" name="address" placeholder="请输入地址" type="text" class="form-control" />
									<p class="help-block" id="addressP" style="display:none;">请输入详细地址</p>
								</div>
							</div>
							<div class="row" id="licensePicDiv" style="margin-top: 10px;">
								<div class="col-xs-4">
									<label >公司营业执照图片：</label>
									<input id="licensePic" name="licensePic" class="file" type="file" >
									<p class="help-block" id="licensePicP" style="display:none;">请上传公司营业执照</p>
								</div>
							</div>
							<div class="row" id="proDiv" style="margin-top: 10px;">
								<div class="col-xs-4">
									<label ><input type="checkbox" id="agreeCheck"/>同意<a href="#">《棒棒达快递注册协议》</a></label>
								</div>
							</div>

						</div><!-- /.box-body -->

						<div class="box-footer">
							&nbsp;&nbsp;<button type="button" class="btn btn-primary" id="saveSiteBtn" style="margin-left: 10px;">保存</button>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</section>
<script type="text/javascript" src="/resources/javascripts/jquery.cityselect.js"></script>
<script type="text/javascript">
	var id = $("#id").val();
	var defprov = "北京";
	var defcity = "北京";
	var defdist = "朝阳区";
	if($("#province").val()!=""){
		defprov = $("#province").val();
		defcity = $("#city").val();
		defdist = $("#area").val();
	}
	$("#city_4").citySelect({
		prov:defprov,
		city:defcity,
		dist:defdist,
		nodata: "none"
	});
	$("#licensePic").fileinput({'showUpload':false, 'previewFileType':'any'});
	function checkSiteWithUsername(loginName){
		if(username!=""){
			$.ajax({
				url: '/site/checkSiteWithLoginName?loginName='+loginName,
				type: 'GET',
				cache: false,
				dataType: "text",
				data: {},
				success: function(response){
					console.log(response);
					if(response=="false"){
						alert("您输入的帐号目前已存在，请重新输入");
						$("#usernameFlag").val(0);
					}else{
						$("#usernameFlag").val(1);
					}
				},
				error: function(){
					alert('服务器繁忙，请稍后再试！');
				}
			});
		}
	}

	$("#saveSiteBtn").click(function(){
		var flag = $("#agreeCheck").is(':checked');
		if(flag==false){
			alert("请先同意《棒棒达快递注册协议》");
			return false;
		}
		var username = $.trim($('input[name="username"]').val());
		var usernameFlag = $("#usernameFlag").val();
		var password = $.trim($('input[name="password"]').val());
		var name = $("#name").val();
		var responser = $("#responser").val();
		var phone = $("#phone").val();
		var telephone = $("#telephone").val();
		var email = $("#email").val();
		var province = $(".prov").val();
		var city = $(".city").val();
		var area = $(".dist").val();
		$("#province").val(province);
		$("#city").val(city);
		$("#area").val(area);
		console.log(province+":"+city+":"+area);
		var address = $("#address").val();
		var licensePic = $("#licensePic").val();
		if(username==""||usernameFlag==0){
			$("#usernameP").attr("style","color:red");
			flag = false;
		} else{
			$("#usernameP").attr("style","display:none");
		}
		if(password==""){
			$("#passwordP").attr("style","color:red");
			flag = false;
		} else{
			$("#passwordP").attr("style","display:none");
		}
		if(name==""){
			$("#nameP").attr("style","color:red");
			flag = false;
		} else{
			$("#nameP").attr("style","display:none");
		}if(responser==""){
			$("#responserP").attr("style","color:red");
			flag = false;
		} else{
			$("#responserP").attr("style","display:none");
		}
		if(phone==""&&telephone==""){
			$("#sitePhoneP").attr("style","color:red");
			flag = false;
		} else{
			$("#sitePhoneP").attr("style","display:none");
		}
		if(email==""){
			$("#emailP").attr("style","color:red");
			flag = false;
		} else{
			$("#emailP").attr("style","display:none");
		}
		if(address==""){
			$("#addressP").attr("style","color:red");
			flag = false;
		} else{
			$("#addressP").attr("style","display:none");
		}
		if(licensePic==""){
			$("#licensePicP").attr("style","color:red");
			flag = false;
		} else{
			$("#licensePicP").attr("style","display:none");
		}
		$("#siteForm").submit();
//		if(flag){
//			console.log("succeful , submit");
//			$("#siteForm").submit();
//		}else{
//			alert("有非法内容，请检查内容合法性！");
//			return false;
//		}
	})

</script>
</body>
</html>
