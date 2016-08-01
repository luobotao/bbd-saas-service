<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
	<jsp:include page="../main.jsp" flush="true" />
</head>
<body >

<!-- Main content -->
<section class="content">
	<div class="modal-content">
		<div class="modal-header b-modal-header">
			<button type="button" class="close j-close"><span>×</span></button>
			<h4 class="modal-title tc">
				<c:if test="${site.flag==0}">站点注册</c:if>
				<c:if test="${site.flag==1}">审核中</c:if>
				<c:if test="${site.flag==2}">审核通过</c:if>
				<c:if test="${site.flag==3}">驳回</c:if>
			</h4>
		</div>
		<c:url var="actionUrl" value="/site/register?${_csrf.parameterName}=${_csrf.token}"/>
		<form role="form" enctype="multipart/form-data" action="${actionUrl}" method="post" id="siteForm" >
			<input type="hidden" id="id" name="id" value="${site.id}"/>
			<div class="modal-body b-modal-body">
				<ul class="b-n-crt">
					<li class="filter clearfix">
						<i>手机号：</i>
						<input type="text" class="form-control form-bod wp80" id="username" name="username" onkeyup="this.value=this.value.replace(/[^\d]/g,'')" onblur="checkSiteWithUsername(this.value)" value="${site.username}" readonly />
						<input type="text" class="form-control" id="usernameFlag" name="usernameFlag" value="1" style="display:none;" />
						<em class="tip-info" id="usernameP" style="display:none;">请输入正确的手机号,不允许重复</em>
					</li>
					<li class="filter">
						<i>密 码：</i>
						<input type="text" class="form-control form-bod wp80" id="password" name="password" value="${site.password}" />
						<em class="tip-info" id="passwordP" style="display:none;">请输入密码</em>
					</li>
					<li class="filter">
						<i>站点名称：</i>
						<input type="text" class="form-control form-bod wp80" id="name" name="name"  value="${site.name}" />
						<em class="tip-info" id="nameP" style="display:none;">请输入站点名称</em>
					</li>
					<li class="filter">
						<i>公司名称：</i>
						<em class="wp25">
							<select class="form-control form-bod" id="companyId" name="companyId" >
								<c:forEach var="postcompany" items="${postcompanyList}">
									<option value="${postcompany.id}" <c:if test="${ postcompany.id eq site.companyId}">selected</c:if>>${postcompany.companyname}</option>
								</c:forEach>
							</select>
							<input id="companyName" name="companyName" type="hidden" class="form-control" />
						</em>
					</li>
					<li class="filter">
						<i>负责人：</i>
						<input type="text" class="form-control form-bod wp80" id="responser" name="responser" value="${site.responser}" />
						<em class="tip-info" id="responserP" style="display:none;">请输入负责人</em>
					</li>

					<li class="filter">
						<i>固定电话：</i>
						<input type="text" class="form-control form-bod wp80" id="telephone" name="telephone" onkeyup="this.value=this.value.replace(/[^\d\-]/g,'')" onblur="this.value=this.value.replace(/[^\d\-]/g,'')" value="${site.telephone}" />
					</li>
					<li class="filter">
						<i>邮箱：</i>
						<input type="text" class="form-control form-bod wp80" id="email" name="email" onkeyup="value=value.replace(/[^a-zA-Z\-_@@\.0-9]/g,'')" value="${site.email}" />
						<em class="tip-info" id="emailP" style="display:none;">请输入邮箱且格式正确</em>
					</li>
					<li class="filter" id="city_4">
						<i>地 址：</i>
						<em class="wp25">
							<select class="form-control form-bod  prov" id="prov" name="prov" ></select>
						</em>
						<em class="wp25">
							<select class="form-control form-bod city" id="cit" disabled="disabled"></select>
						</em>
						<em class="wp25">
							<select class="form-control form-bod dist" id="dist" name="dist" disabled="disabled"></select>
						</em>
						<input id="province" name="province" placeholder="请输入省市区" type="hidden" class="form-control" value="${site.province}"  />
						<input id="city" name="city" placeholder="请输入省市区" type="hidden" class="form-control"  value="${site.city}" />
						<input id="area" name="area" placeholder="请输入省市区" type="hidden" class="form-control"  value="${site.area}" />
						<input type="text" class="form-control form-bod wp80 input-d"  id="address" name="address" placeholder="请输入详细地址" value="${site.address}" />
						<em class="tip-info" id="addressP" style="display:none;">请输入详细地址</em>
					</li>
					<li class="filter">
						<i>营业执照：</i>
						<img style="width: 200px;height: 200px;" alt="" src="${ossUrl}${site.licensePic}@200w"/>
						<input type="hidden" id="prePic" name="prePic" value="${site.licensePic}"/>
					</li>
					<li class="filter">
						<i></i>
						<input id="licensePic" name="licensePic" class="file" type="file" >
						<em class="tip-info" id="licensePicP" style="display:none;">请上传公司营业执照</em>
					</li>
				</ul>
			</div>
			<div class="modal-footer b-modal-body bod0">
				<div class="fl site-re">
					<label>
						<input type="checkbox"  id="agreeCheck" checked/>
						同意<em class="orange">《棒棒达快递注册协议》</em>
					</label>
				</div>
				<a href="javascript:void(0);" class="ser-btn l" id="saveSiteBtn" >保存</a>
			</div>
		</form>
	</div>
</section>
<script type="text/javascript" src="<c:url value="/resources/javascripts/jquery.cityselect.js?_123" />"></script>
<script type="text/javascript">
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
		if(loginName!=""){
			var linkUrl = "<c:url value="/site/checkSiteWithLoginName?loginName=" />"+loginName
			$.ajax({
				url: linkUrl,
				type: 'GET',
				cache: false,
				dataType: "text",
				data: {},
				success: function(response){
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
					ioutDiv('服务器繁忙，请稍后再试！');
				}
			});
		}
	}

	$("#saveSiteBtn").click(function(){
		var flag = $("#agreeCheck").is(':checked');
		if(flag==false){
			ioutDiv("请先同意《棒棒达快递注册协议》");
			return false;
		}
		$("#companyName").val($("#companyId").find("option:selected").text());
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
		var password = $.trim($('input[name="password"]').val());
		var name = $("#name").val();
		var responser = $("#responser").val();
		var telephone = $("#telephone").val();
		var email = $("#email").val();
		var province = $(".prov").val();
		var city = $(".city").val();
		var area = $(".dist").val();
		$("#province").val(province);
		$("#city").val(city);
		$("#area").val(area);
		var address = $("#address").val();
		var licensePic = $("#licensePic").val();
		var prePic = $("#prePic").val();
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
		if(email==""){
			$("#emailP").attr("style","color:red");
			flag = false;
		} else{
			var emailFlag = checkemail(email);
			if(emailFlag==false){
				$("#emailP").attr("style","color:red");
				flag = false;
			}else{
				$("#emailP").attr("style","display:none");
			}
		}
		if(address==""){
			$("#addressP").attr("style","color:red");
			flag = false;
		} else{
			$("#addressP").attr("style","display:none");
		}
		if(licensePic==""&&prePic==""){
			$("#licensePicP").attr("style","color:red");
			flag = false;
		} else{
			$("#licensePicP").attr("style","display:none");
		}
		if(flag){
			$("#siteForm").submit();
		}else{
			ioutDiv("有非法内容，请检查内容合法性！");
			return false;
		}
	})
</script>
</body>
</html>
