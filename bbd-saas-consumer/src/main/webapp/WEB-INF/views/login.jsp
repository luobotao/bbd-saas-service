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
	<meta name="renderer" content="webkit">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<link rel="shortcut icon" href="<c:url value="/resources/images/favicon.ico" />"/>
	<link rel="bookmark" href="<c:url value="/resources/images/favicon.ico" />"/>
	<link href="<c:url value="/resources/bootstrap/css/bootstrap.min.css" />" rel="stylesheet"  type="text/css" />
	<link href="<c:url value="/resources/stylesheets/main.css?_123" />" rel="stylesheet"  type="text/css" /><!--自定义css-->
	<link href="<c:url value="/resources/bootstrap/css/fileinput.css" />" rel="stylesheet"  type="text/css" />
	<!-- iCheck -->
	<link href="<c:url value="/resources/adminLTE/plugins/iCheck/flat/blue.css" />" rel="stylesheet"  type="text/css" />
	<link href="<c:url value="/resources/adminLTE/plugins/iCheck/square/blue.css" />" rel="stylesheet"  type="text/css" />

	<script type="text/javascript" src="<c:url value="/resources/javascripts/jquery-1.12.4.min.js"/>"></script>
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

	<script src="<c:url value="/resources/bootstrap/js/bootstrap.min.js" />" type="text/javascript"></script>
	<script src="<c:url value="/resources/javascripts/main.js" />"> </script>
	<script src="<c:url value="/resources/javascripts/checkUtil.js" />"> </script>
	<script src="<c:url value="/resources/bootstrap/js/fileinput.min.js" />" type="text/javascript"></script>
	<script src="<c:url value="/resources/bootstrap/js/fileinput_locale_zh.js" />" type="text/javascript"></script>
	<!-- iCheck -->
	<script src="<c:url value="/resources/adminLTE/plugins/iCheck/icheck.min.js" />" type="text/javascript"></script>

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
			<a class="navbar-brand" target="_blank" href="http://www.bangbangda.cn/"><img src="<c:url value="/resources/images/logo.png" />" alt="logo" /></a>
		</div>

		<!-- Collect the nav links, forms, and other content for toggling -->
		<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
			<ul class="nav navbar-nav navbar-right login-navbar f16">
				<li class="cur"><a href="javascript:void(0);">首页</a></li>
				<li><a href="#aboutUs">关于我们</a></li>
				<li><a href="#partner">合作伙伴</a></li>
			</ul>
		</div><!-- /.navbar-collapse -->
	</div><!-- /.container-fluid -->
</nav>
<!-- E nav -->
<!-- S banner -->
<div class="b-banner mb120">
	<div class="container">
		<div class="clearfix banner-txt">
			<div class="fl l-txt">
				<i>扫一扫下载棒棒达快递员App</i>
				<em><img src="<c:url value="/resources/images/banner_code.png" />" /></em>
			</div>
			<div class="fr">
				<div class="login-area">
					<div class="l-bg"></div>
					<form:form id="form" method="post" modelAttribute="loginForm" >
						<div class="header">
							<c:if test="${not empty message}">
								<div id="message" class="error">${message}</div>
							</c:if>
						</div>
						<div class="has-icon bor-tr">
							<i class="p-icon p-loginperson"></i>
							<form:input path="userName" class="b-input pl15" placeholder="输入账号" onkeypress="enterPress(event)"/>
						</div>
						<div class="has-icon bor-br">
							<i class="p-icon p-loginlock"></i>
							<form:input path="passWord" type="password" class="b-input pl15" placeholder="输入密码" onkeypress="enterPress(event)"/>
						</div>
						<div class="clearfix mlrf15">
							<div class="col-md-12">
								<a href="javascript:void(0);" class="sbtn blue mb10 f24" onclick="submitForm()">登　录</a>
							</div>
							<div class="col-md-12">
								<a href="<c:url value="/register/registerFirst" />" class="sbtn lblue f24">注　册</a>
							</div>
						</div>
					</form:form>
					<p class="warm-tips">若账号有问题，请联系客服：4008 345 690</p>
				</div>

			</div>
			<div class="banner-cloud"></div>
		</div>
	</div>
</div>
<!-- E banner -->
<!-- S 关于我们 -->
<div class="container" id="aboutUs">
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
<h3 class="b-tit" id="partner"><em>TA们都在使用棒棒达快递</em></h3>
<div class="part-bg clearfix">
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
			<li class="b-grid">
				<img src="<c:url value="/resources/images/hyx.png" />" alt="恒源祥" />
			</li>
		</ul>
	</div>
</div>

<!-- E 合作伙伴 -->
<!-- S footer -->
<footer class="container tc">
	<em class="b-copy">京ICP备 465789765 号 版权所有 &copy; 2016-2020 棒棒达       北京棒棒达科技有限公司</em>
</footer>
<!-- E footer -->

<!-- S pop -->
<div id="mask"></div>
<div class="j-login-type modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog j-scroll-dislog" role="document">
		<div class="modal-content">
			<div class="modal-header b-modal-header">
				<button type="button" class="close j-close"><span>×</span></button>
				<h4 class="modal-title tc">请选择注册类型</h4>
			</div>
			<div class="modal-body b-modal-body">
				<ul class="row">
					<li class="cho-lg col-md-6">
						<a href="javascript:void(0);" class="j-site-re" >
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

<script type="text/javascript" src="<c:url value="/resources/javascripts/jquery.cityselect.js?_123" />"></script>
<script>
	$("#password").val("");

	//回车事件
	function enterPress(e){
		if(!e) e = window.event;//火狐中是 window.event
		if((e.keyCode || e.which) == 13){
			submitForm();
		}
	}
	//回车事件
	function submitForm(){
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
</script>
</body>
</html>