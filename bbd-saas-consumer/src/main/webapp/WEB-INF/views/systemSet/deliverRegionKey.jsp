<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="keywords" content="棒棒哒-系统设置-配送区域-绘制电子围栏" />
	<meta name="description" content="棒棒哒-系统设置-配送区域-绘制电子围栏" />
	<title>系统设置-配送区域-绘制电子围栏</title>
	<link href="<c:url value="/resources/frame.css" />" rel="stylesheet"  type="text/css" />	
		
</head>
<body>
<div>
	<jsp:include page="../top.jsp" flush="true" />	
</div>
<div class="content">
	<div class="content-left" id="content-left"><jsp:include page="../leftMenu.jsp" flush="true" /></div>
	<div class="content-main" id="content-main">
	
	<div  style="border-bottom:solid 1px gray;width:1200px">
		<div class="m20" >
			站点名称：<span class="pl20">立水桥 ${sitename}</span><br><br>
			站点地址：<span class="pl20">北京市-朝阳区-立水桥-xxx ${sitename}</span><br><br>
			<span><a href="<c:url value="/deliverRegion/range" />" >配送范围</a></span>
			<span class="pl20"><a href="<c:url value="/deliverRegion/map" />" >绘制电子围栏</a></span>
			<span class="pl20">导入地址关键词</span>
			
		</div>
	</div>
		
		
		<div class="m20" >
			<div>
				<span>导入时间：<input id="importTime" name="importTime" type="text" value="2016-04-05" /></span>
				<span class="pl20">关键词：<input id="keyword" name="keyword" type="text" value="" /></span>
				<button id="queryData" class="pl20">查询</button><br><br>
				
				<button id="importKeyword" class="pl20">导入地址关键词</button>
				<a class="pl20" href="">下载导入模板</a>
				<button id="exportKeyword" class="pl20">导出地址关键词</button>
			</div>
			<br>
			<table id="keyword_table" border="1" cellpadding="6px" cellspacing="0px"  style="background-color: #b9d8f3;">
				<tr>
					<td>选择</td>
					<td>导入日期</td>
					<td>省</td>
					<td>市</td>
					<td>区</td>
					<td>地址关键词</td>
					<td>操作</td>
				</tr>
				<tr>
					<td><input type="checkbox" id="keywordId"> </td>
					<td>2016-04-06</td>
					<td>山东省</td>
					<td>潍坊市</td>
					<td>奎文区</td>
					<td>新华路</td>
					<td><button  onclick="deleteKeyword()">删除</button></td>
				</tr>
				<tr>
					<td colspan="7"><input type="checkbox">全选</td>
				<tr>
			</table>
			<br>
			<button id="batchDelete" >批量删除</button>
		</div>
	</div>
</div>

<script type="text/javascript" src="<c:url value="/resources/jquery/jquery-1.12.3.min.js" />"></script>

<script type="text/javascript">
$(document).ready(function() {
	

});
//删除关键词
function deleteKeyword() {
	alert("确认删除");
}

</script>

</body>
</html>