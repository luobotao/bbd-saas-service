<%@ page import="com.bbd.saas.utils.OSSUtils" %>
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
		<c:if test="${site.flag==0}">站点注册</c:if>
		<c:if test="${site.flag==1}">审核中</c:if>
		<c:if test="${site.flag==2}">审核通过</c:if>
		<c:if test="${site.flag==3}">驳回</c:if>
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
									<label>${site.memo}</label>
								</div>
							</div>
							<div class="row">
								<div class="col-xs-1" style="margin-top:10px;">
									<label style="float: right;">站点名称:</label>
								</div>
								<div class="col-xs-4" style="margin-top:10px;">
									<label>${site.name}</label>
								</div>
							</div>
							<div class="row">
								<div class="col-xs-1" style="margin-top:10px;">
									<label style="float: right;">负责人:</label>
								</div>
								<div class="col-xs-4" style="margin-top:10px;">
									<label>${site.responser}</label>
								</div>
							</div>
							<div class="row" id="telephoneDiv" style="margin-top:10px;">
								<div class="col-xs-1">
									<label style="float: right;">固定电话:</label>
								</div>
								<div class="col-xs-4">
									<label>${site.telephone}</label>
								</div>
							</div>
							<div class="row" id="emailDiv" style="margin-top:10px;">
								<div class="col-xs-1">
									<label style="float: right;">邮箱:</label>
								</div>
								<div class="col-xs-4">
									<label>${site.email}</label>
								</div>
							</div>
							<div class="row" style="margin-top:10px ;">
								<div class="col-xs-1">
									<label style="float: right ;">省市区:</label>
								</div>
								<div class="col-xs-4">
									<label>${site.province} ${site.city} ${site.area} </label>
								</div>
							</div>
							<div class="row" style="margin-top:10px ;">
								<div class="col-xs-1">
									<label style="float: right ;">详细地址: </label>
								</div>
								<div class="col-xs-4">
									<label>${site.address}</label>
								</div>
							</div>
							<div class="row" id="licensePicDiv" style="margin-top: 10px;">
								<div class="col-xs-1">
									<label style="float: right;">营业执照:</label>
								</div>
								<div class="col-xs-4">
									<img style="width: 200px;height: 200px;" alt="" src="${ossUrl}${site.licensePic}@200w"/>
								</div>
							</div>
						</div><!-- /.box-body -->
						<div class="box-footer">
							<button type="button" class="btn btn-primary" id="saveSiteBtn" style="margin-left: 170px ;" onclick="window.location.href='<c:url value="/login"/>'">
								返回</button>
						</div>
				</div>
			</div>
		</div>
	</div>
</section>
</body>
</html>
