<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<html>
<head>
	<meta charset="UTF-8">
	<meta http-equiv="Access-Control-Allow-Origin" content="*">
	<meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>
	<link rel="shortcut icon" href="<c:url value="/resources/images/favicon.ico" />"/>
	<link rel="bookmark" href="<c:url value="/resources/images/favicon.ico" />"/>
	<c:set var="ctx" value="<%=request.getContextPath()%>"/>

	<!-- Bootstrap 3.3.2 -->
	<link href="<c:url value="/resources/bootstrap/css/bootstrap.min.css" />" rel="stylesheet"  type="text/css" />
	<!-- Date Picker -->
	<link href="<c:url value="/resources/bootstrap/css/bootstrap-datetimepicker.min.css" />" rel="stylesheet"  type="text/css" />
	<link href="<c:url value="/resources/bootstrap/css/bootstrap-responsive.min.css" />" rel="stylesheet"  type="text/css" />
	<link href="<c:url value="/resources/bootstrap/css/fileinput.css" />" rel="stylesheet"  type="text/css" />
	<!-- FontAwesome 4.3.0 -->
	<link href="<c:url value="/resources/adminLTE/css/font-awesome.min.css" />" rel="stylesheet"  type="text/css" />
	<!-- Ionicons 2.0.0 -->
	<link href="<c:url value="/resources/adminLTE/css/ionicons.min.css" />" rel="stylesheet"  type="text/css" />
	<!-- Theme style -->
	<%--<link href="<c:url value="/resources/adminLTE/css/AdminLTE.css" />" rel="stylesheet"  type="text/css" />--%>
	<!-- AdminLTE Skins. Choose a skin from the css/skins
folder instead of downloading all of them to reduce the load. -->
	<link href="<c:url value="/resources/adminLTE/css/skins/_all-skins.css" />" rel="stylesheet"  type="text/css" />
	<!-- DATA TABLES -->
	<link href="<c:url value="/resources/adminLTE/plugins/datatables/dataTables.bootstrap.css" />" rel="stylesheet"  type="text/css" />
	<!-- iCheck -->
	<link href="<c:url value="/resources/adminLTE/plugins/iCheck/flat/blue.css" />" rel="stylesheet"  type="text/css" />
	<link href="<c:url value="/resources/adminLTE/plugins/iCheck/square/blue.css" />" rel="stylesheet"  type="text/css" />
	<!-- Morris chart -->
	<link href="<c:url value="/resources/adminLTE/plugins/morris/morris.css" />" rel="stylesheet"  type="text/css" />
	<!-- jvectormap -->
	<link href="<c:url value="/resources/adminLTE/plugins/jvectormap/jquery-jvectormap-1.2.2.css" />" rel="stylesheet"  type="text/css" />
	<link href="<c:url value="/resources/adminLTE/plugins/datepicker/datepicker3.css" />" rel="stylesheet"  type="text/css" />
	<!-- Daterange picker -->
	<link href="<c:url value="/resources/adminLTE/plugins/daterangepicker/daterangepicker-bs3.css" />" rel="stylesheet"  type="text/css" />
	<!--text editor -->
	<link href="<c:url value="/resources/umeditor/themes/default/css/umeditor.css" />" rel="stylesheet"  type="text/css" />
	<!-- alertify -->
	<link href="<c:url value="/resources/adminLTE/plugins/alertify/alertify.css" />" rel="stylesheet"  type="text/css" />
	<link href="<c:url value="/resources/adminLTE/plugins/alertify/bootstrap.css" />" rel="stylesheet"  type="text/css" />
	<link href="<c:url value="/resources/stylesheets/main.css" />" rel="stylesheet"  type="text/css" /><!--自定义css-->


	<!-- jQuery 2.1.3 -->
	<script src="<c:url value="/resources/adminLTE/plugins/jQuery/jQuery-2.1.3.min.js" />"> </script>
	<!-- jQuery UI 1.11.2 -->
	<script src="<c:url value="/resources/adminLTE/plugins/src/jquery-ui.min.js" />"> </script>
	<!-- Resolve conflict in jQuery UI tooltip with Bootstrap tooltip -->
	<script type="text/javascript">
		$.widget.bridge('uibutton', $.ui.button);
	</script>

	<!-- Bootstrap 3.3.2 JS -->
	<script src="<c:url value="/resources/bootstrap/js/bootstrap.min.js" />" type="text/javascript"></script>
	<script src="<c:url value="/resources/bootstrap/js/bootstrap-datetimepicker.js" />" type="text/javascript"></script>
	<script src="<c:url value="/resources/bootstrap/js/bootstrap-datetimepicker.zh-CN.js" />" type="text/javascript"></script>
	<script src="<c:url value="/resources/bootstrap/js/fileinput.min.js" />" type="text/javascript"></script>
	<script src="<c:url value="/resources/bootstrap/js/fileinput_locale_zh.js" />" type="text/javascript"></script>
	<script src="<c:url value="/resources/bootstrap/js/jquery.form.js" />" type="text/javascript"></script>
	<!-- Sparkline -->
	<script src="<c:url value="/resources/adminLTE/plugins/sparkline/jquery.sparkline.min.js" />" type="text/javascript"></script>
	<!-- jvectormap -->
	<script src="<c:url value="/resources/adminLTE/plugins/jvectormap/jquery-jvectormap-1.2.2.min.js" />" type="text/javascript"></script>
	<script src="<c:url value="/resources/adminLTE/plugins/jvectormap/jquery-jvectormap-world-mill-en.js" />" type="text/javascript"></script>
	<!-- jQuery Knob Chart -->
	<script src="<c:url value="/resources/adminLTE/plugins/knob/jquery.knob.js" />" type="text/javascript"></script>
	<!-- daterangepicker -->
	<script src="<c:url value="/resources/adminLTE/plugins/daterangepicker/daterangepicker.js" />" type="text/javascript"></script>
	<!-- datepicker -->
	<script src="<c:url value="/resources/adminLTE/plugins/datepicker/bootstrap-datepicker.js" />" type="text/javascript"></script>
	<!-- Bootstrap WYSIHTML5 -->
	<script src="<c:url value="/resources/adminLTE/plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.all.min.js" />" type="text/javascript"></script>
	<!-- DATA TABES SCRIPT -->
	<script src="<c:url value="/resources/adminLTE/plugins/datatables/jquery.dataTables.js" />" type="text/javascript"></script>
	<script src="<c:url value="/resources/adminLTE/plugins/datatables/dataTables.bootstrap.js" />" type="text/javascript"></script>
	<!-- iCheck -->
	<script src="<c:url value="/resources/adminLTE/plugins/iCheck/icheck.min.js" />" type="text/javascript"></script>
	<!-- Slimscroll -->
	<script src="<c:url value="/resources/adminLTE/plugins/slimScroll/jquery.slimscroll.min.js" />" type="text/javascript"></script>
	<!-- FastClick -->
	<script src="<c:url value="/resources/adminLTE/plugins/fastclick/fastclick.min.js" />" type="text/javascript"></script>
	<!-- AdminLTE App -->
	<script src="<c:url value="/resources/adminLTE/js/app.min.js" />"> </script>
	<!-- editor -->
	<script src="<c:url value="/resources/umeditor/umeditor.config.js" />"> </script>
	<script src="<c:url value="/resources/umeditor/umeditor.min.js" />"> </script>
	<script src="<c:url value="/resources/umeditor/lang/zh-cn/zh-cn.js" />"> </script>
	<script src="<c:url value="/resources/javascripts/page/pageBar.js" />"> </script>
	<!-- alertify -->
	<script src="<c:url value="/resources/adminLTE/plugins/alertify/alertify.js" />"> </script>
	<script src="<c:url value="/resources/javascripts/main.js" />"> </script>
	<script src="<c:url value="/resources/javascripts/checkUtil.js" />"> </script>
	<script src="<c:url value="/resources/javascripts/timeUtil.js" />"> </script>
	<script type="text/javascript" src="<c:url value="/resources/javascripts/jquery.cityselect.js?_123" />" ></script>
</head>
<body>
<!--S 提示信息-->
<div class="b-prompt">
	<i class="b-prompt-txt"></i>
</div>
<!--E 提示信息-->
<!-- S alert -->
<div class="j-alert-pop modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog b-modal-dialog middleS" role="document">
		<div class="modal-content">
			<div class="modal-header b-modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
				<h4 class="modal-title tc j-c-tie">提示信息</h4>
			</div>
			<div class="modal-body b-modal-body">
				<em class="f16 j-alert-con">alert内容</em>
				<div class="clearfix mt20">
					<a href="javascript:void(0);" class="sbtn sbtn2 l col-md-12" data-dismiss="modal">确认</a>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- E alert -->
</body>
</html>