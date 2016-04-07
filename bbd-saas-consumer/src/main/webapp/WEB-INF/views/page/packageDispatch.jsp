<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="keywords" content="棒棒哒-包裹分派页面" />
	<meta name="description" content="棒棒哒-包裹分派页面" />
	<title>包裹分派页面</title>
	<link href="<c:url value="/resources/frame.css" />" rel="stylesheet"  type="text/css" />		
	
</head>
<body>
<div>
	<jsp:include page="../top.jsp" flush="true" />	
</div>
<div class="content">
	<div class="content-left" id="content-left"><jsp:include page="../leftMenu.jsp" flush="true" /></div>
	<div class="content-main" id="content-main">
		<div class="m20">
			<span>状态:
				<select>  
				  <option value ="1">全部</option>  
				  <option value ="2">未到站</option>  
				  <option value="3">已扫描到站</option>  
				</select>  
			</span> 
			<span class="pl20">到站时间：<input id="toSiteTime" name="toSiteTime" type="text" value="2016-04-05"/></span>
		</div>
		<div class="m20">
			<button onclick="showSenderDiv()">选择派件员</button>
			<span id="sender"></span>
			<span class="pl20">扫描运单号：<input id="waybillId" name="waybillId" type="text" /></span>
		</div>
		<div class="m20">
			<table id="data_table" border="1" cellpadding="6px" cellspacing="0px"  style="background-color: #b9d8f3;">
				<tr>
					<td>运单号</td>
					<td>收货人</td>
					<td>收货人地址</td>
					<td>到站时间</td>
					<td>派送员姓名</td>
					<td>派送员手机</td>
					<td>状态</td>
				</tr>
				<tr>
					<td>运单号xxx</td>
					<td>赵六</td>
					<td>北京市-朝阳区-xxxx</td>
					<td>2016-04-03 15:2:23</td>
					<td>周七</td>
					<td>123456xxx</td>
					<td>未分派</td>
				</tr>
				<tr>
					<td>运单号xxx</td>
					<td>赵六</td>
					<td>北京市-朝阳区-xxxx</td>
					<td>2016-04-03 15:2:23</td>
					<td>周七</td>
					<td>123456xxx</td>
					<td>未分派</td>
				</tr>
				<tr>
					<td>运单号xxx</td>
					<td>赵六</td>
					<td>北京市-朝阳区-xxxx</td>
					<td>2016-04-03 15:2:23</td>
					<td>周七</td>
					<td>123456xxx</td>
					<td>未分派</td>
				</tr>
			</table>
			<div class="fr"> 
				<a href="">上页</a>
				<a href="">1</a>
				<a href="">2</a>
				<a href="">3</a>
				<a href="">4</a>
				<a href="">下页</a>
			</div>
		</div>
	</div>
</div>


<!-- 选择派件员弹出窗-开始 -->
<div  id="chooseSender_div" class="popDiv_small" >
	<div>选择派件员</div>
	<div>
		<span>派件员:
			<select id="sender_select">  
				<option value ="张三">张三</option>  
				<option value ="李四">李四</option>  
				<option value="王五">王五</option>  
			</select>				  
		</span> 
	</div>
	<div>
		<button onclick="hideSenderDiv()">取消</button>
		<button onclick="chooseSender()">确定</button>
	</div>
<div>
<!-- 选择派件员弹出窗-结束 -->

<script type="text/javascript" src="<c:url value="/resources/jquery/1.6/jquery.js" />"></script>
<script type="text/javascript" src="<c:url value="/resources/jqueryform/2.8/jquery.form.js" />"></script>
<script type="text/javascript" src="<c:url value="/resources/jqueryui/1.8/jquery.ui.core.js" />"></script>
<script type="text/javascript" src="<c:url value="/resources/jqueryui/1.8/jquery.ui.widget.js" />"></script>
<script type="text/javascript" src="<c:url value="/resources/jqueryui/1.8/jquery.ui.tabs.js" />"></script>
<script type="text/javascript" src="<c:url value="/resources/json2.js" />"></script>
	
<script type="text/javascript">
$(document).ready(function() {
	$("#sender_select").change(function(){
		$("#sender").text($(this).val());
		$("#chooseSender_div").hide();
	});

});
//显示选择派件员div
function showSenderDiv() {
	$("#chooseSender_div").show();
}
//隐藏选择派件员div
function hideSenderDiv() {
	$("#chooseSender_div").hide();
}
//选择派件员
function chooseSender() {
	$("#sender").text($("#sender_select").val());
	$("#chooseSender_div").hide();
}

</script>
</body>
</html>