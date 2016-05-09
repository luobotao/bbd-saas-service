<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<html>
<head>
	<jsp:include page="../main.jsp" flush="true" />
</head>
<body >
<!-- Content Header (Page header) -->
<section class="content-header">
	<h1 style="float:left; margin-left:40px">
		<c:if test="${postcompany.sta==0}">审核中</c:if>
		<c:if test="${postcompany.sta==1}">审核通过</c:if>
		<c:if test="${postcompany.sta==2}">驳回</c:if>
	</h1>
</section>

<!-- Main content -->
<section class="content">
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12">
				<div class="tab-content" style="height:800px;">
						<div class="box-body">
							<div class="row">
								<div class="col-xs-12" style="margin-top:10px;">
									<label>
										<c:if test="${postcompany.sta==0}">恭喜，您的棒棒达快递账号申请信息提交成功。我们将在1-3个⼯作⽇内完成审核</c:if>
										<c:if test="${postcompany.sta==1}">审核通过</c:if>
										<c:if test="${postcompany.sta==2}">驳回</c:if>
									</label>
								</div>
							</div>
							<div class="row">
								<div class="col-xs-1" style="margin-top:10px;">
									<label style="float: right;">账号类型:</label>
								</div>
								<div class="col-xs-4" style="margin-top:10px;">
									<label>配送公司</label>
								</div>
							</div>
							<div class="row">
								<div class="col-xs-1" style="margin-top:10px;">
									<label style="float: right;">手机号:</label>
								</div>
								<div class="col-xs-4" style="margin-top:10px;">
									<label>${user.loginName}</label>
								</div>
							</div>
							<div class="row">
								<div class="col-xs-1" style="margin-top:10px;">
									<label style="float: right;">公司名称:</label>
								</div>
								<div class="col-xs-4" style="margin-top:10px;">
									<label>${postcompany.companyname}</label>
								</div>
							</div>
							<div class="row" id="licensePicDiv" style="margin-top: 10px;">
								<div class="col-xs-1">
									<label style="float: right;">营业执照:</label>
								</div>
								<div class="col-xs-4">
									<img style="width: 200px;height: 200px;" alt="" src="${ossUrl}${postcompany.licensePic}@200w"/>
								</div>
							</div>
							<div class="row">
								<div class="col-xs-1" style="margin-top:10px;">
									<label style="float: right;">负责人:</label>
								</div>
								<div class="col-xs-4" style="margin-top:10px;">
									<label>${user.realName}</label>
								</div>
							</div>

							<div class="row" id="emailDiv" style="margin-top:10px;">
								<div class="col-xs-1">
									<label style="float: right;">联系邮箱:</label>
								</div>
								<div class="col-xs-4">
									<label>${user.email}</label>
								</div>
							</div>
							<div class="row" style="margin-top:10px ;">
								<div class="col-xs-1">
									<label style="float: right ;">省市区:</label>
								</div>
								<div class="col-xs-4">
									<label>${postcompany.province} ${postcompany.city} ${postcompany.area} </label>
								</div>
							</div>
							<div class="row" style="margin-top:10px ;">
								<div class="col-xs-1">
									<label style="float: right ;">详细地址: </label>
								</div>
								<div class="col-xs-4">
									<label>${postcompany.address}</label>
								</div>
							</div>

						</div><!-- /.box-body -->
						<div class="box-footer">
							<button type="button" class="btn btn-primary" style="margin-left: 170px ;" onclick="window.location.href='<c:url value="/login"/>'">
								返回</button>
						</div>
				</div>
			</div>
		</div>
	</div>
</section>
</body>
</html>
