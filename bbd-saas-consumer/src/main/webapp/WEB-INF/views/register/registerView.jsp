<%@ page import="com.bbd.saas.mongoModels.User" %>
<%@ page import="com.bbd.saas.enums.UserRole" %>
<%@ page import="com.bbd.saas.enums.UserStatus" %>
<%@ page import="com.bbd.saas.utils.PageModel" %>
<%@ page import="com.bbd.saas.enums.ArriveStatus" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
	<title>注册页面</title>
	<jsp:include page="../main.jsp" flush="true" />
</head>
<body class="fbg">
<%

%>
	<!-- S 搜索区域 -->
	<form class="form-inline form-inline-n">
		<div class="search-area">
			<div class="row pb20">
				<div class="form-group col-xs-12 col-sm-6 col-md-4 col-lg-4">
					<label>手机号：</label>
					<input type="text" id="phone" name="phone" placeholder="手机号" class="form-control"  />
				</div>
			</div>
			<div class="row pb20">
				<div class="form-group col-xs-12 col-sm-6 col-md-4 col-lg-4">
					<label>验证码：</label>
					<input type="text" id="yanzhengcode" name="yanzhengcode" placeholder="验证码" class="form-control"  />
					<input type="button" id="yanzhengbutton" name="yanzhengbutton" />

				</div>
			</div>
			<div class="row pb20">
				<div class="form-group col-xs-12 col-sm-6 col-md-4 col-lg-4">
					<label>设置密码：</label>
					<input type="text" id="password" name="password" placeholder="密码" class="form-control"  />
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
						同意<em class="orange">《棒棒达快递注册协议》</em>
					</label>
				</div>
			</div>

			<div class="row pb20">
				<div class="form-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
					<a href="javascript:void(0)" onclick="toSearch();" class="ser-btn l"><i class="b-icon p-query p-ser"></i>立即注册</a>

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

</script>
</body>
</html>