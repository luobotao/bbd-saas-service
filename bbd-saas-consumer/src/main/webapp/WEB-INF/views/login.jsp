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
			<ul class="nav navbar-nav navbar-right f16">
				<li class="cur"><a href="javascript:void(0);">首页</a></li>
				<li><a href="javascript:void(0);">关于我们</a></li>
				<li><a href="javascript:void(0);">合作伙伴</a></li>
			</ul>
		</div><!-- /.navbar-collapse -->
	</div><!-- /.container-fluid -->
</nav>
<!-- E nav -->
<!-- S banner -->
<div class="b-banner mb120">
	<div class="container">
		<div class="clearfix">
			<div class="col-xs-12 col-sm-6 col-md-4 col-lg-4 col-md-offset-8 pr0">
				<div class="login-area">
					<form:form id="form" method="post" modelAttribute="loginForm" >
						<div class="header">
							<c:if test="${not empty message}">
								<div id="message" class="error">${message}</div>
							</c:if>
						</div>
						<div class="has-icon mb12">
							<i class="glyphicon glyphicon-user c-white pl15"></i>
							<form:input path="userName" class="b-input pl15" placeholder="输入账号" onkeypress="enterPress(event)"/>
						</div>
						<div class="has-icon mb12">
							<i class="glyphicon glyphicon-lock c-white pl15"></i>
							<form:input path="passWord" type="password" class="b-input pl15" placeholder="输入密码" onkeypress="enterPress(event)"/>
						</div>
						<div class="clearfix mlrf15">
							<div class="col-md-6">
								<a href="javascript:void(0);" class="sbtn l mb12" onclick="$('#form').submit()">登　录</a>
							</div>
							<div class="col-md-6">
								<a href="javascript:void(0);" class="sbtn d j-login">注　册</a>
							</div>

						</div>
					</form:form>
				</div>

			</div>
		</div>
	</div>
</div>
<!-- E banner -->
<!-- S 关于我们 -->
<div class="container">
	<ul class="row">
		<li class="b-item col-xs-12 col-sm-6 col-md-4 col-lg-4">
			<div class="b-item-card">
				<span><img src="<c:url value="/resources/images/efficien.png" />" /></span>
				<h3>高效运力</h3>
				<i>覆盖全国3000名专业小件员、物流众包人员，可高效处理电商大促高峰订单.</i>
			</div>
		</li>
		<li class="b-item col-xs-12 col-sm-6 col-md-4 col-lg-4">
			<div class="b-item-card">
				<span><img src="<c:url value="/resources/images/infomation.png" />" /></span>
				<h3>快递行业信息化</h3>
				<i>对于长期合作电商卖家，提供完整的物流解决方案，通过信息化服务解决快递行业效率问题，提升物流效率..</i>
			</div>
		</li>
		<li class="b-item col-xs-12 col-sm-6 col-md-4 col-lg-4">
			<div class="b-item-card">
				<span><img src="<c:url value="/resources/images/light.png" />" /></span>
				<h3>快速响应</h3>
				<i>覆盖全国3000名专业小件员、物流众包人员，可高效处理电商大促高峰订单.</i>
			</div>
		</li>
	</ul>
</div>
<!-- E 关于我们  -->
<!-- S 合作伙伴 -->
<h3 class="b-tit"><em>TA们都在使用棒棒达快递</em></h3>
<div class="clearfix">　</div>
<div class="container mb80">
	<ul class="row b-brand">
		<li class="b-grid">
			<img src="<c:url value="/resources/images/ln.png" />" alt="李宁" />
		</li>
		<li class="b-grid">
			<img src="<c:url value="/resources/images/hs.png" />" alt="韩束" />
		</li>
		<li class="b-grid">
			<img src="<c:url value="/resources/images/lyl.png" />" alt="蓝月亮" />
		</li>
		<li class="b-grid">
			<img src="<c:url value="/resources/images/md.png" />" alt="美的" />
		</li>
		<li class="b-grid">
			<img src="<c:url value="/resources/images/lt.png" />" alt="骆驼" />
		</li>
		<li class="b-grid">
			<img src="<c:url value="/resources/images/super.png" />" alt="super" />
		</li>
		<li class="b-grid">
			<img src="<c:url value="/resources/images/dl.png" />" alt="得力办公" />
		</li>
		<li class="b-grid">
			<img src="<c:url value="/resources/images/at.png" />" alt="安踏" />
		</li>
		<li class="b-grid">
			<img src="<c:url value="/resources/images/qf.png" />" alt="清风" />
		</li>
		<li class="b-grid col-sm-6">
			<img src="<c:url value="/resources/images/hyx.png" />" alt="恒源祥" />
		</li>
	</ul>
</div>
<!-- E 合作伙伴 -->
<!-- S footer -->
<footer class="container tc">
	<em class="b-copy">京ICP备 465789765 号 版权所有 &copy; 2016-2020 棒棒达       北京棒棒达科技有限公司</em>
</footer>
<!-- E footer -->

<!-- S pop -->
<div class="j-login-type modal fade in" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header b-modal-header"><h4 class="modal-title tc">请选择注册类型</h4></div>
			<div class="modal-body">
				<ul class="row">
					<li class="cho-lg col-md-6">
						<a href="javascript:void(0);">
							<div class="cho-lg-card">
								<span><img src="<c:url value="/resources/images/site.png" />" alt="站点"></span>
								<h3>站点</h3>
							</div>
						</a>
					</li>
					<li class="cho-lg col-md-6">
						<a href="javascript:void(0);" data-dismiss="modal" class="j-site-re" >
							<div class="cho-lg-card">
								<span><img src="<c:url value="/resources/images/trader.png" />" alt="站点"></span>
								<h3>电商商家</h3>
								<i>暂未开通，敬请期待</i>
							</div>
						</a>
					</li>
				</ul>
			</div>
		</div>
	</div>
</div>


<!--S 站点注册-->
<div class="j-site-re-pop modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog b-guide-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header b-modal-header">
				<h4 class="modal-title tc">站点注册</h4>
			</div>
			<div class="modal-body b-modal-body">
				<ul class="b-n-crt">
					<li class="filter">
						<i>账号：</i>
						<input type="text" class="form-control form-bod wp80" />
					</li>
					<li class="filter">
						<i>密 码：</i>
						<input type="password" class="form-control form-bod wp80" />
					</li>
					<li class="filter">
						<i>站点名称：</i>
						<input type="text" class="form-control form-bod wp80" />
					</li>
					<li class="filter">
						<i>负责人：</i>
						<input type="text" class="form-control form-bod wp80" />
					</li>
					<li class="filter">
						<i>负责人电话：</i>
						<input type="text" class="form-control form-bod wp80" />
					</li>
					<li class="filter">
						<i>固定电话：</i>
						<input type="text" class="form-control form-bod wp80" />
					</li>
					<li class="filter">
						<i>邮箱：</i>
						<input type="text" class="form-control form-bod wp80" />
					</li>
					<li class="filter">
						<i>地 址：</i>
						<em class="wp25">
							<select class="form-control form-bod ">
								<option>省</option>
							</select>
						</em>
						<em class="wp25">
							<select class="form-control form-bod ">
								<option>市</option>
							</select>
						</em>
						<em class="wp25">
							<select class="form-control form-bod ">
								<option>区</option>
							</select>
						</em>

						<input type="text" class="form-control form-bod wp80 input-d" placeholder="请输入详细地址" />
					</li>
					<li class="filter">
						<i>公司营业执照：</i>
						<input type="text" class="form-control form-bod fl wp66" />
						<a href="javascript:void(0);" class="ser-btn g fr">选择</a>
					</li>
				</ul>
			</div>
			<div class="modal-footer mb15 mt20 bod0">
				<div class="fl site-re">
					<label>
						<input type="checkbox" />
						同意<em class="orange">《棒棒达快递注册协议》</em>
					</label>
				</div>
				<a href="javascript:void(0);" class="ser-btn g">查看已注册站点</a>
				<a href="javascript:void(0);" class="ser-btn l">保存</a>
			</div>
		</div>
	</div>
</div>
<!--E 站点注册-->
<!-- E pop -->
<script>
	//回车事件
	function enterPress(e){
		if(!e) e = window.event;//火狐中是 window.event
		if((e.keyCode || e.which) == 13){
			if($("#userName").val()==null || $("#userName").val()==""){
				$("#userName").focus();
				return;
			}
			if($("#passWord").val()==null || $("#passWord").val()==""){
				$("#passWord").focus();
				return;
			}
			$('#form').submit()
		}
	}
</script>
</body>
</html>